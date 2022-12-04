package com.dqcer.mcdull.framework.web.filter;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.mcdull.framework.web.event.LogEvent;
import com.dqcer.mcdull.framework.web.remote.LogDTO;
import com.dqcer.mcdull.framework.web.transform.SpringContextHolder;
import com.dqcer.mcdull.framework.web.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
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
import java.time.LocalTime;
import java.util.*;

/**
 * http日志
 *
 * @author dqcer
 * @version  2022/10/05
 */
public class HttpTraceLogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HttpTraceLogFilter.class);

    private static final String[] headerNames = {
            HttpHeaderConstants.TRACE_ID_HEADER,
            HttpHeaderConstants.AUTHORIZATION
    };

    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";


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
            if (Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())) {
                //1. 记录日志
                HttpTraceLog traceLog = new HttpTraceLog();
                traceLog.setPath(path);
                traceLog.setMethod(request.getMethod());
                long latency = System.currentTimeMillis() - startTime;
                traceLog.setTimeTaken(latency);
                traceLog.setTime(LocalTime.now().toString());
                traceLog.setHeaders(getHeaders(request));
                traceLog.setParameterMap(getParamsMap(request));
                traceLog.setStatus(status);
                traceLog.setRequestBody(getRequestBody(request));
                // TODO: 2022/11/17 是否考虑去掉打印返回参数
//                traceLog.setResponseBody(getResponseBody(response));

                String userAgent = getUserAgent(request);

                UserAgent parse = UserAgentUtil.parse(userAgent);

                LogDTO entity = new LogDTO();
                entity.setClientIp(IpUtil.getIpAddr(request));
                entity.setBrowser(parse.getBrowser().getName());
                entity.setEngine(parse.getEngine().getName());
                entity.setPlatform(parse.getPlatform().getName());
                entity.setMobile(parse.isMobile() ? 1 : 2);
                entity.setOs(parse.getOs().getName());
                entity.setVersion(parse.getVersion());
                entity.setEngineVersion(parse.getEngineVersion());
                entity.setHeaders(JSONObject.toJSONString(getHeaderMap(request)));
                entity.setRequestBody(traceLog.getRequestBody());
                entity.setResponseBody(traceLog.getResponseBody());
                entity.setParameterMap(traceLog.getParameterMap());
                entity.setPath(traceLog.getPath());
                entity.setMethod(request.getMethod());
//                entity.setTime(traceLog.getTime());
//                entity.setStatus(traceLog.getStatus());
                entity.setCreatedTime(new Date());
                entity.setTimeTaken(traceLog.getTimeTaken());


//                eventTrackService.save(entity);
                RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
                SpringContextHolder.publishEvent(new LogEvent(entity));

                log.info("Http trace log: {}", traceLog);
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

    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap();
        Enumeration<String> names = request.getHeaderNames();

        while(names.hasMoreElements()) {
            String name = (String)names.nextElement();
            headerMap.put(name, request.getHeader(name));
        }

        return headerMap;
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


    /**
     * 日志记录
     */
    private static class HttpTraceLog {

        private String time;
        private String method;
        private String path;
        private Long timeTaken;
        private Integer status;
        private String parameterMap;
        private String requestBody;
        private String headers;
        private String responseBody;

        @Override
        public String toString() {
            return "HttpTraceLog{" +
                    "time='" + time + '\'' +
                    ", method='" + method + '\'' +
                    ", path='" + path + '\'' +
                    ", timeTaken=" + timeTaken +
                    ", status=" + status +
                    ", parameterMap='" + parameterMap + '\'' +
                    ", requestBody='" + requestBody + '\'' +
                    ", headers='" + headers + '\'' +
                    '}';
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getParameterMap() {
            return parameterMap;
        }

        public void setParameterMap(String parameterMap) {
            this.parameterMap = parameterMap;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Long getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(Long timeTaken) {
            this.timeTaken = timeTaken;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getRequestBody() {
            return requestBody;
        }

        public void setRequestBody(String requestBody) {
            this.requestBody = requestBody;
        }

        public String getHeaders() {
            return headers;
        }

        public void setHeaders(String headers) {
            this.headers = headers;
        }

        public String getResponseBody() {
            return responseBody;
        }

        public void setResponseBody(String responseBody) {
            this.responseBody = responseBody;
        }
    }

}
