package io.gitee.dqcer.mcdull.framework.scheduler.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 任务执行历史 VO。
 * <p>注意：db-scheduler 不维护独立的历史表，而是基于 scheduled_tasks 表中 task_instance 的记录。
 * 一个任务的每次 OneTime 调度会产生一条新记录；RecurringTask 只有一行记录（last_success / last_failure 更新）。
 *
 * @author dqcer
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistoryVO {

    /** 任务名称 */
    private String taskName;

    /** 任务实例 ID */
    private String taskInstance;

    /** 执行时间 */
    private Instant executionTime;

    /** 执行结果：SUCCESS / FAILED / RUNNING */
    private String status;

    /** 成功时间（SUCCESS 时有值） */
    private Instant successTime;

    /** 失败时间（FAILED 时有值） */
    private Instant failureTime;

    /** 失败原因（consecutive_failures > 0 时粗略判断） */
    private Integer consecutiveFailures;
}
