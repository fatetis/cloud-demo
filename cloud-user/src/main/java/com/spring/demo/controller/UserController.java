package com.spring.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/user/hello")
    public String hello(){
        return "✅ 用户服务 SpringBoot4.0.5 正常运行";
    }

    @GetMapping("/internal/hello")
    public String inView(){
        return "✅ 内部服务，不对外开放 SpringBoot4.0.5 正常运行";
    }
}
