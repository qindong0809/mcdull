package io.gitee.dqcer.mcdull.framework.web.util;

import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
            log.error(e.getMessage(), e);
            throw new BusinessException();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
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
