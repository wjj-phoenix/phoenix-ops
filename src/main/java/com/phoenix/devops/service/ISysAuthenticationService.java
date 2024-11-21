package com.phoenix.devops.service;

import cn.hutool.core.lang.Pair;
import com.phoenix.devops.model.LoginInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
public interface ISysAuthenticationService extends UserDetailsService {
    /**
     * 用户登录请求
     *
     * @param info 登录输入信息
     * @return 登录成功的响应
     */
    Pair<String, String> login(LoginInfo info, HttpServletResponse response);

    /**
     * 退出认证
     *
     * @param response 响应体
     * @return 响应消息
     */
    String logout(HttpServletResponse response);

    /**
     * 获取验证码
     *
     * @param request  请求体
     * @param response 响应体
     */
    void verify(HttpServletRequest request, HttpServletResponse response);
}
