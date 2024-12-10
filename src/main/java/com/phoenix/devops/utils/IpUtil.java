package com.phoenix.devops.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author god-lamp
 * @since 2024-04-08
 */
@Log4j2
public final class IpUtil {
    // 判断是否在该网段中
    public static boolean isIpAddressInSubnet(String ipAddress, String subnetCIDR) {
        boolean isIpAddressInSubnet = false;
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            SubnetUtils subnetUtils = new SubnetUtils(subnetCIDR);

            if (subnetUtils.getInfo().isInRange(inetAddress.getHostAddress())) {
                isIpAddressInSubnet = true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace(System.err);
        }
        return isIpAddressInSubnet;
    }

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "127.0.0.1";
    // 客户端与服务器同为一台机器，获取的 ip 有时候是 ipv6 格式
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String SEPARATOR = ",";

    // 根据 HttpServletRequest 获取 IP
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCALHOST_IP.equalsIgnoreCase(ip) || LOCALHOST_IPV6.equalsIgnoreCase(ip)) {
                // 根据网卡取本机配置的 IP
                InetAddress iNet = null;
                try {
                    iNet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace(System.err);
                }
                if (iNet != null) {
                    ip = iNet.getHostAddress();
                }
            }
        }
        // 对于通过多个代理的情况，分割出第一个 IP
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(SEPARATOR) > 0) {
                ip = ip.substring(0, ip.indexOf(SEPARATOR));
            }
        }
        String currentIP = LOCALHOST_IPV6.equals(ip) ? LOCALHOST_IP : ip;
        log.info("当前IP:{}", currentIP);
        return currentIP;
    }
}
