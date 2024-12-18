package com.phoenix.devops.utils;

import java.util.UUID;

/**
 * @author wjj-phoenix
 * @since 2024-12-10
 * 链路追踪工具类
 */
public class TracerUtils {

    /**
     * 私有化构造方法
     */
    private TracerUtils() {
    }

    /**
     * 获得链路追踪编号，直接返回 SkyWalking 的 TraceId。
     * 如果不存在的话为空字符串！！！
     *
     * @return 链路追踪编号
     */
    public static String getTraceId() {
        return UUID.randomUUID().toString();
    }

}
