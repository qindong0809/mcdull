package io.gitee.dqcer.mcdull.framework.web.util;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * servlet 工具类
 *
 * @author dqcer
 * @since 2023/01/28
 */
public class ServletUtil {

    private static final Logger log = LoggerFactory.getLogger(ServletUtil.class);

    /**
     * 设置下载excel http头
     *
     * @param response 响应
     * @param fileName 文件名称
     */
    public static void setDownloadExcelHttpHeader(HttpServletResponse response, String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException();
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            LogHelp.error(log, e.getMessage(), e);
            throw new BusinessException();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
    }

    public static String getUserAgent() {
        HttpServletRequest request = getRequest();
        String userAgent = null;
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            if ("User-Agent".equalsIgnoreCase(element)) {
                userAgent = request.getHeader(element);
            }
        }
        return userAgent;
    }


    public static void setDownloadFileHeader(HttpServletResponse response, String fileName, Long fileSize) {
        response.setCharacterEncoding("utf-8");
        try {
            if (fileSize != null) {
                response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize));
            }
            if (StrUtil.isNotEmpty(fileName)) {
                response.setHeader(HttpHeaders.CONTENT_TYPE,
                        MediaTypeFactory.getMediaType(fileName).orElse(MediaType.APPLICATION_OCTET_STREAM) + ";charset=utf-8");
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
                        URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
                response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            }
        } catch (UnsupportedEncodingException e) {
            LogHelp.error(log, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            if (attributes instanceof ServletRequestAttributes) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) attributes;
                return requestAttributes.getRequest();
            }
        }
        throw new IllegalStateException();
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            if (attributes instanceof ServletRequestAttributes) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) attributes;
                return requestAttributes.getResponse();
            }
        }
        throw new IllegalStateException();
    }
}
