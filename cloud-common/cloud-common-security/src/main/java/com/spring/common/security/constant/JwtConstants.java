package com.spring.common.security.constant;

/**
 * JWT通用常量（所有服务共用）
 */
public class JwtConstants {
    // JWT密钥（生产环境放配置中心，建议至少32位）
    public static final String JWT_SECRET = "your-256bit-secret-key-1234567890123456";
    // Token过期时间：24小时
    public static final Long JWT_EXPIRE = 86400000L;
    // Token请求头key
    public static final String TOKEN_HEADER = "Authorization";
    // Token前缀
    public static final String TOKEN_PREFIX = "Bearer ";
    // 用户信息请求头（网关透传给业务服务）
    public static final String USER_INFO_HEADER = "user-info";
    // 解析JWT-Token地址
    public static final String JWT_PARSE_TOKEN_LINK = "http://127.0.0.1:8088/cloud-auth/auth/parseToken";

}
