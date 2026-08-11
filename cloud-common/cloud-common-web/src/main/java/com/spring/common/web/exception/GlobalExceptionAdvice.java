package com.spring.common.web.exception;

import com.spring.common.core.constant.ResultCode;
import com.spring.common.core.exception.BaseException;
import com.spring.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public R<Void> handleBaseException(BaseException e) {
        log.error("业务异常 code={},msg={}",e.getCode(),e.getMessage(),e);
        return R.fail(e.getCode(), e.getMessage());
    }

    // 通用异常
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常",e);
        return R.fail(ResultCode.SERVER_ERROR);
    }
}