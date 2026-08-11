package com.spring.common.core.constant;

/**
 * 全局统一响应码
 * 0 成功；4xx客户端错误；5xx服务端错误
 */
public enum ResultCode {

    SUCCESS(0, "操作成功"),
    FAIL(-1, "操作失败"),

    // 客户端
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请登录"),
    FORBIDDEN(403, "权限不足，禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    // 服务端
    SERVER_ERROR(500, "服务器内部异常"),
    GATEWAY_ERROR(502, "网关服务异常"),
    SERVICE_UNAVAILABLE(503, "服务不可用");

    private final Integer code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
