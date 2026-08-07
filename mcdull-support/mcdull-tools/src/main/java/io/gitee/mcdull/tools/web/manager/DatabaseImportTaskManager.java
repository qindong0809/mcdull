package io.gitee.mcdull.tools.web.manager;

import io.gitee.mcdull.tools.web.domain.ImportPhaseEnum;
import io.gitee.mcdull.tools.web.domain.ImportProgressVO;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In memory registry of database import tasks plus the worker pool that runs them.
 * <p>
 * State is intentionally kept in memory: this tool is deployed as a single instance and a task is
 * only useful while the browser that started it is still open. Concurrent imports do not need a
 * lock because every import creates its own versioned schema.
 *
 * @author dqcer
 */
@Slf4j
@Component
public class DatabaseImportTaskManager {

    /**
     * How long a finished task stays queryable before it is evicted.
     */
    private static final long TASK_RETENTION_MILLIS = 30 * 60 * 1000L;

    private static final int WORKER_POOL_SIZE = 2;

    private final Map<String, ImportProgressVO> taskMap = new ConcurrentHashMap<>();

    private final ExecutorService workerPool = Executors.newFixedThreadPool(WORKER_POOL_SIZE, namedThreadFactory());

    /**
     * Register a new running task.
     *
     * @param totalBytes size of the dump file
     * @return the registered task, already in the IMPORTING phase
     */
    public ImportProgressVO register(long totalBytes) {
        this.evictExpired();
        ImportProgressVO task = new ImportProgressVO();
        task.setTaskId(UUID.randomUUID().toString().replace("-", ""));
        task.setPhase(ImportPhaseEnum.IMPORTING);
        task.setTotalBytes(totalBytes);
        task.setStartMillis(System.currentTimeMillis());
        taskMap.put(task.getTaskId(), task);
        return task;
    }

    /**
     * Look up a task by id.
     *
     * @param taskId task id
     * @return the task, or null when unknown or already evicted
     */
    public ImportProgressVO get(String taskId) {
        return taskMap.get(taskId);
    }

    /**
     * Mark a task as finished.
     *
     * @param task    the task to complete
     * @param phase   terminal phase
     * @param message masking log on success, failure reason otherwise
     */
    public void complete(ImportProgressVO task, ImportPhaseEnum phase, String message) {
        task.setPhase(phase);
        task.setMessage(message);
        task.setEndMillis(System.currentTimeMillis());
    }

    /**
     * Submit the task body to the worker pool.
     *
     * @param body work to run off the request thread
     */
    public void submit(Runnable body) {
        workerPool.submit(body);
    }

    @PreDestroy
    public void shutdown() {
        workerPool.shutdownNow();
    }

    private void evictExpired() {
        long deadline = System.currentTimeMillis() - TASK_RETENTION_MILLIS;
        taskMap.entrySet().removeIf(entry -> {
            ImportProgressVO task = entry.getValue();
            return task.getPhase().isTerminal() && task.getEndMillis() > 0 && task.getEndMillis() < deadline;
        });
    }

    private static ThreadFactory namedThreadFactory() {
        AtomicInteger counter = new AtomicInteger();
        return runnable -> {
            Thread thread = new Thread(runnable, "db-import-worker-" + counter.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
    }
}
