package com.phoenix.devops.lang;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpMethod;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
public final class Constant {
    public static final Integer OFFSET_HOUR = 6;
    public static final String HEADER = "Authorization";
    public static final String JWT_AUTH_PREFIX = "Bearer ";
    public static final String BASIC_AUTH_PREFIX = "Basic ";
    public static final byte[] KEY = "1234567890".getBytes();

    public static final String PASSWORD = "123456";

    public static final List<Pair<HttpMethod, String>> WHITES = new LinkedList<>();

    static {
        // 注意，后面的url需要写：
        //  1. 整个请求映射地址
        //  2. 可接受**通配符
        WHITES.add(Pair.of(HttpMethod.GET, "/**"));
        WHITES.add(Pair.of(HttpMethod.POST, "/**"));
        WHITES.add(Pair.of(HttpMethod.POST, "/login"));
        WHITES.add(Pair.of(HttpMethod.POST, "/logout"));
        WHITES.add(Pair.of(HttpMethod.GET, "/verify"));
        WHITES.add(Pair.of(HttpMethod.GET, "/favicon.ico"));
        WHITES.add(Pair.of(HttpMethod.GET, "/swagger-ui.html"));
        WHITES.add(Pair.of(HttpMethod.GET, "/swagger-ui/**"));
        WHITES.add(Pair.of(HttpMethod.GET, "/v3/**"));
    }

    public static final String CUSTOM_CACHE_MANAGER = "customizedRedisCacheManager";

    public static final String CUSTOM_CACHE_KEY_GENERATOR = "customizedkKeyGenerator";
}
