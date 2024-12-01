package com.phoenix.devops.utils;

import cn.hutool.json.JSONUtil;
import com.phoenix.devops.enums.RespEnum;
import com.phoenix.devops.lang.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author wjj-phoenix
 * @since 2024-11-21
 */
@Slf4j
public final class ResponseUtil {

    /**
     * 异常消息返回(适用过滤器中处理异常响应)
     *
     * @param response HttpServletResponse
     * @param status   状态码
     * @param message  响应消息
     */
    public static void writeErrMsg(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (PrintWriter writer = response.getWriter()) {
            String jsonResponse = JSONUtil.toJsonStr(Result.failure(status, message));
            writer.print(jsonResponse);
            // 确保将响应内容写入到输出流
            writer.flush();
        } catch (IOException e) {
            log.error("响应异常处理失败", e);
        }
        log.error("出现的异常状态码：{}，异常信息：{}", status, message);
    }

    /**
     * 异常消息返回(适用过滤器中处理异常响应)
     *
     * @param response HttpServletResponse
     * @param resp     响应结果枚举
     */
    public static void writeErrMsg(HttpServletResponse response, RespEnum resp) {
        response.setStatus(resp.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (PrintWriter writer = response.getWriter()) {
            String jsonResponse = JSONUtil.toJsonStr(Result.failure(resp));
            writer.print(jsonResponse);
            // 确保将响应内容写入到输出流
            writer.flush();
        } catch (IOException e) {
            log.error("响应异常处理失败", e);
        }
        log.error("出现的异常信息:{}", resp.getMessage());
    }
}
