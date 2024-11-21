package com.phoenix.devops.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.phoenix.devops.lang.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Slf4j
public final class TokenProviderUtil {
    public static String token(String username) {
        return JWT.create()
                // 设置签发时间
                .setIssuedAt(DateUtil.date())
                // 设置过期时间
                .setExpiresAt(DateUtil.offsetHour(DateUtil.date(), Constant.OFFSET_HOUR))
                // 设置签发时间
                .setNotBefore(DateUtil.date())
                // .setIssuer(PropertiesValue.author)
                // .setSubject(PropertiesValue.application)
                .setJWTId(UUID.randomUUID().toString())
                .setPayload("username", username)
                .setKey(Constant.KEY)
                .sign();
    }

    public static Boolean validate(String token) {
        JWTValidator jwtValidator = JWTValidator.of(token)
                .validateAlgorithm(JWTSignerUtil.hs256(Constant.KEY))
                // leeway:容忍空间，单位：秒
                .validateDate(DateUtil.date(), 5);
        return jwtValidator != null;
    }

    public static String getAccount(String token) {
        final JWT jwt = JWTUtil.parseToken(token);
        return (String) jwt.getPayload("username");
    }

    /**
     * 解析登录时的 jwt token
     *
     * @param token token
     * @return pair
     */
    public static Pair<String, String> decodeBasicAuthToken(String token) {
        String plain = new String(Base64.decode(token), StandardCharsets.UTF_8);
        // split() 将提供的文本拆分为具有最大长度、指定分隔符的数组
        String[] userAndPassword = StringUtils.split(plain, ":", 2);
        if (userAndPassword.length != 2) {
            throw new IllegalStateException("bad authentication token");
        }
        return Pair.of(userAndPassword[0], userAndPassword[1]);
    }
}
