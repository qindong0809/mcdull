package com.dqcer.mcdull.gateway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Objects;

/**
 * IP 工具类
 *
 * @author dqcer
 * @version 2022/10/28
 */
public class IpUtils {

    private IpUtils() {
    }

    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    private static final String X_REAL_IP = "X-Real-IP";
    private static final String UNKNOWN = "unknown";
    private static final String SPLIT = ",";


    /**
     * 获取请求的真实IP
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getRealIp(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst(X_FORWARDED_FOR);
        if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            boolean contains = ip.contains(SPLIT);
            if (contains) {
                ip = ip.split(",")[0];
            }
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(PROXY_CLIENT_IP);
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(WL_PROXY_CLIENT_IP);
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(HTTP_CLIENT_IP);
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(HTTP_X_FORWARDED_FOR);
        }
        if (checkIp(ip)) {
            ip = headers.getFirst(X_REAL_IP);
        }
        if (checkIp(ip)) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        }
        return ip;
    }

    private static boolean checkIp(String ip) {
        return ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip);
    }
}
