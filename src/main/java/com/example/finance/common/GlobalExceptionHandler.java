package com.example.finance.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理，保证异常场景也返回统一 Result 结构。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.fail(ResultCode.PARAM_ERROR, e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<Void> handleValidException(Exception e) {
        String msg = e instanceof MethodArgumentNotValidException validException
                ? validException.getBindingResult().getAllErrors().get(0).getDefaultMessage()
                : ((BindException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.fail(ResultCode.PARAM_ERROR, msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        return Result.fail(ResultCode.PARAM_ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        return Result.fail(ResultCode.SERVER_ERROR, e.getMessage());
    }
}
