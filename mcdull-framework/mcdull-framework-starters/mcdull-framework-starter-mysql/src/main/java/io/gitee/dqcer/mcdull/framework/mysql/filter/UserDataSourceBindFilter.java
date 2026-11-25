package io.gitee.dqcer.mcdull.framework.mysql.filter;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.mysql.service.UserDataSourceBindingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用户绑定数据源过滤器：在鉴权会话加载完成后，按已认证用户切换其绑定的数据源。
 * <p>
 * 必须排在 AuthSessionFilter（order=3）之后：此时 {@link UnifySession} 已回填登录用户信息。
 * 数据源逻辑内聚在 starter-mysql，不侵入 starter-security。
 *
 * @author dqcer
 */
public class UserDataSourceBindFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(UserDataSourceBindFilter.class);

    private final UserDataSourceBindingService bindingService;

    public UserDataSourceBindFilter(UserDataSourceBindingService bindingService) {
        this.bindingService = bindingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        UnifySession session = UserContextHolder.getSession();
        if (session != null && session.getUserId() != null) {
            try {
                bindingService.bind(session.getUserId(), request.getHeader(HttpHeaderConstants.T_ID));
            } catch (RuntimeException e) {
                // 租户校验失败/数据源上限异常：不吞异常，由全局异常处理器渲染业务错误
                LogHelp.warn(log, "绑定用户数据源失败. userId={}, cause={}",
                        session.getUserId(), e.getMessage());
                throw e;
            }
        }
        filterChain.doFilter(request, response);
    }
}
