package io.gitee.dqcer.mcdull.framework.scheduler.annotation;

import io.gitee.dqcer.mcdull.framework.scheduler.node.BackoffStrategy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明式定时任务注解。
 *
 * <p>简单场景下一行注解即可，Starter 自动包装成 {@code McdullScheduledTask} 实现并注册到 db-scheduler。
 * 复杂场景建议直接实现 {@code McdullScheduledTask<T>} 接口。
 *
 * <p>cron 表达式支持 SpEL，例如：{@code "${mcdull.scheduler.my-task.cron:0 0 2 * * ?}"}
 *
 * @author dqcer
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface McdullScheduled {

    /**
     * 任务唯一标识。
     * <p>用于管理（暂停/恢复/手动触发）和查询执行历史，在整个应用中必须唯一。
     */
    String name();

    /**
     * Cron 表达式。三者（cron / fixedDelayMs / fixedRateMs）选一。
     * <p>支持 SpEL，例如：{@code "${mcdull.scheduler.my-task.cron:0 0 2 * * ?}"}
     */
    String cron() default "";

    /**
     * 固定间隔（毫秒）。上一次执行完成后等待多久再执行。
     */
    long fixedDelayMs() default 0;

    /**
     * 固定频率（毫秒）。不管上一次是否完成，每隔多久执行一次。
     */
    long fixedRateMs() default 0;

    /**
     * 最大重试次数。0 表示不重试。
     */
    int maxRetries() default 0;

    /**
     * 退避策略。
     */
    BackoffStrategy backoff() default BackoffStrategy.FIXED;

    /**
     * 是否启用。支持通过配置中心动态关闭任务。
     */
    boolean enabled() default true;
}
