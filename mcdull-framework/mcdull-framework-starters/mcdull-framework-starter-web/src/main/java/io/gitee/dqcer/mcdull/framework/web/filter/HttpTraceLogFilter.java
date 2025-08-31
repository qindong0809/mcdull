package io.gitee.dqcer.mcdull.framework.web.filter;

import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUser;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.config.properties.GatewayProperties;
import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.mysql.datasource.GlobalDataRoutingDataSource;
import io.gitee.dqcer.mcdull.framework.security.StpKit;
import io.gitee.dqcer.mcdull.framework.web.advice.BaseExceptionAdvice;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * http日志
 *
 * @author dqcer
 * @since  2022/10/05
 */
public class HttpTraceLogFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpTraceLogFilter.class);
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String [] EXCLUDE_PATTERNS = {
        GlobalConstant.LOGIN_URL,
        GlobalConstant.INNER_API + GlobalConstant.ALL_PATTERNS,
        GlobalConstant.FAVICON_ICO,
        GlobalConstant.ACTUATOR_ALL,
        "/druid/**",
        "/upload" + GlobalConstant.ALL_PATTERNS,
        "/doc.html/**",
        "/doc-ui.html/**",
        "/doc-resources/**",
        "/webjars/**",
        "/error",
        "/home/upload" + GlobalConstant.ALL_PATTERNS,
        "/v3/def-docs/**",
        "/v3/api-docs/**",
        "/error",
    };

    private final GlobalDataRoutingDataSource globalDataRoutingDataSource;
    private final McdullProperties mcdullProperties;
    private final DynamicLocaleMessageSource dynamicLocaleMessageSource;
    private final BaseExceptionAdvice baseExceptionAdvice;


    public HttpTraceLogFilter(GlobalDataRoutingDataSource globalDataRoutingDataSource,
                              McdullProperties mcdullProperties,
                              DynamicLocaleMessageSource dynamicLocaleMessageSource,
                              BaseExceptionAdvice baseExceptionAdvice) {
        this.globalDataRoutingDataSource = globalDataRoutingDataSource;
        this.mcdullProperties = mcdullProperties;
        this.dynamicLocaleMessageSource = dynamicLocaleMessageSource;
        this.baseExceptionAdvice = baseExceptionAdvice;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        this.addSecurityHeader(response);

        String requestUrl = request.getRequestURI();
        String traceId = this.getTraceId(request);
        try {
            UnifySession unifySession = this.buildBaseSession(traceId, requestUrl);
            UserContextHolder.setSession(unifySession);
            if (!this.isRequestValid(request)) {
                LogHelp.warn(log, "Illegal request. url: {}", requestUrl);
                filterChain.doFilter(request, response);
                return;
            }
            globalDataRoutingDataSource.switchDataSource();
            if (this.shouldSkipAuth(requestUrl)) {
                LogHelp.debug(log, "Should skip auth. url：{}", requestUrl);
                filterChain.doFilter(request, response);
                return;
            }
            CacheUser cacheUser = this.validateAndGetUser(requestUrl);
            unifySession.copyCommon(cacheUser);
            filterChain.doFilter(request, response);
        } catch (SaTokenException exception) {
            this.errorResult(response, exception);
        } finally {
            LogHelp.debug(log, "Clean UnifySession.");
            globalDataRoutingDataSource.removeDataSource();
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

    private void errorResult(HttpServletResponse response, SaTokenException exception) {
        Result<?> result = baseExceptionAdvice.getResult(exception, dynamicLocaleMessageSource);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, MediaType.ALL_VALUE);
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(out, result);
            out.flush();
        } catch (IOException e) {
            LogHelp.error(log, "logRequest error.", e);
            throw new RuntimeException(e);
        }
    }

    private CacheUser validateAndGetUser(String requestUrl) {
        if (PATH_MATCHER.match(requestUrl, GlobalConstant.ADMINISTRATOR_PATH + GlobalConstant.ALL_PATTERNS)) {
            return StpKit.ADMIN.getSession().get(GlobalConstant.CACHE_CURRENT_USER, new CacheUser());
        }
        return StpKit.DEFAULT.getSession().get(GlobalConstant.CACHE_CURRENT_USER, new CacheUser());
    }


    public boolean shouldSkipAuth(String path) {
        GatewayProperties gateway = mcdullProperties.getGateway();
        List<String> noAuthList = gateway.getNoAuth();
        noAuthList = CollUtil.defaultIfEmpty(noAuthList, new ArrayList<>());
        noAuthList.addAll(Arrays.asList(EXCLUDE_PATTERNS));
        return noAuthList.stream().anyMatch(url -> path.startsWith(url) || PATH_MATCHER.match(url, path));
    }


    private String getTraceId(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER);
        String traceId = (header == null || header.isBlank()) ? RandomUtil.uuid() : header.trim();
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
