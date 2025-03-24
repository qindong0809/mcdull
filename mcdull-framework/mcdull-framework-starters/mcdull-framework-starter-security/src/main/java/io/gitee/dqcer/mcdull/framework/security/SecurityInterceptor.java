package io.gitee.dqcer.mcdull.framework.security;

import cn.dev33.satoken.fun.SaParamFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.enums.LanguageEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 安全拦截器  在sa-token拦截器基础上增加用户上下文
 *
 * @author dqcer
 * @since 2024/04/28
 */
public class SecurityInterceptor extends SaInterceptor {

    public SecurityInterceptor() {
    }

    public SecurityInterceptor(SaParamFunction<Object> auth) {
        super(auth);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean preHandle = super.preHandle(request, response, handler);

        // 设置用户上下文
        this.setUserContextHolder(request);
        return preHandle;
    }

    private <T> void setUserContextHolder(HttpServletRequest request) {
        String tokenValue = StpUtil.getTokenValue();
        Object userId = StpUtil.getLoginIdByToken(tokenValue);

        UnifySession unifySession = new UnifySession();
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (language == null) {
            language = LanguageEnum.ZH_CN.getCode();
        } else {
            language = language.substring(0, language.indexOf(','));
        }
        unifySession.setLanguage(language);
        unifySession.setUserId(Convert.toStr(userId));
        unifySession.setTraceId(request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER));
        Boolean administratorFlag = StpUtil.getSession().get(GlobalConstant.ADMINISTRATOR_FLAG, false);
        unifySession.setAdministratorFlag(administratorFlag);
        UserContextHolder.setSession(unifySession);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        UserContextHolder.clearSession();
    }
}
