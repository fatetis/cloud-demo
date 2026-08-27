package com.spring.common.api.autoconfigure;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.spring.common.api.feign")
public class CommonFeignAutoConfiguration {
}
