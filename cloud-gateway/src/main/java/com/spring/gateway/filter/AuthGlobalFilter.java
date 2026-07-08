package com.spring.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.common.constant.JwtConstants;
import com.spring.common.vo.UserInfoVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Order(-100)
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private static final List<String> WHITE_LIST = Arrays.asList(
            "/cloud-auth/auth/login",
            "/auth/register"
    );

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
                .uri("http://127.0.0.1:8084/auth/parseToken",
                        uriBuilder -> uriBuilder.queryParam("token", token).build())
                .retrieve()
                .bodyToMono(UserInfoVO.class)
                .flatMap(result -> {
                    try {
                        String userInfoJson = objectMapper.writeValueAsString(result);
                        ServerHttpRequest newReq = request.mutate()
                                .header(JwtConstants.USER_INFO_HEADER, userInfoJson)
                                .build();
                        return chain.filter(exchange.mutate().build());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
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
        map.put("code", 401);
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