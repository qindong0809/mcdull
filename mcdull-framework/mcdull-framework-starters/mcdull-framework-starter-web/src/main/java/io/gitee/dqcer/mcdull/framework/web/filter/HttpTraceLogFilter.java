package io.gitee.dqcer.mcdull.framework.web.filter;

import cn.dev33.satoken.stp.StpUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
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

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrl = request.getRequestURI();
        LogHelp.debug(log, "start filter. url: {}", requestUrl);
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

            if (!isRequestValid(request)) {
                LogHelp.warn(log, "Illegal request. url: {}", requestUrl);
                filterChain.doFilter(request, response);
                return;
            }

            filterChain.doFilter(request, response);
        } finally {
            LogHelp.debug(log, "end filter. url: {}", requestUrl);
            UserContextHolder.clearSession();
            MDC.remove(HttpHeaderConstants.LOG_TRACE_ID);
        }
    }

    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
        } catch (URISyntaxException e) {
            LogHelp.warn(log, e.getMessage(), e);
            return false;
        }
        return true;
    }
}
