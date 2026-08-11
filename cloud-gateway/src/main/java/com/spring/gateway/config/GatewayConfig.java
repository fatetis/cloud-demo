package com.spring.gateway.config;

import com.spring.gateway.exception.GatewayGlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class GatewayConfig {
    @Bean
    public WebExceptionHandler gatewayGlobalExceptionHandler(ObjectMapper objectMapper) {
        return new GatewayGlobalExceptionHandler(objectMapper);
    }
}