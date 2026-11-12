package io.gitee.dqcer.mcdull.framework.scheduler.operation;

import java.time.Instant;
import java.util.List;

/**
 * 任务查询门面接口。
 * <p>查询 db-scheduler 的 scheduled_tasks 表，给 system 模块做管理页面使用。
 * <p>业务模块注入这个接口即可，不需要知道底层是 JdbcTemplate 还是其他实现。
 *
 * @author dqcer
 * @since 1.0.0
 */
public interface TaskQueryService {

    /**
     * 查询所有已注册的任务及其当前执行状态。
     */
    List<TaskExecutionVO> listTasks();

    /**
     * 查询指定任务的执行历史。
     * <p>注意：db-scheduler 的 RecurringTask 在 scheduled_tasks 表中只有一行记录，
     * 所以对于周期性任务，这个方法返回的是最近的状态快照而非完整历史。
     *
     * @param taskName 任务名称
     * @param limit    最多返回条数
     */
    List<TaskHistoryVO> history(String taskName, int limit);

    /**
     * 查询指定任务的下次预计执行时间。
     *
     * @param taskName 任务名称
     * @return 下次执行时间，任务不存在时返回 null
     */
    Instant nextExecuteTime(String taskName);

    /**
     * 查询任务执行历史（表 {@code scheduler_task_execution}）。
     * <p>默认开启；若显式关闭 {@code mcdull.scheduler.history.enabled}，返回空列表。
     *
     * @param taskName 任务名称，为 null 表示查询全部任务
     * @param status   执行状态（RUNNING / SUCCESS / FAILED），为 null 表示不过滤
     * @param limit    最多返回条数
     */
    List<TaskExecutionRecord> executions(String taskName, String status, int limit);
}
