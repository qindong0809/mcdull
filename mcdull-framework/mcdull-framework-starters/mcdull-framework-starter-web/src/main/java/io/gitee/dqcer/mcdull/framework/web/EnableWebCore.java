package io.gitee.dqcer.mcdull.framework.web;


import io.gitee.dqcer.mcdull.framework.web.advice.BaseExceptionAdvice;
import io.gitee.dqcer.mcdull.framework.web.advice.ExceptionAdvice;
import io.gitee.dqcer.mcdull.framework.web.config.*;
import io.gitee.dqcer.mcdull.framework.web.jmx.DatabaseJmxAdapter;
import io.gitee.dqcer.mcdull.framework.web.jmx.RedisJmxAdapter;
import io.gitee.dqcer.mcdull.framework.web.jmx.ThreadPoolJmxAdapter;
import io.gitee.dqcer.mcdull.framework.web.transform.SpringContextHolder;
import io.gitee.dqcer.mcdull.framework.web.version.VersionInfoImplComponent;
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
//        UndertowConfig.class,
        SpringContextHolder.class,
        I18nConfig.class,
        RedisJmxAdapter.class,
        ThreadPoolJmxAdapter.class,
        DatabaseJmxAdapter.class,
        SystemEnvironmentConfig.class,
        VersionInfoImplComponent.class
})
public @interface EnableWebCore {
}
