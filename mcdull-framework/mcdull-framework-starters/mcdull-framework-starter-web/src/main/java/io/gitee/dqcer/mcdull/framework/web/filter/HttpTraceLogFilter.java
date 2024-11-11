package io.gitee.dqcer.mcdull.framework.web.filter;

import cn.dev33.satoken.stp.StpUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUser;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Locale;

/**
 * http日志
 *
 * @author dqcer
 * @since  2022/10/05
 */
public class HttpTraceLogFilter extends OncePerRequestFilter {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUrl = request.getRequestURI();
        try {
            String traceId = request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER);
            if(null == traceId || traceId.trim().isEmpty()) {
                traceId = RandomUtil.uuid();
            }
            MDC.put(HttpHeaderConstants.LOG_TRACE_ID, traceId);
            LogHelp.debug(log, "Build UnifySession And Initializing TraceId.");
            UnifySession<Object> unifySession = new UnifySession<>();
            unifySession.setTraceId(traceId);
            unifySession.setRequestUrl(requestUrl);
            unifySession.setNow(new Date());
            if (StpUtil.isLogin()) {
                unifySession.setUserId(StpUtil.isLogin() ? StpUtil.getLoginId() : null);
                CacheUser cacheUser = StpUtil.getSession().get(GlobalConstant.CACHE_CURRENT_USER, new CacheUser());
                unifySession.setAdministratorFlag(cacheUser.getAdministratorFlag());
                unifySession.setDateFormat(cacheUser.getDateFormat());
                unifySession.setZoneIdStr(cacheUser.getZoneIdStr());
                unifySession.setLocale(new Locale(cacheUser.getLanguage()));
                unifySession.setTenantId(cacheUser.getTenantId());
                unifySession.setLoginName(cacheUser.getLoginName());
            }
            UserContextHolder.setSession(unifySession);

            if (!isRequestValid(request)) {
                LogHelp.warn(log, "Illegal request. url: {}", requestUrl);
                filterChain.doFilter(request, response);
                return;
            }

            filterChain.doFilter(request, response);
        } finally {
            LogHelp.debug(log, "Clean UnifySession.");
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
