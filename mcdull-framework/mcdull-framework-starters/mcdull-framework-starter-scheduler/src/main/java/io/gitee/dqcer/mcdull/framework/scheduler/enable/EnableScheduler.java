package io.gitee.dqcer.mcdull.framework.scheduler.enable;

import io.gitee.dqcer.mcdull.framework.scheduler.config.SchedulerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一键启用 McDull 调度器。
 * <p>标记在各业务模块的启动类上，例如：
 * <pre>
 *   &#64;SpringBootApplication
 *   &#64;EnableScheduler
 *   public class SystemBootstrap { ... }
 * </pre>
 *
 * @author dqcer
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SchedulerAutoConfiguration.class)
public @interface EnableScheduler {
}
