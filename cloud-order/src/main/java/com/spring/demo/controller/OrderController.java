package com.spring.demo.controller;

import com.spring.demo.feign.UserFeign;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Resource
    private UserFeign userFeign;

    @GetMapping("/order/create")
    public String create(){
        String res = userFeign.userHello();
        String res1 = userFeign.inView();
        return "✅ 订单服务调用成功 -> " + res + res1;
    }
}
