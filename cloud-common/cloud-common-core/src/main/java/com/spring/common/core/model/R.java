package com.spring.common.core.model;

import com.spring.common.core.constant.ResultCode;
import com.spring.common.core.util.TraceIdUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局统一返回结果
 * 兼容 SpringMVC、SpringCloud Gateway WebFlux
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    /**
     * 响应码 0成功，其他失败
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 全局唯一请求ID，用于日志排查
     */
    private String requestId;

    //====================成功静态方法====================
    public static <T> R<T> ok() {
        return R.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .msg(ResultCode.SUCCESS.getMsg())
                .requestId(TraceIdUtil.getRequestId())
                .build();
    }

    public static <T> R<T> ok(T data) {
        return R.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .msg(ResultCode.SUCCESS.getMsg())
                .data(data)
                .requestId(TraceIdUtil.getRequestId())
                .build();
    }

    public static <T> R<T> ok(String msg, T data) {
        return R.<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .msg(msg)
                .data(data)
                .requestId(TraceIdUtil.getRequestId())
                .build();
    }

    //====================失败静态方法====================
    public static <T> R<T> fail() {
        return R.<T>builder()
                .code(ResultCode.FAIL.getCode())
                .msg(ResultCode.FAIL.getMsg())
                .requestId(TraceIdUtil.getRequestId())
                .build();
    }

    public static <T> R<T> fail(String msg) {
        return R.<T>builder()
                .code(ResultCode.FAIL.getCode())
                .msg(msg)
                .requestId(TraceIdUtil.getRequestId())
                .build();
    }

    public static <T> R<T> fail(Integer code, String msg) {
        return R.<T>builder()
                .code(code)
                .msg(msg)
                .requestId(TraceIdUtil.getRequestId())
                .build();
    }

    public static <T> R<T> fail(ResultCode resultCode) {
        return R.<T>builder()
                .code(resultCode.getCode())
                .msg(resultCode.getMsg())
                .requestId(TraceIdUtil.getRequestId())
                .build();
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }
}
