package com.spring.common.web.autoconfigure;

import com.spring.common.web.exception.GlobalExceptionAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 公共模块自动装配配置类
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(name = "org.springframework.web.bind.annotation.RestControllerAdvice")
@ConditionalOnProperty(prefix = "common.exception", name = "enable", havingValue = "true", matchIfMissing = true)
public class CommonAutoConfiguration {

    /**
     * 将全局异常处理器注册为Bean，供引入方微服务使用
     */
    @Bean
    public GlobalExceptionAdvice globalExceptionAdvice() {
        return new GlobalExceptionAdvice();
    }
}