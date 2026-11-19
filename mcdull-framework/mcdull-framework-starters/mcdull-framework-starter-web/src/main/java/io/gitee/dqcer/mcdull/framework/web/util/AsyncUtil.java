package io.gitee.dqcer.mcdull.framework.web.util;

import io.gitee.dqcer.mcdull.framework.web.context.AsyncContextSupport;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 异步工具类
 *
 * @author dqcer
 * @since 2026-03-18
 */
public class AsyncUtil {

    private static final ExecutorService DEFAULT_EXECUTOR = new ThreadPoolExecutor(2, 4, 60, TimeUnit.MINUTES,
        new ArrayBlockingQueue<>(100), new CustomizableThreadFactory("common-executor"), new ThreadPoolExecutor.CallerRunsPolicy());


    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        Supplier<T> guarded = () -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                LoggerFactory.getLogger(AsyncUtil.class).error("supplyAsync error", e);
            }
            return null;
        };
        // wrap 在提交线程上执行，因此必须在当前线程调用才能捕获到提交线程的上下文
        return CompletableFuture.supplyAsync(AsyncContextSupport.current().wrap(guarded), DEFAULT_EXECUTOR);
    }
}
