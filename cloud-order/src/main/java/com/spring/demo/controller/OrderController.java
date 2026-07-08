package com.spring.demo.controller;

import com.spring.demo.feign.UserFeign;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.spring.common.constant.JwtConstants;
import com.spring.common.vo.UserInfoVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private UserFeign userFeign;

    /**
     * 业务接口：直接获取用户信息，无授权代码
     */
    @GetMapping("/info")
    public String getOrderInfo(HttpServletRequest request) {
        System.out.println(222);
        // 从网关透传的请求头获取用户信息
        String userInfoJson = request.getHeader(JwtConstants.USER_INFO_HEADER);
        try {
            UserInfoVO userInfo = objectMapper.readValue(userInfoJson, UserInfoVO.class);
            return "订单查询成功，当前用户：" + userInfo.getUsername() + "，角色：" + userInfo.getRole();
        } catch (Exception e) {
            return "用户信息解析失败";
        }
    }

    @GetMapping("/create")
    public String create(){
        String res = userFeign.userHello();
        String res1 = userFeign.inView();
        return "✅ 订单服务调用成功 -> " + res + res1;
    }


}
