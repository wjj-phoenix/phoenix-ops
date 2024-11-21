package com.phoenix.devops.lang;

import com.phoenix.devops.enums.RespEnum;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Getter
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    @Parameter(description = "状态码")
    private final int code;
    /**
     * 响应消息
     */
    @Parameter(description = "响应消息")
    private final String message;
    /**
     * 响应内容
     */
    @Parameter(description = "响应体")
    private final T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(200, "success", data);
    }

    public static <T> Result<T> failure(RespEnum resp) {
        return new Result<T>(resp.getCode(), resp.getMessage(), null);
    }

    public static <T> Result<T> failure(int code, String message) {
        return new Result<T>(code, message, null);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(10000, message, null);
    }
}
