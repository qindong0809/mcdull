package io.gitee.dqcer.mcdull.framework.web.advice;

import io.gitee.dqcer.mcdull.framework.base.help.ExceptionHelp;
import io.gitee.dqcer.mcdull.framework.spi.DynamicLocaleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Resource;

/**
 * abstract exception advice
 *
 * @author dqcer
 * @since 2024/8/19 9:07
 */

public abstract class AbstractExceptionAdvice {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected DynamicLocaleMessageSource dynamicLocaleMessageSource;

    protected String buildExceptionStr(Exception exception) {
        return ExceptionHelp.buildStackStr(exception);
    }
}
