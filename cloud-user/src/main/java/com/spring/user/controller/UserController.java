package com.spring.user.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

//    获取登录用户信息例子
    @GetMapping("/user/hello")
    public String hello(@RequestHeader("user-info") String userInfoVO) {
        JSONObject jsonObject = JSONUtil.parseObj(userInfoVO);
        return "欢迎用户"+jsonObject.get("username") + "登录";
    }

    @GetMapping("/internal/hello")
    public String inView(){
        return "✅ 内部服务，不对外开放 SpringBoot4.0.5 正常运行";
    }
}
