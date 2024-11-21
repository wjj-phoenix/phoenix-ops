package com.phoenix.devops.exception;

import com.phoenix.devops.enums.RespEnum;
import com.phoenix.devops.lang.Result;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Nullable> doException(Exception ex) {
        log.error("服务出现的异常: {}", ex.getMessage());
        ex.printStackTrace(System.err);
        return Result.failure(RespEnum.UNKNOWN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Nullable> doException(HttpMessageNotReadableException ex) {
        log.error("服务读取消息出现异常: {}", ex.getMessage());
        return Result.failure("Http 消息不可读异常");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public Result<Nullable> doAuthenticationException(AuthenticationException ex) {
        log.error("认证失败异常:{}", ex.getMessage());
        return Result.failure(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public Result<Nullable> doIllegalStateException(IllegalStateException ex) {
        log.error("运行服务出现的IllegalStateException异常:{}", ex.getMessage());
        return Result.failure(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Nullable> doIllegalArgumentException(IllegalArgumentException ex) {
        log.error("运行服务出现的IllegalArgumentException异常:{}", ex.getMessage());
        return Result.failure(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<Nullable> doMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("参数校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(";");
        }
        return Result.failure(sb.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ConstraintViolationException.class})
    public Result<Nullable> doConstraintViolationException(ConstraintViolationException ex) {
        return Result.failure(ex.getMessage());
    }
}
