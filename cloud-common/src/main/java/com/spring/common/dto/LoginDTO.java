package com.spring.common.dto;

import lombok.Data;

// 登录请求DTO
@Data
public class LoginDTO {
    private String username;
    private String password;
}

