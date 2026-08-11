package com.spring.common.core.util;

import java.util.UUID;

/**
 * 全局唯一请求ID工具
 */
public final class TraceIdUtil {

    private TraceIdUtil() {
    }

    /**
     * 获取全局唯一requestId
     */
    public static String getRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 截取短id，32位转16位，可选
     */
    public static String getShortRequestId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 16);
    }
}