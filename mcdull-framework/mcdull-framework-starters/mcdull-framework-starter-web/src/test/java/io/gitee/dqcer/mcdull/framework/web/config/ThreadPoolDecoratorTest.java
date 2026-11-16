package io.gitee.dqcer.mcdull.framework.web.config;

import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ThreadPoolConfig.ThreadPoolDecorator} 的上下文传递回归测试。
 * <p>
 * 直接调用 {@code decorate(task).run()} 就是在复现 {@code CallerRunsPolicy} 打满队列后的行为：
 * 任务落到提交线程上同步执行，因此这类用例能守住「执行完不许抹掉提交线程自己的上下文」这条底线。
 */
class ThreadPoolDecoratorTest {

    private static final String TRACE_ID = "traceId";
    private static final long AWAIT_SECONDS = 5L;

    private final ThreadPoolConfig.ThreadPoolDecorator decorator = new ThreadPoolConfig.ThreadPoolDecorator();

    @AfterEach
    void resetContext() {
        MDC.clear();
        UserContextHolder.clearSession();
        DynamicContextHolder.clear();
    }

    @Test
    @DisplayName("CallerRunsPolicy：任务在提交线程上跑完后，提交线程自己的上下文必须原样还在")
    void callerRunsMustNotEraseSubmitterContext() {
        establish(TRACE_ID, "trace-owner", "owner", "ds-owner");

        AtomicReference<String> traceInTask = new AtomicReference<>();
        AtomicReference<String> loginInTask = new AtomicReference<>();
        AtomicReference<String> dataSourceInTask = new AtomicReference<>();

        decorator.decorate(() -> {
            traceInTask.set(MDC.get(TRACE_ID));
            loginInTask.set(UserContextHolder.getSession().getLoginName());
            dataSourceInTask.set(DynamicContextHolder.peek());
        }).run();

        assertEquals("trace-owner", traceInTask.get(), "任务内应看到提交线程的 traceId");
        assertEquals("owner", loginInTask.get(), "任务内应看到提交线程的登录态");
        assertEquals("ds-owner", dataSourceInTask.get(), "任务内应看到提交线程的数据源");

        assertEquals("trace-owner", MDC.get(TRACE_ID), "任务结束后提交线程的 traceId 被清掉了");
        assertNotNull(UserContextHolder.getSession(), "任务结束后提交线程的登录态被清掉了");
        assertEquals("ds-owner", DynamicContextHolder.peek(),
                "任务结束后提交线程的数据源路由被清掉了，后续查询会静默回落默认库");
    }

    @Test
    @DisplayName("CallerRunsPolicy：任务内切换数据源不得倒灌进提交线程的路由栈")
    void taskMustNotMutateSubmitterDataSourceStack() {
        establish(TRACE_ID, "trace-a", "user-a", "ds-a");

        decorator.decorate(() -> DynamicContextHolder.push("ds-b")).run();

        assertEquals("ds-a", DynamicContextHolder.peek(), "提交线程的数据源栈顶被异步任务改掉了");
        assertEquals(1, DynamicContextHolder.getAll().size(), "提交线程的数据源栈长度被异步任务改掉了");
    }

    @Test
    @DisplayName("池线程：任务看到提交线程快照，跑完还原工作线程自己的上下文而不是清空")
    void poolTaskPropagatesSnapshotThenRestoresWorkerOwnContext() throws InterruptedException {
        ExecutorService worker = Executors.newSingleThreadExecutor();
        try {
            // 模拟池线程被复用后残留的"上一次任务"的上下文
            runOn(worker, () -> establish(TRACE_ID, "stale-trace", "stale-login", "ds-stale"));

            establish(TRACE_ID, "trace-current", "current", "ds-current");
            AtomicReference<String> traceInTask = new AtomicReference<>();
            AtomicReference<String> dataSourceInTask = new AtomicReference<>();

            runOn(worker, decorator.decorate(() -> {
                traceInTask.set(MDC.get(TRACE_ID));
                dataSourceInTask.set(DynamicContextHolder.peek());
            }));

            assertEquals("trace-current", traceInTask.get(), "任务内应拿到提交线程的快照");
            assertEquals("ds-current", dataSourceInTask.get(), "任务内应拿到提交线程的数据源");

            AtomicReference<String> traceAfterTask = new AtomicReference<>();
            runOn(worker, () -> traceAfterTask.set(MDC.get(TRACE_ID)));
            assertEquals("stale-trace", traceAfterTask.get(), "任务结束后应还原工作线程自己的上下文");
        } finally {
            worker.shutdownNow();
        }
    }

    @Test
    @DisplayName("提交线程没有 MDC 时，任务不得沿用池线程上一个任务的残留 traceId")
    void emptySubmitterMdcMustNotInheritWorkerLeftovers() throws InterruptedException {
        ExecutorService worker = Executors.newSingleThreadExecutor();
        try {
            runOn(worker, () -> MDC.put(TRACE_ID, "leftover"));

            // 提交线程刻意不带任何 MDC
            AtomicReference<String> traceInTask = new AtomicReference<>("sentinel");
            runOn(worker, decorator.decorate(() -> traceInTask.set(MDC.get(TRACE_ID))));

            assertNull(traceInTask.get(), "任务读到了工作线程残留的 traceId，日志会串到别的请求上");
        } finally {
            worker.shutdownNow();
        }
    }

    private static void establish(String key, String traceId, String loginName, String dataSource) {
        MDC.put(key, traceId);
        UnifySession session = new UnifySession();
        session.setTraceId(traceId);
        session.setLoginName(loginName);
        UserContextHolder.setSession(session);
        DynamicContextHolder.clear();
        DynamicContextHolder.push(dataSource);
    }

    /**
     * 在指定线程上跑一段代码并等它结束，避免测试里出现时序抖动。
     */
    private static void runOn(ExecutorService executor, Runnable task) throws InterruptedException {
        CountDownLatch finished = new CountDownLatch(1);
        executor.execute(() -> {
            try {
                task.run();
            } finally {
                finished.countDown();
            }
        });
        assertTrue(finished.await(AWAIT_SECONDS, TimeUnit.SECONDS), "线程池任务未在超时内完成");
    }
}
