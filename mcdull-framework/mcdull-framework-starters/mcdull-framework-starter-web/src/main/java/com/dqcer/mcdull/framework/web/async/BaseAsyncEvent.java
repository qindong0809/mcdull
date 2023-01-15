package com.dqcer.mcdull.framework.web.async;

import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.framework.base.exception.BusinessException;
import com.dqcer.framework.base.storage.UnifySession;
import com.dqcer.framework.base.storage.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;

/**
 * 日志事件监听器
 *
 * @author dqcer
 * @date 2021/08/19
 */
public class BaseAsyncEvent<T> {

    private static final Logger log = LoggerFactory.getLogger(BaseAsyncEvent.class);


    /**
     * 基于异步处理事件, 多个可添加
     * @see org.springframework.core.annotation.Order
     *
     * @param t t
     */
    @Async("threadPoolTaskExecutor")
    public void asyncEvent(T t) {
        UnifySession session = UserContextHolder.getSession();
        if (session == null) {
            throw new BusinessException("UnifySession is null");
        }
        MDC.put(HttpHeaderConstants.LOG_TRACE_ID, session.getTraceId());
        if (log.isDebugEnabled()) {
            log.debug("Async event: {}", t);
        }
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