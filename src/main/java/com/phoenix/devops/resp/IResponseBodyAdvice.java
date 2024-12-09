package com.phoenix.devops.resp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.devops.annotation.NotControllerRespAdvice;
import com.phoenix.devops.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Slf4j
@RestControllerAdvice(basePackages = {"com.phoenix.devops.controller"})
public class IResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter parameter, @NonNull MediaType type, @NonNull Class<? extends HttpMessageConverter<?>> converterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        if (body instanceof Result) {
            return body;
        }

        if (body instanceof String) {
            // 如果返回值是String类型，那就手动把Result对象转换成JSON字符串
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(Result.success(body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return Result.success(body);
    }

    @Override
    public boolean supports(@NonNull MethodParameter param, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 若返回类型已包装或不需要包装，则直接返回
        return !(Objects.requireNonNull(param).getParameterType().isAssignableFrom(Result.class) || param.hasMethodAnnotation(NotControllerRespAdvice.class));
    }
}

