package com.spring.common.util;

import com.spring.common.constant.JwtConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类（所有服务共用）
 */
public class JwtUtil {

    // 生成安全密钥
    private static final SecretKey KEY = Keys.hmacShaKeyFor(JwtConstants.JWT_SECRET.getBytes());

    /**
     * 生成JWT Token
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims) // 存储用户信息
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.JWT_EXPIRE))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析Token获取载荷
     */
    public static Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token.replace(JwtConstants.TOKEN_PREFIX, ""))
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token已过期");
        } catch (SecurityException | MalformedJwtException e) {
            throw new RuntimeException("Token非法");
        } catch (Exception e) {
            throw new RuntimeException("Token解析失败");
        }
    }

    /**
     * 校验Token是否有效
     */
    public static boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}