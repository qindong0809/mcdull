package com.dqcer.mcdull.framework.web;


import com.dqcer.mcdull.framework.web.advice.ExceptionAdvice;
import com.dqcer.mcdull.framework.web.config.AutoConfiguration;
import com.dqcer.mcdull.framework.web.config.DateTimeConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * web core
 *
 * @author dqcer
 * @date 2021/10/05 00:10:22
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ExceptionAdvice.class, AutoConfiguration.class, DateTimeConfig.class})
public @interface EnableWebCore {
}