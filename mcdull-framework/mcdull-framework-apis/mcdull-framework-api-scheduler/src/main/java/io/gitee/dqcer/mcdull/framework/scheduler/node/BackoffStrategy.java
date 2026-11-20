package io.gitee.dqcer.mcdull.framework.scheduler.node;

/**
 * 失败退避策略枚举。
 *
 * @author dqcer
 * @since 1.0.0
 */
public enum BackoffStrategy {

    /**
     * 固定间隔。每次重试间隔相同（基于 fixedDelay）。
     */
    FIXED,

    /**
     * 指数退避。每次重试间隔翻倍（2^n 秒）。
     */
    EXPONENTIAL,

    /**
     * 不重试。
     */
    NONE
}
