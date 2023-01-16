package io.gitee.dqcer.framework.web;


import io.gitee.dqcer.framework.web.advice.BaseExceptionAdvice;
import io.gitee.dqcer.framework.web.advice.ExceptionAdvice;
import io.gitee.dqcer.framework.web.config.AutoConfiguration;
import io.gitee.dqcer.framework.web.config.DateTimeConfig;
import io.gitee.dqcer.framework.web.config.ThreadPoolConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * web core
 *
 * @author dqcer
 * @version 2021/10/05 00:10:22
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ExceptionAdvice.class, BaseExceptionAdvice.class, AutoConfiguration.class, DateTimeConfig.class, ThreadPoolConfig.class})
public @interface EnableWebCore {
}
