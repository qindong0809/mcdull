package io.gitee.dqcer.mcdull.framework.web.component.impl;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @author dqcer
 * @since 2023/12/27
 */
@Component
public class DynamicLocaleMessageSourceImpl implements DynamicLocaleMessageSource {

    private static final Logger log = LoggerFactory.getLogger(DynamicLocaleMessageSourceImpl.class);

    @Resource
    private MessageSource messageSource;

    @Override
    public String getMessage(String code, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            log.error(e.getMessage(), e);
        }
        return StrUtil.EMPTY;
    }

    @Override
    public String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(code, null, locale);
        } catch (NoSuchMessageException e) {
            log.error(e.getMessage(), e);
        }
        return StrUtil.EMPTY;
    }
}
