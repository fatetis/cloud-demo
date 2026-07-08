package com.spring.demo.controller;

import com.spring.common.dto.LoginDTO;
import com.spring.common.util.JwtUtil;
import com.spring.common.vo.UserInfoVO;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一认证接口（所有服务登录都走这里）
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * 登录接口（唯一入口）
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        // 1. 校验用户名密码（实际从数据库查询）
        if (!"admin".equals(loginDTO.getUsername()) || !"123456".equals(loginDTO.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 2. 封装用户信息（实际查询用户角色、权限）
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(1L);
        userInfo.setUsername(loginDTO.getUsername());
        userInfo.setRole("ADMIN");

        // 3. 生成JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userInfo.getUserId());
        claims.put("username", userInfo.getUsername());
        claims.put("role", userInfo.getRole());
        return JwtUtil.generateToken(claims);
    }

    /**
     * 校验Token并获取用户信息（网关调用）
     */
    @PostMapping("/parseToken")
    public UserInfoVO parseToken(@RequestParam("token") String token) {

        Claims claims = JwtUtil.getClaims(token);
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(claims.get("userId", Long.class));
        userInfo.setUsername(claims.get("username", String.class));
        userInfo.setRole(claims.get("role", String.class));
        return userInfo;
    }
}
