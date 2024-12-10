package com.phoenix.devops.service;

import com.phoenix.devops.entity.SysAccount;
import com.phoenix.devops.model.LoginInfo;
import com.phoenix.devops.model.vo.LoginRespVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;

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
    LoginRespVO login(LoginInfo info, HttpServletResponse response);

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

    /**
     * 根据认证信息查询用户信息
     *
     * @param principal 认证信息
     * @return 账户信息
     */
    SysAccount info(Principal principal);

    Long fetchSysAccountIdByUsername(String username);
}
