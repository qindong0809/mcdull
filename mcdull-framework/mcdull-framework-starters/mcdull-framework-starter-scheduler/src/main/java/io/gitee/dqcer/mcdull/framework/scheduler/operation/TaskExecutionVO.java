package io.gitee.dqcer.mcdull.framework.scheduler.operation;

/**
 * 任务查询 VO。
 * <p>对应 db-scheduler {@code scheduled_tasks} 表的一行记录。
 *
 * @author dqcer
 * @since 1.0.0
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecutionVO {

    /** 任务名称 */
    private String taskName;

    /** 任务实例 ID（RecurringTask 通常为空，OneTimeTask 有值） */
    private String taskInstance;

    /** 下次（或本次）执行时间 */
    private Instant executionTime;

    /** 是否被某个调度器实例拾取（执行中） */
    private boolean picked;

    /** 被哪个实例拾取 */
    private String pickedBy;

    /** 上次成功时间 */
    private Instant lastSuccess;

    /** 上次失败时间 */
    private Instant lastFailure;

    /** 连续失败次数 */
    private int consecutiveFailures;

    /** 上次心跳时间（执行中持续更新） */
    private Instant lastHeartbeat;
}
