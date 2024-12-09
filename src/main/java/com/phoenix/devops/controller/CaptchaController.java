package com.phoenix.devops.controller;

import cn.hutool.core.util.StrUtil;
import com.phoenix.devops.model.vo.CaptchaVO;
import com.phoenix.devops.service.ISysCaptchaService;
import com.phoenix.devops.utils.ServletUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2024-12-01
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Resource
    private ISysCaptchaService captchaService;

    @PermitAll
    @GetMapping()
    @Operation(summary = "获得验证码")
    public CaptchaVO get(@NotNull HttpServletRequest request) {
        return captchaService.get();
    }

    @PermitAll
    @PostMapping()
    @Operation(summary = "校验验证码")
    public CaptchaVO check(@Validated @RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.check(data);
    }

    public static String getRemoteId(HttpServletRequest request) {
        String ip = ServletUtils.getClientIpAddress(request);
        String ua = request.getHeader("user-agent");
        if (StrUtil.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }
}
