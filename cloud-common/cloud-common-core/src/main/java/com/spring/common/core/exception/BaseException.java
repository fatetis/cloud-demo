package com.spring.common.core.exception;

import com.spring.common.core.constant.ResultCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Integer code;
    private final String message;

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.message = resultCode.getMsg();
    }
}