package io.gitee.dqcer.mcdull.framework.security.filter;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.same.SaSameUtil;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUserSession;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.security.config.GatewayProperties;
import io.gitee.dqcer.mcdull.framework.security.StpKit;
import io.gitee.dqcer.mcdull.framework.security.advice.SaTokenExceptionAdvice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Same-Token 校验与登录态解析。
 * <p>
 * 先校验网关下发的 Same-Token，确认外部请求来自网关；再按请求路径选择对应的 sa-token 账号体系，
 * 把会话里的用户信息并入 {@link UnifySession}；未登录时由
 * {@link StpKit} 抛出 {@code SaTokenException} 并在此渲染 401 响应体。这段逻辑原先长在
 * starter-web 的 {@code HttpTraceLogFilter} 里，属于鉴权能力，搬回 security 后 web 只负责 trace 与上下文。
 * <p>
 * 必须排在 {@code HttpTraceLogFilter}（order=2）之后：它要往后者建好的 {@link UnifySession} 上并字段。
 *
 * @author dqcer
 * @since 2026-11-19
 */
public class AuthSessionFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthSessionFilter.class);
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String[] EXCLUDE_PATTERNS = {
        GlobalConstant.LOGIN_URL + GlobalConstant.ALL_PATTERNS,
        GlobalConstant.INNER_API + GlobalConstant.ALL_PATTERNS,
        GlobalConstant.FAVICON_ICO,
        GlobalConstant.ACTUATOR_ALL,
        "/druid" + GlobalConstant.ALL_PATTERNS,
        "/monitoring" + GlobalConstant.ALL_PATTERNS,
        "/upload" + GlobalConstant.ALL_PATTERNS,
        "/error",
        "/home/upload" + GlobalConstant.ALL_PATTERNS,
        "/mp3" + GlobalConstant.ALL_PATTERNS,
    };

    /**
     * 不需要校验 Same-Token 的路径。
     * <p>
     * /inner/** 是 Feign 直连内部接口，不经过网关、没有 Same-Token；其余为基础设施与静态资源路径。
     * 登录等 no-auth 接口仍由网关转发，必须持有 Same-Token，因此不在此列。
     */
    private static final String[] SAME_TOKEN_EXCLUDE_PATTERNS = {
        GlobalConstant.INNER_API + GlobalConstant.ALL_PATTERNS,
        GlobalConstant.IGNORE_URL + GlobalConstant.ALL_PATTERNS,
        GlobalConstant.ACTUATOR_ALL,
        GlobalConstant.FAVICON_ICO,
        "/error",
        "/druid" + GlobalConstant.ALL_PATTERNS,
        "/monitoring" + GlobalConstant.ALL_PATTERNS,
        "/upload" + GlobalConstant.ALL_PATTERNS,
        "/home/upload" + GlobalConstant.ALL_PATTERNS,
        "/mp3" + GlobalConstant.ALL_PATTERNS,
        "/doc.html/**",
        "/doc-ui.html/**",
        "/doc-resources/**",
        "/webjars/**",
        "/v3/api-docs/**",
        "/v3/def-docs/**",
    };

    private final GatewayProperties gatewayProperties;
    private final SaTokenExceptionAdvice saTokenExceptionAdvice;

    public AuthSessionFilter(GatewayProperties gatewayProperties, SaTokenExceptionAdvice saTokenExceptionAdvice) {
        this.gatewayProperties = gatewayProperties;
        this.saTokenExceptionAdvice = saTokenExceptionAdvice;
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
        String requestUrl = request.getRequestURI();
        try {
            if (this.shouldCheckSameToken(requestUrl)) {
                SaSameUtil.checkToken(request.getHeader(SaSameUtil.SAME_TOKEN));
            }
            if (this.shouldSkipAuth(requestUrl)) {
                LogHelp.debug(log, "Should skip auth. url：{}", requestUrl);
                UserContextHolder.setDefaultSession();
                filterChain.doFilter(request, response);
                return;
            }
            CacheUserSession cacheUser = this.validateAndGetUser(requestUrl);
            UnifySession unifySession = UserContextHolder.getSession();
            if (unifySession != null) {
                unifySession.copyCommon(cacheUser);
            }
            filterChain.doFilter(request, response);
        } catch (SaTokenException exception) {
            this.errorResult(response, exception);
        }
    }

    private CacheUserSession validateAndGetUser(String requestUrl) {
        if (PATH_MATCHER.match(GlobalConstant.ADMINISTRATOR_PATH + GlobalConstant.ALL_PATTERNS, requestUrl)) {
            return StpKit.ADMIN.getSession().get(GlobalConstant.CACHE_CURRENT_ADMINISTRATOR_USER, new CacheUserSession());
        }
        if (PATH_MATCHER.match(GlobalConstant.APP_PATH + GlobalConstant.ALL_PATTERNS, requestUrl)) {
            return StpKit.APP_USER.getSession().get(GlobalConstant.CACHE_CURRENT_APP_USER, new CacheUserSession());
        }
        return StpKit.DEFAULT.getSession().get(GlobalConstant.CACHE_CURRENT_USER, new CacheUserSession());
    }

    private boolean shouldCheckSameToken(String path) {
        if (!gatewayProperties.isSameTokenEnabled()) {
            return false;
        }
        return Arrays.stream(SAME_TOKEN_EXCLUDE_PATTERNS)
                .noneMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    public boolean shouldSkipAuth(String path) {
        List<String> allNoAuthList = new ArrayList<>();
        List<String> noAuthList = gatewayProperties.getNoAuth();
        if (CollUtil.isNotEmpty(noAuthList)) {
            allNoAuthList.addAll(noAuthList);
        }
        allNoAuthList.addAll(Arrays.asList(EXCLUDE_PATTERNS));
        allNoAuthList.add(GlobalConstant.IGNORE_URL + GlobalConstant.ALL_PATTERNS);
        return allNoAuthList.stream().anyMatch(url -> PATH_MATCHER.match(url, path));
    }

    private void errorResult(HttpServletResponse response, SaTokenException exception) {
        var result = saTokenExceptionAdvice.toResult(exception);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            // CORS响应头由高优先级的CorsFilter统一处理，此处不再手工设置（手工设置*且缺少凭证头会被浏览器拦截）
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(out, result);
            out.flush();
        } catch (IOException e) {
            LogHelp.error(log, "logRequest error.", e);
            throw new RuntimeException(e);
        }
    }
}
