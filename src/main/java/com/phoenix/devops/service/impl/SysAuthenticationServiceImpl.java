package com.phoenix.devops.service.impl;

import com.phoenix.devops.entity.SysAccount;
import com.phoenix.devops.entity.SysMenu;
import com.phoenix.devops.enums.LoginLogTypeEnum;
import com.phoenix.devops.enums.LoginResultEnum;
import com.phoenix.devops.enums.RespEnum;
import com.phoenix.devops.enums.UserTypeEnum;
import com.phoenix.devops.fastmap.IFastMap;
import com.phoenix.devops.model.LoginInfo;
import com.phoenix.devops.model.common.ResponseModel;
import com.phoenix.devops.model.dto.LoginLogCreateReqDTO;
import com.phoenix.devops.model.vo.CaptchaVO;
import com.phoenix.devops.model.vo.LoginRespVO;
import com.phoenix.devops.service.ISysAccountService;
import com.phoenix.devops.service.ISysAuthenticationService;
import com.phoenix.devops.service.ISysCaptchaService;
import com.phoenix.devops.service.ISysMenuService;
import com.phoenix.devops.utils.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.header.CacheControlServerHttpHeadersWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Slf4j
@Service
public class SysAuthenticationServiceImpl implements ISysAuthenticationService {
    @Resource
    private ISysAccountService accountService;

    @Resource
    private ISysMenuService menuService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private Validator validator;

    @Resource
    private ISysCaptchaService captchaService;

    @Resource
    private IFastMap<String, String> fastMap;

    @Override
    public LoginRespVO login(LoginInfo info, HttpServletResponse response) {
        log.info("登录输入的请求体: {}", info);
        validateCaptcha(info);
        SysAccount account = accountService.fetchAccountByUsername(info.getUsername());
        Assert.notNull(account, String.format("指定用户【%s】不存在", info.getUsername()));
        log.info("user: {}", account);

        String password = info.getPassword();

        if (!passwordEncoder.matches(password, account.getPassword())) {
            ResponseUtil.writeErrMsg(response, RespEnum.PASSWORD_ERROR);
        }
        if (!account.isEnabled() || !account.isAccountNonLocked()) {
            ResponseUtil.writeErrMsg(response, RespEnum.ACCOUNT_STATUS_INVALID);
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("user 【{}】 logged into the system at the {}.", info.getUsername(), LocalDateTime.now());

        // return Pair.of("token", TokenProviderUtil.token(info.getUsername()));
        return LoginRespVO.builder()
                .userId(account.getId())
                .accessToken(TokenProviderUtil.token(info.getUsername()))
                .refreshToken(TokenProviderUtil.token(info.getUsername()))
                .expiresTime(LocalDateTime.now())
                .build();
    }


    private void validateCaptcha(LoginInfo reqVO) {
        // 校验验证码
        ValidationUtils.validate(validator, reqVO, LoginInfo.CodeEnableGroup.class);
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(reqVO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        // 验证不通过
        if (!response.isSuccess()) {
            // 创建登录失败日志（验证码不正确)
            createLoginLog(null, reqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.CAPTCHA_CODE_ERROR);
            throw new IllegalArgumentException("验证码不正确");
        }
    }

    private void createLoginLog(Long userId, String username, LoginLogTypeEnum logTypeEnum, LoginResultEnum loginResult) {
        // 插入登录日志
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logTypeEnum.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(getUserType().getValue());
        reqDTO.setUsername(username);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        // IpUtil.getIpAddress(request);
        reqDTO.setUserIp("");
        reqDTO.setResult(loginResult.getResult());
        // loginLogService.createLoginLog(reqDTO);
        // 更新最后登录时间
        if (userId != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
            accountService.updateAccountLogin(userId, "");
        }
    }

    private UserTypeEnum getUserType() {
        return UserTypeEnum.ADMIN;
    }

    @Override
    public String logout(HttpServletResponse response) {
        try {
            SecurityContextHolder.clearContext();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.flushBuffer();
        } catch (IOException ex) {
            log.error("退出认证失败，原因：{}", ex.getMessage());
            throw new IllegalStateException("请求失败");
        }
        return "success";
    }

    @Override
    public void verify(HttpServletRequest request, HttpServletResponse response) {
        // 验证码的过期时间
        long expiredTime = 3 * 60 * 1000;
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", CacheControlServerHttpHeadersWriter.PRAGMA_VALUE);
        response.setDateHeader("Expires", System.currentTimeMillis() + expiredTime);
        response.setHeader("Access-Control-Expose-Headers", "key");
        String uuid = UUID.randomUUID().toString().replace("-", "");
        response.setHeader("key", uuid);
        try {
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            String randomCode = randomValidateCode.getRandomCode(request, response);
            log.info("生成的验证码: {}；随机ID: {}", randomCode, uuid);
            fastMap.put(uuid, randomCode);
            // 设置过期的回调事件
            fastMap.expire(uuid, expiredTime, (key, val) -> fastMap.remove(uuid));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public SysAccount info(Principal principal) {
        return accountService.fetchSysAccountWithRelationsByUsername(principal.getName());
    }

    @Override
    public Long fetchSysAccountIdByUsername(String username) {
        SysAccount account = accountService.fetchAccountByUsername(username);
        Assert.notNull(account, String.format("用户名[%s]不存在!", username));
        return account.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取用户信息
        SysAccount account = accountService.fetchAccountByUsername(username);
        Assert.notNull(account, String.format("指定的用户【%s】不存在", username));
        List<SysMenu> menus = menuService.fetchMenusByAccountId(account.getId());
        List<SimpleGrantedAuthority> authorities = menus.stream().map(menu -> new SimpleGrantedAuthority(menu.getUrl())).collect(Collectors.toList());
        account.setAuthorities(authorities);
        return account;
    }
}
