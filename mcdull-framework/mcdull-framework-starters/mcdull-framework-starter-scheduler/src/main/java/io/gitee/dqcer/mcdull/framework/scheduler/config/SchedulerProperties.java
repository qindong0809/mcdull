package io.gitee.dqcer.mcdull.framework.scheduler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 调度器配置属性。
 * <p>绑定前缀：{@code mcdull.scheduler}
 *
 * @author dqcer
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "mcdull.scheduler")
public class SchedulerProperties {

    /**
     * 是否启用调度器。
     */
    private boolean enabled = true;

    /**
     * 统一调度表库 JDBC URL。
     * <p>不填则 fallback 到 {@code @Primary DataSource}。
     */
    private String dataSourceUrl;

    /**
     * 统一调度表库用户名。
     */
    private String dataSourceUsername;

    /**
     * 统一调度表库密码。
     */
    private String dataSourcePassword;

    /**
     * 统一调度表库驱动类。默认 MySQL。
     */
    private String dataSourceDriverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 调度线程数。
     */
    private int threadCount = 10;

    /**
     * 轮询间隔（毫秒）。调度器多久扫一次 scheduled_tasks 表找可执行任务。
     */
    private long pollingIntervalMs = 10_000L;

    /**
     * 心跳间隔（毫秒）。任务执行中多久更新一次心跳，防止被其他实例重复接管。
     */
    private long heartbeatIntervalMs = 300_000L;

    /**
     * 任务执行超时（毫秒）。超过此时间任务会被标记为失败。
     */
    private long taskExecutionTimeoutMs = 60_000L;

    /**
     * 执行历史配置。
     */
    private History history = new History();

    /**
     * 执行历史配置项。
     * <p>默认开启：每次执行会多一次 insert + 一次 update，如对写放大敏感可显式关闭。
     */
    @Getter
    @Setter
    public static class History {

        /**
         * 是否记录任务执行历史（表 scheduler_task_execution），默认开启。
         */
        private boolean enabled = true;

        /**
         * 执行历史保留天数，小于等于 0 表示不自动清理。
         */
        private int retentionDays = 30;
    }
}
