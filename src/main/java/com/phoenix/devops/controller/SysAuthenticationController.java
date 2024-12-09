package com.phoenix.devops.controller;

import cn.hutool.core.lang.Pair;
import com.phoenix.devops.model.LoginInfo;
import com.phoenix.devops.service.ISysAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@RestController
@RequestMapping()
public class SysAuthenticationController {
    @Resource
    private ISysAuthenticationService service;

    @GetMapping("/verify")
    @Operation(summary = "获取登录验证码")
    public void verify(HttpServletRequest request, HttpServletResponse response) {
        service.verify(request, response);
    }

    @PostMapping("/login")
    @Operation(summary = "登录接口，隐含两个参数code和key")
   /*  @Parameters({
            @Parameter(name = "code", description = "验证码"),
            @Parameter(name = "key", description = "随机ID"),
    }) */
    public Pair<String, String> login(@Validated @RequestBody LoginInfo info, HttpServletResponse response) {
        return service.login(info, response);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录接口")
    public String logout(HttpServletResponse response) {
        return service.logout(response);
    }

    @GetMapping("/info")
    @Operation(summary = "获取个人信息")
    public String info(HttpServletResponse response) {
        return service.logout(response);
    }

    @GetMapping("/get-id-by-username")
    @PermitAll
    @Operation(summary = "使用租户名，获得租户编号", description = "登录界面，根据用户的租户名，获得租户编号")
    @Parameter(name = "username", description = "租户名", required = true, example = "1024")
    public Long getTenantIdByName(@RequestParam("username") String username) {
        return service.fetchSysAccountIdByUsername(username);
    }
}
