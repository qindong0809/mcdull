package io.gitee.dqcer.mcdull.framework.web.util;

import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 异步工具类
 * @author dqcer
 * @since 2026/03/18
 */
public class AsyncUtil {

    private static final ExecutorService DEFAULT_EXECUTOR = new ThreadPoolExecutor(2, 4, 60, TimeUnit.MINUTES,
        new ArrayBlockingQueue<>(100), new CustomizableThreadFactory("common-executor"), new ThreadPoolExecutor.CallerRunsPolicy());


    public static  <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        UnifySession session = UserContextHolder.getSession();
        Map<String,String> previous = MDC.getCopyOfContextMap();
        Deque<String> deque = DynamicContextHolder.getAll();
        return CompletableFuture.supplyAsync(() -> {
            try {
                UserContextHolder.setSession(session);
                if (previous != null && !previous.isEmpty()) {
                    MDC.setContextMap(previous);
                }
                DynamicContextHolder.setAll(deque);
                return supplier.get();
            } catch (Exception e) {
                LoggerFactory.getLogger(AsyncUtil.class).error("supplyAsync error", e);
            }finally {
                DynamicContextHolder.clear();
                UserContextHolder.clearSession();
                MDC.clear();
            }
            return null;
        }, DEFAULT_EXECUTOR);
    }
}
