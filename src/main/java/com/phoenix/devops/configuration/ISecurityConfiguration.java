package com.phoenix.devops.configuration;

import com.phoenix.devops.lang.Constant;
import com.phoenix.devops.security.IAccessDeniedHandler;
import com.phoenix.devops.security.IAuthenticationEntryPoint;
import com.phoenix.devops.security.IAuthenticationTokenFilter;
import com.phoenix.devops.service.ISysAuthenticationService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Configuration
public class ISecurityConfiguration {
    @Resource
    private IAccessDeniedHandler accessDeniedHandler;
    @Resource
    private IAuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    private IAuthenticationTokenFilter authenticationTokenFilter;
    @Resource
    private ISysAuthenticationService userDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> {
            RequestMatcher[] matchers = new RequestMatcher[Constant.WHITES.size()];
            for (int i = 0; i < Constant.WHITES.size(); i++) {
                Pair<HttpMethod, String> white = Constant.WHITES.get(i);
                matchers[i] = new AntPathRequestMatcher(white.getValue(), white.getKey().name());
            }
            authorizeHttpRequests
                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                    .requestMatchers(matchers).permitAll()
                    .anyRequest().authenticated();
        });

        // 关闭跨站请求防护及不使用session
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 自定义权限拒绝处理类
        httpSecurity.exceptionHandling(exception -> {
            exception.accessDeniedHandler(accessDeniedHandler);
            exception.authenticationEntryPoint(authenticationEntryPoint);
        });

        // 自定义权限拦截器JWT过滤器
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // httpSecurity.addFilterBefore(imageCodeValidateFilter, authenticationTokenFilter.getClass());
        httpSecurity.userDetailsService(userDetailService);
        return httpSecurity.build();
    }
}
