package io.gitee.dqcer.mcdull.framework.mysql.context;

import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import io.gitee.dqcer.mcdull.framework.spi.AsyncContextPropagator;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 数据源路由栈的跨线程传播器。
 * <p>
 * {@link DynamicContextHolder#getAll()} 返回的是所属线程活着的 {@link Deque}，因此捕获与回放都要拷贝：
 * 直接跨线程复用会让提交线程与执行线程并发 push/poll 同一个非线程安全容器，异步任务里的数据源变更
 * 还会倒灌回请求线程。
 *
 * @author dqcer
 * @since 2026/11/19
 */
public class DataSourceContextPropagator implements AsyncContextPropagator {

    @Override
    public Object capture() {
        return new ArrayDeque<>(DynamicContextHolder.getAll());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void apply(Object snapshot) {
        if (snapshot == null) {
            DynamicContextHolder.clear();
            return;
        }
        DynamicContextHolder.setAll(new ArrayDeque<>((Deque<String>) snapshot));
    }
}
