package io.gitee.dqcer.mcdull.framework.scheduler.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * 定时任务执行记录。
 * <p>对应表 {@code scheduler_task_execution}，一次执行一行。
 *
 * @author dqcer
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecutionRecord {

    /**
     * 主键
     */
    private Long id;

    /**
     * 任务名称，对应 scheduled_tasks.task_name
     */
    private String taskName;

    /**
     * 任务实例，对应 scheduled_tasks.task_instance
     */
    private String taskInstance;

    /**
     * 计划执行时间
     */
    private Instant scheduledAt;

    /**
     * 实际开始时间
     */
    private Instant startedAt;

    /**
     * 完成时间
     */
    private Instant completedAt;

    /**
     * 执行状态：RUNNING / SUCCESS / FAILED
     */
    private String status;

    /**
     * 执行节点（主机名）
     */
    private String executorNode;

    /**
     * 失败信息（异常类名 + message，超长截断）
     */
    private String failureMessage;

    /**
     * 尝试次数（db-scheduler 的 consecutiveFailures + 1）
     */
    private Integer attemptNumber;

    /**
     * 链路追踪 ID，可与业务日志串联
     */
    private String traceId;

    /**
     * 创建时间
     */
    private Instant createdTime;
}
