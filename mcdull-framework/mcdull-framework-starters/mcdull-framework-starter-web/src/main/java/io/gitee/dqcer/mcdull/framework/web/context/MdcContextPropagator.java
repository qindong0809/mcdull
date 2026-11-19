package io.gitee.dqcer.mcdull.framework.web.context;

import io.gitee.dqcer.mcdull.framework.spi.AsyncContextPropagator;
import org.slf4j.MDC;

import java.util.Map;

/**
 * 日志 MDC（traceId 等）的跨线程传播器。
 *
 * @author dqcer
 * @since 2026/11/19
 */
public class MdcContextPropagator implements AsyncContextPropagator {

    @Override
    public Object capture() {
        return MDC.getCopyOfContextMap();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void apply(Object snapshot) {
        Map<String, String> mdc = (Map<String, String>) snapshot;
        // 快照为空时也要显式清空，否则执行线程会沿用上一个任务残留的 traceId
        if (mdc == null || mdc.isEmpty()) {
            MDC.clear();
        } else {
            MDC.setContextMap(mdc);
        }
    }
}
