package io.gitee.dqcer.mcdull.framework.web.filter;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUser;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.mysql.datasource.GlobalDataRoutingDataSource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;

/**
 * http日志
 *
 * @author dqcer
 * @since  2022/10/05
 */
public class HttpTraceLogFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpTraceLogFilter.class);

    private final GlobalDataRoutingDataSource globalDataRoutingDataSource;

    public HttpTraceLogFilter(GlobalDataRoutingDataSource globalDataRoutingDataSource) {
        this.globalDataRoutingDataSource = globalDataRoutingDataSource;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUrl = request.getRequestURI();
        String traceId = this.getTraceId(request);

        try {
            this.setUserSession(traceId, requestUrl);
            if (!isRequestValid(request)) {
                LogHelp.warn(log, "Illegal request. url: {}", requestUrl);
                filterChain.doFilter(request, response);
                return;
            }
            globalDataRoutingDataSource.switchDataSource();
            filterChain.doFilter(request, response);
        } finally {
            globalDataRoutingDataSource.removeDataSource();
            LogHelp.debug(log, "Clean UnifySession.");
            UserContextHolder.clearSession();
            MDC.remove(HttpHeaderConstants.LOG_TRACE_ID);
        }
    }

    private String getTraceId(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER);
        String traceId = (header == null || header.isBlank()) ? RandomUtil.uuid() : header.trim();
        MDC.put(HttpHeaderConstants.LOG_TRACE_ID, traceId);
        return traceId;
    }

    private void setUserSession(String traceId, String requestUrl) {
        LogHelp.debug(log, "Build UnifySession And Initializing TraceId.");
        UnifySession unifySession = new UnifySession();
        unifySession.setTraceId(traceId);
        unifySession.setRequestUrl(requestUrl);
        unifySession.setNow(Date.from(Instant.now()));
        if (StpUtil.isLogin()) {
            unifySession.setUserId(StpUtil.isLogin() ? Convert.toStr(StpUtil.getLoginId()) : null);
            CacheUser cacheUser = StpUtil.getSession().get(GlobalConstant.CACHE_CURRENT_USER, new CacheUser());
            unifySession.copyCommon(cacheUser, unifySession);
        }
        UserContextHolder.setSession(unifySession);
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
