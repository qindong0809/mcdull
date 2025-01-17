package io.gitee.dqcer.mcdull.framework.web.component.impl;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import jakarta.annotation.Resource;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * Dynamic Locale Message Source
 *
 * @author dqcer
 * @since 2023/12/27
 */
public class DynamicLocaleMessageSourceImpl implements DynamicLocaleMessageSource {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private MessageSource messageSource;

    @Override
    public String getMessage(String code, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            LogHelp.warn(log, e.getMessage());
        }
        if (args == null) {
            return code;
        }
        return MessageFormat.format(code, args);
    }

    @Override
    public String getMessage(String code) {
        return this.getMessage(code, null);
    }
}
