package com.spring.common.vo;


import lombok.Data;

// 用户信息VO
@Data
public class UserInfoVO {
    private Long userId;
    private String username;
    private String role;
}
