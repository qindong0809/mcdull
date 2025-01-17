package io.gitee.dqcer.mcdull.framework.web.advice;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;

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
        StringWriter stringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(stringWriter);
        exception.printStackTrace(pw);
        String errorStack = stringWriter.toString();
        if (StrUtil.isNotBlank(errorStack)) {
            if (errorStack.length() > 1000) {
                return errorStack.substring(0, 1000);
            }
        }
        return StrUtil.EMPTY;
    }
}
