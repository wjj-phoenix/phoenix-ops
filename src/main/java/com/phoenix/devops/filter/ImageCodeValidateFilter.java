package com.phoenix.devops.filter;

import cn.hutool.core.util.StrUtil;
import com.phoenix.devops.enums.RespEnum;
import com.phoenix.devops.fastmap.IFastMap;
import com.phoenix.devops.utils.ResponseUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Log4j2
@Component
public class ImageCodeValidateFilter extends OncePerRequestFilter {
    @Resource
    private IFastMap<String, String> fastMap;
    private static final AntPathRequestMatcher LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");
    public static final String CAPTCHA_CODE_PARAM_NAME = "code";
    public static final String CAPTCHA_KEY_PARAM_NAME = "key";


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        // 检验登录接口的验证码
        if (LOGIN_PATH_REQUEST_MATCHER.matches(request)) {
            // 请求中的验证码
            String captchaCode = request.getParameter(CAPTCHA_CODE_PARAM_NAME);

            if (StrUtil.isBlank(captchaCode)) {
                ResponseUtil.writeErrMsg(response, RespEnum.VERIFY_CODE_NOT_NULL);
            }

            // 缓存中的验证码
            String verifyCodeKey = request.getParameter(CAPTCHA_KEY_PARAM_NAME);
            String cacheVerifyCode = fastMap.get(verifyCodeKey);

            if (cacheVerifyCode == null) {
                ResponseUtil.writeErrMsg(response, RespEnum.VERIFY_CODE_TIMEOUT);
            } else {
                // 验证码比对
                if (captchaCode.equalsIgnoreCase(cacheVerifyCode)) {
                    chain.doFilter(request, response);
                } else {
                    ResponseUtil.writeErrMsg(response, RespEnum.VERIFY_CODE_ERROR);
                }
            }
        } else {
            // 非登录接口放行
            chain.doFilter(request, response);
        }
    }
}
