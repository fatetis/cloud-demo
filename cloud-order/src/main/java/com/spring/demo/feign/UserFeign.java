package com.spring.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("cloud-user")
public interface UserFeign {
    @GetMapping("/user/hello")
    String userHello();

    @GetMapping("/internal/hello")
    String inView();
}