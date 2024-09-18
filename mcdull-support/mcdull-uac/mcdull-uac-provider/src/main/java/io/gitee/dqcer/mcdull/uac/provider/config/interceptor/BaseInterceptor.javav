package io.gitee.dqcer.mcdull.uac.provider.config.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.enums.LanguageEnum;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础信息拦截器
 *
 * @author dqcer
 * @since 2021/08/19
 */
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(BaseInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUrl = request.getRequestURI();
        LogHelp.debug(log, StrUtil.format("BaseInfoInterceptor#preHandle requestURI:[{}]", requestUrl));

        if (! (handler instanceof HandlerMethod)) {
            return true;
        }
        boolean login = StpUtil.isLogin();
        if (login) {
            String tokenValue = StpUtil.getTokenValue();
            Object userId = StpUtil.getLoginIdByToken(tokenValue);
            // 设置用户上下文
            this.setUserContextHolder(userId, request);
        }
        return true;
    }

    private <T> void setUserContextHolder(T userId, HttpServletRequest request) {
        UnifySession<T> unifySession = new UnifySession<>();
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (language == null) {
            language = LanguageEnum.ZH_CN.getCode();
        } else {
            language = language.substring(0, language.indexOf(','));
        }
        unifySession.setLanguage(language);
        unifySession.setUserId(userId);
        unifySession.setTraceId(request.getHeader(HttpHeaderConstants.TRACE_ID_HEADER));
        Boolean administratorFlag = StpUtil.getSession().get(GlobalConstant.ADMINISTRATOR_FLAG, false);
        unifySession.setAdministratorFlag(administratorFlag);
        UserContextHolder.setSession(unifySession);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextHolder.clearSession();
    }
}
