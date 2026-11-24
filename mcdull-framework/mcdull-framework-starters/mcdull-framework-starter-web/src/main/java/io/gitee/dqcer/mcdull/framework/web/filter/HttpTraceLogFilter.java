package io.gitee.dqcer.mcdull.framework.web.filter;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.spi.RequestDataRouting;
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
import java.util.Locale;

/**
 * http日志
 *
 * @author dqcer
 * @since  2022/10/05
 */
public class HttpTraceLogFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpTraceLogFilter.class);

    private final RequestDataRouting dataRouting;

    public HttpTraceLogFilter(RequestDataRouting dataRouting) {
        this.dataRouting = dataRouting;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 如果是OPTIONS预检请求，直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        this.addSecurityHeader(response);

        String requestUrl = request.getRequestURI();
        String traceId = this.getTraceId(request);
        try {
            UnifySession unifySession = this.buildBaseSession(traceId, requestUrl);
            UserContextHolder.setSession(unifySession);
            if (!this.isRequestValid(request)) {
                LogHelp.warn(log, "Illegal request. url: {}", requestUrl);
                UserContextHolder.setDefaultSession();
                filterChain.doFilter(request, response);
                return;
            }
            dataRouting.switchDataSource();
            filterChain.doFilter(request, response);
        } finally {
            LogHelp.debug(log, "Clean UnifySession.");
            dataRouting.removeDataSource();
            UserContextHolder.clearSession();
            MDC.remove(HttpHeaderConstants.LOG_TRACE_ID);
        }
    }

    private void addSecurityHeader(HttpServletResponse response) {
        // 禁用浏览器内容嗅探
        response.setHeader("X-Content-Type-Options", "nosniff");
        // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
        response.setHeader("X-XSS-Protection", "1; mode=block");
    }


    private String getTraceId(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER);
        String traceId = (header != null && header.trim().matches(HttpHeaderConstants.TRACE_ID_PATTERN))
                ? header.trim().toLowerCase(Locale.ROOT)
                : RandomUtil.uuid();
        MDC.put(HttpHeaderConstants.LOG_TRACE_ID, traceId);
        return traceId;
    }

    private UnifySession buildBaseSession(String traceId, String requestUrl) {
        UnifySession unifySession = new UnifySession();
        unifySession.setTraceId(traceId);
        unifySession.setRequestUrl(requestUrl);
        unifySession.setNow(Date.from(Instant.now()));
        return unifySession;
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
