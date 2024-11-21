package com.phoenix.devops.security;

import com.phoenix.devops.enums.RespEnum;
import com.phoenix.devops.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 * 自定义返回结果：没有权限访问时
 */
@Component
public class IAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        ResponseUtil.writeErrMsg(response, RespEnum.ACCESS_UNAUTHORIZED.getCode(), ex.getMessage());
    }
}