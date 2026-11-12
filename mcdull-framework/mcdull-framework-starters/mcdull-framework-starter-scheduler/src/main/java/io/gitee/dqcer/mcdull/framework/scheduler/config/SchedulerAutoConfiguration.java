package io.gitee.dqcer.mcdull.framework.scheduler.config;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.SchedulerClient;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import io.gitee.dqcer.mcdull.framework.scheduler.annotation.McdullScheduledHolder;
import io.gitee.dqcer.mcdull.framework.scheduler.interceptor.McdullExecutionInterceptor;
import io.gitee.dqcer.mcdull.framework.scheduler.interceptor.TaskConverter;
import io.gitee.dqcer.mcdull.framework.scheduler.node.McdullScheduleDefinition;
import io.gitee.dqcer.mcdull.framework.scheduler.node.McdullScheduledTask;
import io.gitee.dqcer.mcdull.framework.scheduler.operation.DefaultTaskQueryService;
import io.gitee.dqcer.mcdull.framework.scheduler.operation.SchedulerAlarmService;
import io.gitee.dqcer.mcdull.framework.scheduler.operation.SchedulerManager;
import io.gitee.dqcer.mcdull.framework.scheduler.operation.TaskHistoryRepository;
import io.gitee.dqcer.mcdull.framework.scheduler.operation.TaskQueryService;
import io.gitee.dqcer.mcdull.framework.scheduler.registry.TaskRegistrarPostProcessor;
import io.gitee.dqcer.mcdull.framework.scheduler.registry.TaskRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.SmartLifecycle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 调度器核心自动配置。
 *
 * <p>启动流程：
 * <ol>
 *     <li>所有 Spring 单例创建完成（此时 {@link TaskRegistry} 已收集完所有任务）</li>
 *     <li>{@link SchedulerBootstrap} 触发 {@link Scheduler} 的创建</li>
 *     <li>自动建表 → 转换任务 → 构建并启动 db-scheduler</li>
 * </ol>
 *
 * @author dqcer
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SchedulerProperties.class)
@ConditionalOnProperty(name = "mcdull.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class SchedulerAutoConfiguration {

    private static final String CREATE_SCHEDULED_TASKS_DDL = """
            CREATE TABLE IF NOT EXISTS scheduled_tasks (
                task_name            VARCHAR(100)  NOT NULL,
                task_instance        VARCHAR(100)  NOT NULL,
                task_data            BLOB,
                execution_time       DATETIME(6)   NULL,
                picked               TINYINT(1)    NOT NULL DEFAULT 0,
                picked_by            VARCHAR(100)  NULL,
                last_success         DATETIME(6)   NULL,
                last_failure         DATETIME(6)   NULL,
                consecutive_failures INT           NOT NULL DEFAULT 0,
                last_heartbeat       DATETIME(6)   NULL,
                version              INT           NOT NULL,
                PRIMARY KEY (task_name, task_instance)
            )
            """;

    private static final String CREATE_TASK_EXECUTION_DDL = """
            CREATE TABLE IF NOT EXISTS scheduler_task_execution (
                id              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
                task_name       VARCHAR(100)  NOT NULL COMMENT '任务名称，对应 scheduled_tasks.task_name',
                task_instance   VARCHAR(100)  NOT NULL COMMENT '任务实例，对应 scheduled_tasks.task_instance',
                scheduled_at    DATETIME(6)   NULL COMMENT '计划执行时间',
                started_at      DATETIME(6)   NOT NULL COMMENT '实际开始时间',
                completed_at    DATETIME(6)   NULL COMMENT '完成时间',
                status          VARCHAR(16)   NOT NULL COMMENT '执行状态：RUNNING/SUCCESS/FAILED/LOST',
                executor_node   VARCHAR(128)  NULL COMMENT '执行节点（主机名）',
                failure_message VARCHAR(2000) NULL COMMENT '失败信息（异常类名 + message）',
                attempt_number  INT           NOT NULL DEFAULT 1 COMMENT '尝试次数',
                trace_id        VARCHAR(128)  NULL COMMENT '链路追踪ID',
                created_time    DATETIME(6)   NOT NULL COMMENT '创建时间',
                PRIMARY KEY (id),
                KEY idx_task_started (task_name, started_at),
                KEY idx_status_started (status, started_at),
                KEY idx_started (started_at)
            ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '定时任务执行历史'
            """;

    // ==================== 基础设施 Bean ====================

    @Bean
    @ConditionalOnMissingBean
    public TaskRegistry taskRegistry() {
        return new TaskRegistry();
    }

    /**
     * BeanPostProcessor：收集实现了 {@link McdullScheduledTask} 的 Bean 和 {@code @McdullScheduled} 注解方法。
     * <p>声明为 static 以便尽早注册，避免过早实例化配置类。
     */
    @Bean
    @ConditionalOnMissingBean
    public static TaskRegistrarPostProcessor taskRegistrarPostProcessor(TaskRegistry taskRegistry) {
        return new TaskRegistrarPostProcessor(taskRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    public SchedulerAlarmService schedulerAlarmService() {
        return new SchedulerAlarmService();
    }

    @Bean
    @ConditionalOnMissingBean
    public McdullExecutionInterceptor mcdullExecutionInterceptor(SchedulerAlarmService alarmService,
                                                                 ObjectProvider<TaskHistoryRepository> historyProvider) {
        return new McdullExecutionInterceptor(alarmService, historyProvider.getIfAvailable());
    }

    /**
     * 任务执行历史仓储。仅当 {@code mcdull.scheduler.history.enabled=true} 时创建，
     * 同时自动创建 {@code scheduler_task_execution} 表。
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "mcdull.scheduler.history.enabled", havingValue = "true", matchIfMissing = true)
    public TaskHistoryRepository taskHistoryRepository(SchedulerDataSourceProvider dataSourceProvider,
                                                       SchedulerProperties properties) {
        DataSource dataSource = dataSourceProvider.getDataSource();
        createTaskExecutionTable(dataSource);
        log.info("[mcdull-scheduler] 已启用任务执行历史，保留 {} 天",
                properties.getHistory().getRetentionDays());
        return new TaskHistoryRepository(new JdbcTemplate(dataSource),
                properties.getHistory().getRetentionDays());
    }

    // ==================== db-scheduler 核心 Bean ====================

    /**
     * 调度表库数据源提供者。
     * <p>注意：这里刻意不直接注册 {@link DataSource} 类型的 Bean，避免与业务模块的动态路由数据源
     * 产生按类型注入歧义（{@code MysqlAutoConfiguration} 中按类型注入 DataSource）。
     */
    @Bean(destroyMethod = "close")
    public SchedulerDataSourceProvider schedulerDataSourceProvider(SchedulerProperties properties,
                                                                   Environment environment,
                                                                   ApplicationContext applicationContext) {
        return new SchedulerDataSourceProvider(properties, environment, applicationContext);
    }

    /**
     * 调度器。声明为 {@link Lazy}，由 {@link SchedulerBootstrap} 在所有单例就绪后触发创建，
     * 保证任务注册表已收集完整。
     * <p>{@code destroyMethod = ""} 禁止 Spring 推断销毁方法，改由 {@link SchedulerBootstrap}
     * 以 {@link SmartLifecycle} 的方式停止，确保停止发生在数据源销毁之前。
     */
    @Bean(destroyMethod = "")
    @Lazy
    @ConditionalOnMissingBean
    public Scheduler scheduler(SchedulerDataSourceProvider dataSourceProvider,
                               SchedulerProperties properties,
                               TaskRegistry taskRegistry,
                               McdullExecutionInterceptor interceptor) {

        DataSource dataSource = dataSourceProvider.getDataSource();
        createScheduledTasksTable(dataSource);

        List<RecurringTask<?>> dbTasks = convertAllTasks(taskRegistry, interceptor);
        if (dbTasks.isEmpty()) {
            log.warn("[mcdull-scheduler] 没有发现任何定时任务，Scheduler 将以无任务模式启动");
        }

        RecurringTask<?>[] taskArray = dbTasks.toArray(new RecurringTask[0]);
        Scheduler scheduler = Scheduler.create(dataSource)
                .startTasks(taskArray)
                .threads(properties.getThreadCount())
                .pollingInterval(Duration.ofMillis(properties.getPollingIntervalMs()))
                .heartbeatInterval(Duration.ofMillis(properties.getHeartbeatIntervalMs()))
                .build();

        log.info("[mcdull-scheduler] 启动调度器，线程数: {}, 轮询间隔: {}ms, 心跳间隔: {}ms, 注册任务数: {}",
                properties.getThreadCount(),
                properties.getPollingIntervalMs(),
                properties.getHeartbeatIntervalMs(),
                dbTasks.size());
        scheduler.start();

        return scheduler;
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public SchedulerClient schedulerClient(Scheduler scheduler) {
        return scheduler;
    }

    /**
     * 管理门面。使用 {@link ObjectProvider} 延迟获取调度器，避免提前触发 Scheduler 创建。
     */
    @Bean
    @ConditionalOnMissingBean
    public SchedulerManager schedulerManager(ObjectProvider<Scheduler> schedulerProvider,
                                             ObjectProvider<SchedulerClient> schedulerClientProvider) {
        return new SchedulerManager(schedulerProvider, schedulerClientProvider);
    }

    /**
     * 任务查询门面。直接基于调度表数据源构建 JdbcTemplate，不依赖容器中是否存在 JdbcTemplate Bean
     * （多数据源场景下 Spring Boot 不会自动创建 JdbcTemplate）。
     */
    @Bean
    @ConditionalOnMissingBean
    public TaskQueryService taskQueryService(SchedulerDataSourceProvider dataSourceProvider,
                                             ObjectProvider<TaskHistoryRepository> historyProvider) {
        return new DefaultTaskQueryService(new JdbcTemplate(dataSourceProvider.getDataSource()),
                historyProvider.getIfAvailable());
    }

    /**
     * 所有单例初始化完成后触发调度器创建与启动；容器关闭时最先停止调度器。
     */
    @Bean
    public SchedulerBootstrap schedulerBootstrap(ObjectProvider<Scheduler> schedulerProvider) {
        return new SchedulerBootstrap(schedulerProvider);
    }

    /**
     * 调度器启动引导器。
     * <p>用 {@link SmartLifecycle} 而非 {@code SmartInitializingSingleton}：启动时机相同（refresh 末期），
     * 但关闭时会先于所有单例销毁执行 stop。否则调度表库连接池先被关闭，
     * db-scheduler 的轮询线程会持续抛出 {@code HikariDataSource has been closed}。
     */
    @Slf4j
    public static class SchedulerBootstrap implements SmartLifecycle {

        private final ObjectProvider<Scheduler> schedulerProvider;

        private volatile Scheduler scheduler;

        private volatile boolean running;

        public SchedulerBootstrap(ObjectProvider<Scheduler> schedulerProvider) {
            this.schedulerProvider = schedulerProvider;
        }

        @Override
        public void start() {
            if (running) {
                return;
            }
            this.scheduler = schedulerProvider.getObject();
            this.running = true;
        }

        @Override
        public void stop() {
            Scheduler current = this.scheduler;
            this.scheduler = null;
            this.running = false;
            if (current == null) {
                return;
            }
            try {
                current.stop();
                log.info("[mcdull-scheduler] 调度器已停止");
            } catch (Exception e) {
                log.warn("[mcdull-scheduler] 停止调度器失败: {}", e.getMessage());
            }
        }

        @Override
        public boolean isRunning() {
            return running;
        }

        @Override
        public int getPhase() {
            // phase 越大启动越晚、停止越早，保证先于数据源等基础设施停止
            return Integer.MAX_VALUE;
        }
    }

    // ==================== 私有工具方法 ====================

    private void createScheduledTasksTable(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_SCHEDULED_TASKS_DDL);
            log.info("[mcdull-scheduler] 已确认 scheduled_tasks 表存在");
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 自动建表失败（可能表已存在或无权限）: {}", e.getMessage());
        }
    }

    private void createTaskExecutionTable(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TASK_EXECUTION_DDL);
            log.info("[mcdull-scheduler] 已确认 scheduler_task_execution 表存在");
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 自动创建执行历史表失败: {}", e.getMessage());
        }
    }

    private List<RecurringTask<?>> convertAllTasks(TaskRegistry taskRegistry,
                                                   McdullExecutionInterceptor interceptor) {
        List<RecurringTask<?>> dbTasks = new ArrayList<>();

        for (McdullScheduledTask<?> mcdullTask : taskRegistry.getInterfaceTasks()) {
            McdullScheduleDefinition definition = mcdullTask.definition();
            if (!definition.isEnabled()) {
                log.info("[mcdull-scheduler] 任务已禁用，跳过注册: {}", definition.getName());
                continue;
            }
            dbTasks.add(TaskConverter.convertInterfaceTask(mcdullTask, definition, interceptor));
            log.info("[mcdull-scheduler] 注册接口任务: {}", definition.getName());
        }

        for (McdullScheduledHolder holder : taskRegistry.getAnnotationHolders()) {
            if (!holder.getAnnotation().enabled()) {
                log.info("[mcdull-scheduler] 任务已禁用，跳过注册: {}", holder.getName());
                continue;
            }
            dbTasks.add(TaskConverter.convertAnnotationHolder(holder, interceptor));
            log.info("[mcdull-scheduler] 注册注解任务: {}", holder.getName());
        }

        return dbTasks;
    }
}
