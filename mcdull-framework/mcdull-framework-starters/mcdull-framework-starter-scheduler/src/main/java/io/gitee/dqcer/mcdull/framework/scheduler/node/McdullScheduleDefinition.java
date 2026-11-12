package io.gitee.dqcer.mcdull.framework.scheduler.node;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 定时任务调度定义。
 * <p>从执行逻辑中解耦调度参数，支持从 Nacos 动态读取配置。
 *
 * @author dqcer
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
public class McdullScheduleDefinition {

    /**
     * 任务唯一标识。在整个应用中必须唯一。
     */
    private final String name;

    /**
     * Cron 表达式。三者（cron / fixedDelayMs / fixedRateMs）选一。
     */
    private final String cron;

    /**
     * 固定间隔（毫秒）。上一次执行完成后等待多久再执行。
     */
    private final Long fixedDelayMs;

    /**
     * 固定频率（毫秒）。不管上一次是否完成，每隔多久执行一次。
     */
    private final Long fixedRateMs;

    /**
     * 最大重试次数。0 表示不重试。
     */
    private final int maxRetries;

    /**
     * 退避策略。
     */
    private final BackoffStrategy backoff;

    /**
     * 是否为有状态任务。
     * <p>有状态任务的 data 会被持久化，下次执行时通过 {@code TaskInstance.getData()} 拿到。
     */
    private final boolean stateful;

    /**
     * 是否启用。false 时该任务不会被调度。
     */
    private final boolean enabled;

    // ========== Builder 默认值 ==========

    public static McdullScheduleDefinitionBuilder builder() {
        return new McdullScheduleDefinitionBuilder()
                .cron("")
                .fixedDelayMs(0L)
                .fixedRateMs(0L)
                .maxRetries(0)
                .backoff(BackoffStrategy.FIXED)
                .stateful(false)
                .enabled(true);
    }
}
