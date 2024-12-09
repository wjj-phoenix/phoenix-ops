package com.phoenix.devops.security;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.phoenix.devops.enums.RespEnum;
import com.phoenix.devops.lang.Constant;
import com.phoenix.devops.utils.ResponseUtil;
import com.phoenix.devops.utils.TokenProviderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Slf4j
@Order(0)
@Component
public class IAuthenticationTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public IAuthenticationTokenFilter(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        boolean match = Constant.WHITES.stream()
                .map(p -> new AntPathRequestMatcher(p.getValue(), p.getKey().toString()))
                .anyMatch(matcher -> matcher.matches(request));
        if (match) {
            chain.doFilter(request, response);
            return;
        }

        Optional<UserDetails> maybeUserDetail = compoundAuth(request, response);
        if (maybeUserDetail.isPresent()) {
            UserDetails userDetail = maybeUserDetail.get();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetail.getUsername(), userDetail.getPassword(), userDetail.getAuthorities()
            );
            auth.setDetails(userDetail);
            SecurityContextHolder.getContext().setAuthentication(auth);
            // log.info("user 【{}】 logged into the system at the {}.", userDetail.getUsername(), LocalDateTime.now());
        }
        chain.doFilter(request, response);
    }

    private Optional<UserDetails> compoundAuth(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(Constant.HEADER);
        if (StrUtil.isBlank(header)) {
            ResponseUtil.writeErrMsg(response, RespEnum.REQUEST_HEADER_NULL);
        }
        Pair<String, String> token = null;

        // token验证
        if (StringUtils.startsWith(header, Constant.JWT_AUTH_PREFIX)) {
            token = Pair.of(Constant.JWT_AUTH_PREFIX, StringUtils.removeStart(header, Constant.JWT_AUTH_PREFIX));
        }
        // 密码验证
        if (StringUtils.startsWith(header, Constant.BASIC_AUTH_PREFIX)) {
            token = Pair.of(Constant.BASIC_AUTH_PREFIX, StringUtils.removeStart(header, Constant.BASIC_AUTH_PREFIX));
        }

        Assert.notNull(token, "请求头token异常");

        if (StrUtil.equals(token.getKey(), Constant.JWT_AUTH_PREFIX)) {
            Boolean validate = TokenProviderUtil.validate(token.getValue());
            if (!validate) {
                ResponseUtil.writeErrMsg(response, RespEnum.TOKEN_INVALID);
            }
            return Optional.of(userDetailsService.loadUserByUsername(TokenProviderUtil.getAccount(token.getValue())));
        }
        if (StrUtil.equals(token.getKey(), Constant.BASIC_AUTH_PREFIX)) {
            Pair<String, String> userAndPassword = TokenProviderUtil.decodeBasicAuthToken(token.getValue());
            UserDetails userDetail = userDetailsService.loadUserByUsername(userAndPassword.getKey());
            if (!passwordEncoder.matches(userAndPassword.getValue(), userDetail.getPassword())) {
                ResponseUtil.writeErrMsg(response, RespEnum.USERNAME_OR_PASSWORD_ERROR);
            }
            return Optional.of(userDetail);
        }
        return Optional.empty();
    }
}
