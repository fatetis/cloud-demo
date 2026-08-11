package com.spring.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.common.core.constant.ResultCode;
import com.spring.common.core.model.R;
import com.spring.common.security.constant.JwtConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Order(-99)
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

//    读取配置文件
    @Value("#{'${spring.cloud.gateway.auth.white-list:}'.split(',')}")
    private final List<String> WHITE_LIST;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        if (WHITE_LIST.contains(path)) {
            return chain.filter(exchange);
        }

        String token = request.getHeaders().getFirst(JwtConstants.TOKEN_HEADER);
        if (token == null || token.isBlank()) {
            log.warn("路径 {} 未携带token", path);
            return build401Resp(exchange, "未登录，请传入Authorization令牌");
        }
        log.info("请求路径:{}, token:{}", path, token);

        return webClient.post()
                .uri(JwtConstants.JWT_PARSE_TOKEN_LINK)
                .header(JwtConstants.TOKEN_HEADER, token)
                .retrieve()
                .bodyToMono(R.class)
                .flatMap(result -> {
                    try {
                        String userInfoJson = objectMapper.writeValueAsString(result.getData());
                        System.out.println("授权返回："+userInfoJson);
                        ServerHttpRequest newReq = request.mutate()
                                .header(JwtConstants.USER_INFO_HEADER, userInfoJson)
                                .build();
                        return chain.filter(exchange.mutate().build());
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                })
                .onErrorResume(e -> {
                    log.error("鉴权服务调用异常", e);
                    return build401Resp(exchange, "鉴权服务调用失败：" + e.getMessage());
                });
    }

    private Mono<Void> build401Resp(ServerWebExchange exchange, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("code", ResultCode.UNAUTHORIZED.getCode());
        map.put("msg", msg);

        try {
            byte[] bytes = objectMapper.writeValueAsString(map).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("组装响应失败", e);
            return Mono.error(e);
        }
    }
}