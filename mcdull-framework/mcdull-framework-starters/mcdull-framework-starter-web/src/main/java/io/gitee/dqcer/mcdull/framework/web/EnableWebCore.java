package io.gitee.dqcer.mcdull.framework.web;


import io.gitee.dqcer.mcdull.framework.web.advice.BaseExceptionAdvice;
import io.gitee.dqcer.mcdull.framework.web.advice.ExceptionAdvice;
import io.gitee.dqcer.mcdull.framework.web.config.AutoConfiguration;
import io.gitee.dqcer.mcdull.framework.web.config.BannerConfig;
import io.gitee.dqcer.mcdull.framework.web.config.DateTimeConfig;
import io.gitee.dqcer.mcdull.framework.web.config.MultipartFileConfig;
import io.gitee.dqcer.mcdull.framework.web.config.ThreadPoolConfig;
import io.gitee.dqcer.mcdull.framework.web.config.UndertowConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * web core
 *
 * @author dqcer
 * @since 2021/10/05 00:10:22
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({
        BannerConfig.class,
        ExceptionAdvice.class,
        BaseExceptionAdvice.class,
        AutoConfiguration.class,
        DateTimeConfig.class,
        MultipartFileConfig.class,
        ThreadPoolConfig.class,
        UndertowConfig.class
})
public @interface EnableWebCore {
}
