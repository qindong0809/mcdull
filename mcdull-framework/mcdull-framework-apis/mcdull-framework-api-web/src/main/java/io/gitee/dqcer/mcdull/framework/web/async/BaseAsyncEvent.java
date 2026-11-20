package io.gitee.dqcer.mcdull.framework.web.async;

import org.springframework.scheduling.annotation.Async;

/**
 * 日志事件监听器
 *
 * @author dqcer
 * @since 2021/08/19
 */
public class BaseAsyncEvent<T> {

    /**
     * 基于异步处理事件, 多个可添加
     *
     * @param t t
     * @see org.springframework.core.annotation.Order
     */
    @Async("threadPoolTaskExecutor")
    public void asyncEvent(T t) {
        execute(t);
    }

    /**
     * 执行
     *
     * @param t t
     */
    protected void execute(T t) {
    }
}
