package com.phoenix.devops.security;

import com.phoenix.devops.enums.RespEnum;
import com.phoenix.devops.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 * 自定义返回结果：未登录或登录过期
 */
@Slf4j
@Component
public class IAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        int status = response.getStatus();
        if (status == HttpServletResponse.SC_NOT_FOUND) {
            // 资源不存在
            ResponseUtil.writeErrMsg(response, RespEnum.NOT_FOUND);
        } else {
            if (ex instanceof BadCredentialsException) {
                // 用户名或密码错误
                ResponseUtil.writeErrMsg(response, RespEnum.USERNAME_OR_PASSWORD_ERROR);
            } else {
                // 未认证或者token过期
                ResponseUtil.writeErrMsg(response, RespEnum.TOKEN_INVALID);
            }
        }
    }
}

