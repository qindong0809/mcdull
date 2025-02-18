package io.gitee.dqcer.mcdull.framework.web.util;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;

import jakarta.servlet.http.HttpServletRequest;

/**
 * ip 工具类
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class IpUtil {

    public IpUtil() {
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return HttpHeaderConstants.UNKNOWN;
        } else {
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || HttpHeaderConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }

            if (ip == null || ip.length() == 0 || HttpHeaderConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }

            if (ip == null || ip.length() == 0 || HttpHeaderConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ip == null || ip.length() == 0 || HttpHeaderConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }

            if (ip == null || ip.length() == 0 || HttpHeaderConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }

            return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip.split(",")[0];
        }
    }
}
