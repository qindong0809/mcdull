package io.gitee.dqcer.mcdull.framework.web.component;

import java.util.function.Supplier;

/**
 * concurrent rate limiter
 *
 * @author dqcer
 * @since 2024/8/14 13:14
 */

public interface ConcurrentRateLimiter {

    /**
     * 限流
     *
     * @param key      key
     * @param quantity 数量
     * @param time     时间
     * @return boolean true:通过 false:限流
     */
    boolean limiter(String key, int quantity, int time);

    /**
     * 锁
     *
     * @param key      key
     * @param timeout  超时时间
     * @return boolean true:通过 false:限流
     */
    <T> T locker(String key, long timeout, Supplier<T> function);

    /**
     * 锁
     *
     * @param key            钥匙
     * @param timeout        超时
     * @param function       功能
     * @param throwException 抛出异常
     */
    <T> void locker(String key, long timeout, Supplier<T> function, boolean throwException);
}
