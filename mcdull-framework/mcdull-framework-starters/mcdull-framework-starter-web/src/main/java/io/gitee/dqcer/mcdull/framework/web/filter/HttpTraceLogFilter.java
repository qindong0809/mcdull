package io.gitee.dqcer.mcdull.framework.web.filter;

import cn.dev33.satoken.stp.StpUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * http日志
 *
 * @author dqcer
 * @since  2022/10/05
 */
public class HttpTraceLogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HttpTraceLogFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrl = request.getRequestURI();

        try {
            String traceId = request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER);
            if(null == traceId || traceId.trim().length() == 0) {
                traceId = RandomUtil.uuid();
            }
            MDC.put(HttpHeaderConstants.LOG_TRACE_ID, traceId);
            UnifySession unifySession = new UnifySession();
            unifySession.setTraceId(traceId);
            unifySession.setRequestUrl(requestUrl);
            unifySession.setNow(new Date());
            unifySession.setUserId(StpUtil.isLogin() ? StpUtil.getLoginIdAsInt() : null);
            UserContextHolder.setSession(unifySession);

            if (log.isDebugEnabled()) {
                log.debug("Filter url: {}", requestUrl);
            }
            if (!isRequestValid(request)) {
                log.warn("非法请求....");
                filterChain.doFilter(request, response);
                return;
            }

            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clearSession();
            MDC.remove(HttpHeaderConstants.LOG_TRACE_ID);
        }
    }

    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
        } catch (URISyntaxException e) {
            log.warn(e.getMessage(), e);
            return false;
        }
        return true;
    }
}
