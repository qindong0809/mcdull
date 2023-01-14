package com.dqcer.mcdull.framework.web.filter;

import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.framework.base.storage.UnifySession;
import com.dqcer.framework.base.storage.UserContextHolder;
import com.dqcer.framework.base.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * http日志
 *
 * @author dqcer
 * @version  2022/10/05
 */
public class HttpTraceLogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HttpTraceLogFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrl = request.getRequestURI();

        if (log.isDebugEnabled()) {
            log.debug("Filter url: {}", requestUrl);
        }
        if (!isRequestValid(request)) {
            log.warn("非法请求....");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String traceId = request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER);
            if(null == traceId || traceId.trim().length() == 0) {
                traceId = RandomUtil.uuid();
            }
            MDC.put(HttpHeaderConstants.LOG_TRACE_ID, traceId);
            UnifySession unifySession = new UnifySession();
            unifySession.setTraceId(traceId);
            unifySession.setRequestUrl(requestUrl);
            UserContextHolder.setSession(unifySession);
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clearSession();
            MDC.remove(HttpHeaderConstants.LOG_TRACE_ID);
        }
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
}
