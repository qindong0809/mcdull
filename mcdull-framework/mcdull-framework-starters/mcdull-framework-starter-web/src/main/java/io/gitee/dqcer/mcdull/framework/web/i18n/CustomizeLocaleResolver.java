package io.gitee.dqcer.mcdull.framework.web.i18n;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;

import java.util.Locale;
import java.util.TimeZone;

public class CustomizeLocaleResolver extends AbstractLocaleContextResolver {

    @Override
    public LocaleContext resolveLocaleContext(HttpServletRequest request) {
        return new TimeZoneAwareLocaleContext() {
            @Override
            public Locale getLocale() {
                // 优先自定义：zh_CN
                String localeStr = request.getHeader(GlobalConstant.CUSTOMIZE_LANGUAGE_HEAD_NAME);
                if (localeStr != null) {
                    return StringUtils.parseLocale(localeStr);
                }
                UnifySession session = UserContextHolder.getSession();
                if (session != null) {
                    String language = session.getLanguage();
                    if (language != null) {
                        return session.getLocale();
                    }
                }
                Locale locale = request.getLocale();
                if (locale != null) {
                    return locale;
                }
                return CustomizeLocaleResolver.super.getDefaultLocale();
            }

            @Override
            public TimeZone getTimeZone() {
                UnifySession session = UserContextHolder.getSession();
                if (session != null) {
                    return session.getTimeZone();
                }
                return CustomizeLocaleResolver.super.getDefaultTimeZone();
            }
        };
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {

    }
}
