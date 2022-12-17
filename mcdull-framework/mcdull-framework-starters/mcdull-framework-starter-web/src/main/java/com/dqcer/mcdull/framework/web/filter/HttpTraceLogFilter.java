package com.dqcer.mcdull.framework.web.filter;

import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.mcdull.framework.web.event.LogEvent;
import com.dqcer.mcdull.framework.web.feign.model.LogDTO;
import com.dqcer.mcdull.framework.web.transform.SpringContextHolder;
import com.dqcer.mcdull.framework.web.util.IpUtil;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * http日志
 *
 * @author dqcer
 * @version  2022/10/05
 */
public class HttpTraceLogFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final String[] headerNames = {
            HttpHeaderConstants.TRACE_ID_HEADER,
            HttpHeaderConstants.AUTHORIZATION
    };

    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    public static final List<String> pathList = new ArrayList<>();

    static {
        pathList.add("/feign/**");
        pathList.add("/login");
    }


    /**
     * 设置TraceId
     *
     * @param request 请求
     * @return boolean
     */
    private boolean setTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER);
        if(null == traceId || traceId.trim().length() == 0) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put(HttpHeaderConstants.LOG_TRACE_ID, traceId);
        return true;
    }

    /**
     * 忽略Filter
     *
     * @param path 路径
     * @return boolean
     */
    public boolean ignoreFilter(String path, List<String> list) {
        return list.stream().anyMatch(url -> path.startsWith(url) || PATH_MATCHER.match(url, path));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        long startTime = System.currentTimeMillis();
        boolean isSetTraceId = false;
        try {
            isSetTraceId = setTraceId(request);
            filterChain.doFilter(request, response);
            status = response.getStatus();
        } finally {
            String path = request.getRequestURI();
            if (!Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())
                    && !ignoreFilter(path, pathList)) {
                long latency = System.currentTimeMillis() - startTime;
                LogDTO entity = new LogDTO();
                entity.setClientIp(IpUtil.getIpAddr(request));
                entity.setUserAgent(getUserAgent(request));
                entity.setHeaders(getHeaders(request));
                entity.setRequestBody(getRequestBody(request));
//                entity.setResponseBody(getResponseBody(response));
                entity.setParameterMap(getParamsMap(request));
                entity.setPath(path);
                entity.setMethod(request.getMethod());
                entity.setCreatedTime(new Date());
                entity.setTimeTaken(latency);
                entity.setStatus((long) status);
                entity.setAccountId(Long.valueOf(request.getHeader(HttpHeaderConstants.U_ID)));

                RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
                SpringContextHolder.publishEvent(new LogEvent(entity));

                if(isSetTraceId) {
                    MDC.remove(HttpHeaderConstants.LOG_TRACE_ID);
                }
            }
            updateResponse(response);
        }
    }

    private static String getUserAgent(HttpServletRequest request) {
        String userAgent = null;
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            if (element.equalsIgnoreCase("User-Agent")) {
                userAgent = request.getHeader(element);
            }
        }
        return userAgent;
    }


    /**
     * @param request http request
     * @return boolean
     */
    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    /**
     * @param request req
     * @return headers
     */
    private String getHeaders(HttpServletRequest request) {
        StringBuilder headerStr = new StringBuilder();
        headerStr.append('{');
        for (String headerName : headerNames) {
            String value = request.getHeader(headerName);
            headerStr.append(headerName).append('=');
            if(null != value && value.trim().length() != 0) {
                headerStr.append(value);
            }
            headerStr.append(',');
        }
        if(headerStr.length() > 1) {
            headerStr.deleteCharAt(headerStr.length() - 1);
        }
        headerStr.append('}');
        return headerStr.toString();
    }

    /**
     * @param request req
     * @return headers
     */
    private String getParamsMap(HttpServletRequest request) {
        StringBuilder paramsStr = new StringBuilder();
        paramsStr.append('{');
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String[] values = entry.getValue();
            paramsStr.append(entry.getKey()).append('=');
            if(values != null && values.length > 0) {
                List<String> valueList = Arrays.asList(values);
                paramsStr.append(valueList);
            } else {
                paramsStr.append("[]");
            }
            paramsStr.append(',');
        }
        if(paramsStr.length() > 1) {
            paramsStr.deleteCharAt(paramsStr.length() - 1);
        }
        paramsStr.append('}');
        return paramsStr.toString();
    }

    /**
     * @param request req
     * @return request body
     */
    private String getRequestBody(HttpServletRequest request) {
        String requestBody = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            requestBody = new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        }
        return requestBody;
    }


    /**
     * @param response resp
     * @throws IOException ex
     */
    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }

}
