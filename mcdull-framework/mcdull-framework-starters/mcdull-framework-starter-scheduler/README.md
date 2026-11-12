# mcdull-framework-starter-scheduler

mcdull-framework-starter-scheduler 是 McDull 框架的定时任务模块，基于 [db-scheduler](https://github.com/kagkarlsson/db-scheduler) 实现，提供了声明式注解、接口实现、运行时管理等多层 API，支持集群安全、失败重试、状态持久化等企业级特性。

## 项目简介

mcdull-framework-starter-scheduler 为 McDull 框架提供统一的定时任务调度能力。它封装了 db-scheduler 的核心调度引擎，采用"三层暴露"设计（注解 → 接口 → 底层），让业务方在简单场景下一行注解搞定，在复杂场景下也能拿到完整的 ExecutionContext 做状态管理和链式调度。

与 Spring 原生 `@Scheduled` 相比，该模块提供：
- ✅ 集群安全：多实例部署时保证单个实例执行（数据库行锁）
- ✅ 单表持久化：只需要一张 `scheduled_tasks` 表，重启不丢
- ✅ 失败重试：内置固定间隔 / 指数退避策略
- ✅ 状态持久化：任务执行后可更新持久化状态（StatefulTask）
- ✅ 运行时管理：支持暂停 / 恢复 / 手动触发 / 查询执行历史
- ✅ 动态注册：支持运行时动态调度新任务

## 架构决策

以下是该模块设计过程中的关键决策，所有决策均基于 McDull 框架现有的架构约束。

### 1. 为什么选择 db-scheduler

| 维度 | db-scheduler | Quartz | XXL-Job |
|---|---|---|---|
| 复杂度 | 极简 | 重量级 | 需要独立部署 |
| 持久化表数 | **1 张** | 11 张 | 多表 |
| 需要独立进程 | ❌ embed | ❌ embed | ✅ Admin + Executor |
| 依赖 | 仅 slf4j | 较多 | 较重 |
| 集群安全 | ✅ 数据库行锁 | ✅ 数据库行锁 | ✅ 中心化协调 |

选择 db-scheduler 的核心理由：**最小依赖、最轻嵌入、和 McDull "轻量自研"的设计哲学完全吻合**。

### 2. 持久化策略：统一调度表库

db-scheduler 要求所有调度器实例读写同一张 `scheduled_tasks` 表才能保证集群锁正确。因此：
- `scheduled_tasks` 表建在 system 模块的业务库（`mcdull-cloud-3`）
- 所有模块的 Scheduler 都连这个库，不管各模块自己的业务库是什么
- Starter 自动创建指向统一库的 DataSource Bean（可通过 `mcdull.scheduler.data-source-url` 覆盖）

### 3. API 形态：三层暴露

```
第一层：@McdullScheduled 注解（覆盖 80% 场景）
  → 简单任务，一行注解搞定，和 @RedisLock 风格一致

第二层：McdullScheduledTask<T> 接口（覆盖 15% 场景）
  → 需要 ExecutionContext / StatefulTask / 链式调度
  → 接口默认方法设计，和 ProcessHandler 风格一致

第三层：SchedulerClient 直接暴露（覆盖 5% 场景）
  → 运行时动态 schedule / unschedule / 查询执行历史
  → 不做二次封装，db-scheduler 原生 API 本身就很简洁
```

### 4. 模块边界

| 职责 | 放在哪里 |
|---|---|
| 注解定义、接口定义、自动配置 | ✅ Starter |
| TaskRegistry 扫描收集 | ✅ Starter |
| Scheduler 创建与启动 | ✅ Starter |
| scheduled_tasks 表 DDL 执行 | ✅ Starter |
| JdbcTemplate 查询 scheduled_tasks | ✅ Starter（默认实现） |
| SchedulerManager / TaskQueryService Bean | ✅ Starter |
| 任务管理 Controller + UI | ❌ system 模块（后续） |
| 业务表 Mapper | ❌ 业务模块 |
| ArchUnit 架构约束 | ❌ mcdull-framework-enforcer |

### 5. 旧任务兼容

**不兼容。** 旧的 Hutool CronUtil 任务由业务方自选迁移。Starter 只提供新能力，不背上 Hutool 依赖。

## 功能特性

- 声明式注解：`@McdullScheduled` 一行注解定义任务
- 接口实现：`McdullScheduledTask<T>` 完整覆盖复杂场景
- 自动扫描：启动时自动收集注解方法和接口 Bean
- 集群安全：基于数据库行锁保证多实例下单次执行
- 失败重试：支持 FIXED（固定间隔）/ EXPONENTIAL（指数退避）/ NONE
- 状态持久化：StatefulTask 每次执行后可更新持久化状态
- 运行时管理：SchedulerManager 提供 pause / resume / trigger 手动触发
- 执行历史：scheduler_task_execution 流水（默认开启），记录每次执行的耗时、状态、traceId
- 自动建表：启动时自动执行 scheduled_tasks / scheduler_task_execution DDL
- 拦截器扩展：ExecutionInterceptor 支持 TraceId 关联、耗时统计、失败告警
- 配置动态化：cron 表达式支持 SpEL 和 Nacos 配置中心

## 核心依赖

| 依赖 | 版本 | 说明 |
|---|---|---|
| db-scheduler-spring-boot-starter | 17.0.0 | db-scheduler Spring Boot 启动器 |
| db-scheduler | 17.0.0 | db-scheduler 核心库 |
| spring-context | provided | Spring 上下文 |
| spring-boot-autoconfigure | provided | Spring Boot 自动配置 |
| spring-jdbc | provided | JdbcTemplate（用于查询 scheduled_tasks） |
| javax.sql | provided | DataSource |
| mcdull-framework-base | provided | McDull 框架基础模块 |

## 项目结构

```
mcdull-framework-starter-scheduler/
├── pom.xml
├── README.md
└── src/main/java/io/gitee/dqcer/mcdull/framework/scheduler/
    ├── annotation/
    │   ├── McdullScheduled.java              # 声明式注解
    │   └── McdullScheduledHolder.java        # 注解元数据持有
    ├── aspect/
    │   └── ScheduledAnnotationAspect.java    # 注解扫描 AOP
    ├── node/
    │   ├── McdullScheduledTask.java          # 任务接口（对应 ProcessHandler）
    │   ├── McdullScheduleDefinition.java     # 调度定义 POJO
    │   └── BackoffStrategy.java              # 退避策略枚举
    ├── config/
    │   ├── SchedulerAutoConfiguration.java   # 核心自动配置
    │   ├── SchedulerProperties.java          # 配置属性类
    │   └── SchedulerDataSourceProvider.java  # 调度表库数据源解析（不暴露 DataSource Bean）
    ├── registry/
    │   ├── TaskRegistry.java                 # 任务注册表
    │   └── TaskRegistrarPostProcessor.java   # BeanPostProcessor 收集任务
    ├── operation/
    │   ├── SchedulerManager.java              # 管理门面（pause/resume/trigger）
    │   ├── TaskQueryService.java             # 查询门面（接口定义）
    │   ├── DefaultTaskQueryService.java      # 默认 JdbcTemplate 实现
    │   ├── TaskExecutionRecord.java          # 执行记录 POJO
    │   ├── TaskHistoryRepository.java        # 执行历史写入/查询/清理
    │   └── SchedulerAlarmService.java        # 告警门面
    ├── interceptor/
    │   ├── McdullExecutionInterceptor.java   # 通用拦截器（TraceId、耗时）
    │   └── AlarmExecutionInterceptor.java    # 告警拦截器
    └── enable/
        └── EnableScheduler.java              # 一键启用注解
```

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-scheduler</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 启用调度器

在每个业务模块的启动类上标记 `@EnableScheduler`：

```java
@SpringBootApplication
@EnableScheduler
public class SystemBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SystemBootstrap.class, args);
    }
}
```

### 配置示例

```yaml
mcdull:
  scheduler:
    enabled: true
    # 统一调度表库的 DataSource（不填则 fallback 到 @Primary DataSource）
    data-source-url: jdbc:mysql://mcdull.io:3306/mcdull-cloud-3
    data-source-username: root
    data-source-password: ${DB_PASSWORD}
    # 调度线程数
    thread-count: 10
    # 轮询间隔（毫秒）—— 多久扫一次 scheduled_tasks 表
    polling-interval-ms: 10000
    # 心跳间隔（毫秒）—— 任务执行中多久更新一次心跳
    heartbeat-interval-ms: 300000
    # 执行历史（默认开启，每次执行会多一次 insert + 一次 update，可显式关闭）
    history:
      enabled: true
      # 保留天数，<=0 表示不自动清理
      retention-days: 30
```

## API 使用指南

### 第一层：声明式注解（80% 场景）

适合简单的定时任务，不需要 ExecutionContext、不需要状态持久化：

```java
@Component
public class TaskExecutor {

    /**
     * 凌晨 2 点执行数据库备份
     * cron 表达式支持 SpEL 和 Nacos 配置中心动态刷新
     */
    @McdullScheduled(
        name = "db-backup",
        cron = "${mcdull.scheduler.db-backup.cron:0 0 2 * * ?}",
        maxRetries = 2,
        backoff = BackoffStrategy.EXPONENTIAL
    )
    public void backup() {
        // 直接写业务逻辑
        // Starter 自动包装成 McdullScheduledTask 实现
        databaseBackupService.start();
    }

    /**
     * 每 5 分钟执行一次心跳检查
     * fixedDelayMs = 上一次执行完成后等待多久再执行
     */
    @McdullScheduled(name = "heartbeat", fixedDelayMs = 300_000)
    public void heartbeat() {
        healthCheckService.ping();
    }

    /**
     * 固定频率执行
     * fixedRateMs = 不管上一次执行是否完成，每隔多久执行一次
     */
    @McdullScheduled(name = "metrics-collect", fixedRateMs = 60_000)
    public void collectMetrics() {
        metricsService.collect();
    }
}
```

`@McdullScheduled` 参数说明：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|---|---|---|---|---|
| `name` | String | ✅ | - | 任务唯一标识，用于管理和查询 |
| `cron` | String | 三者选一 | "" | Cron 表达式，支持 SpEL |
| `fixedDelayMs` | long | 三者选一 | 0 | 固定间隔（毫秒） |
| `fixedRateMs` | long | 三者选一 | 0 | 固定频率（毫秒） |
| `maxRetries` | int | ❌ | 0 | 最大重试次数 |
| `backoff` | BackoffStrategy | ❌ | FIXED | 退避策略：FIXED / EXPONENTIAL / NONE |
| `enabled` | boolean | ❌ | true | 是否启用（支持配置中心动态关闭） |

> Cron 格式：底层是 cron-utils 的 Quartz 定义，**6 段**（`秒 分 时 日 月 周`，如 `0 0 2 * * ?`）。
> 为兼容 Linux crontab 和历史存量配置，**5 段**（`分 时 日 月 周`，如 `0 2 * * *`）会被自动补秒位为 `0 0 2 * * *`。

### 第二层：接口实现（15% 场景）

需要 ExecutionContext、StatefulTask、链式调度、或者想从 Nacos 动态读 cron 的场景：

```java
@Component
public class DataSyncTask implements McdullScheduledTask<DataSyncState> {

    private final NacosConfigService nacosConfigService;

    public DataSyncTask(NacosConfigService nacosConfigService) {
        this.nacosConfigService = nacosConfigService;
    }

    @Override
    public McdullScheduleDefinition definition() {
        // 从 Nacos 动态读取 cron 表达式，支持运行时刷新
        String cron = nacosConfigService.getProperty("data-sync.cron", "0 0 1 * * ?");

        return McdullScheduleDefinition.builder()
            .name("data-sync")
            .cron(cron)
            .maxRetries(5)
            .backoff(BackoffStrategy.EXPONENTIAL)
            .stateful(true)                    // 有状态任务
            .build();
    }

    @Override
    public void execute(TaskInstance<DataSyncState> instance, ExecutionContext context) {
        // 拿到上一次执行遗留的持久化状态
        DataSyncState lastState = instance.getData();
        long lastSyncId = lastState != null ? lastState.getLastSyncId() : 0L;

        // 执行同步逻辑
        DataSyncState newState = dataSyncService.syncFrom(lastSyncId);

        // 链式调度下一个任务（可选）
        context.getSchedulerClient().schedule(
            TaskInstance.oneshot("data-sync-followup")
        );

        // 新的状态会自动持久化到 scheduled_tasks 表
        context.persist(newState);
    }

    /**
     * 是否为有状态任务
     * 有状态任务的 data 会被持久化，下次执行时通过 instance.getData() 拿到
     */
    @Override
    public boolean isStateful() {
        return true;
    }

    /**
     * 失败回调（可选 override）
     * 默认实现打日志，业务方可以 override 接钉钉/企微告警
     */
    @Override
    public void onFailure(Exception e, TaskInstance<DataSyncState> instance, ExecutionContext context) {
        log.error("data-sync task failed, instance: {}", instance.getId(), e);
        schedulerAlarmService.onFailure("data-sync", e);
    }
}
```

`McdullScheduledTask` 方法说明：

| 方法 | 是否必填 | 说明 |
|---|---|---|
| `definition()` | ✅ | 返回调度定义（name、cron、retries 等） |
| `execute(instance, context)` | ✅ | 任务执行逻辑 |
| `isStateful()` | ❌ | 默认 false。true 时 data 会被持久化 |
| `dataType()` | ❌ | 默认 Void.class。StatefulTask 时指定状态类型 |
| `onFailure(e, instance, context)` | ❌ | 默认打日志。失败时回调 |
| `onSuccess(instance, context)` | ❌ | 默认空实现。成功时回调 |

### 第三层：SchedulerClient 直接使用（5% 场景）

运行时动态注册/取消任务、查询执行历史等场景：

```java
@Service
public class TaskManagementService {

    private final SchedulerClient schedulerClient;   // db-scheduler 原生 Bean
    private final SchedulerManager schedulerManager; // 我们封装的管理门面
    private final TaskQueryService taskQueryService; // 我们封装的查询门面

    public TaskManagementService(
            SchedulerClient schedulerClient,
            SchedulerManager schedulerManager,
            TaskQueryService taskQueryService) {
        this.schedulerClient = schedulerClient;
        this.schedulerManager = schedulerManager;
        this.taskQueryService = taskQueryService;
    }

    // ===== 运行时动态调度 =====

    /**
     * 运行时动态注册一个一次性任务
     */
    public void scheduleOneShot(Runnable task, Instant executeAt) {
        // 方式 1：用 db-scheduler 原生 API
        schedulerClient.schedule(
            TaskInstance.oneshot("dynamic-task")
                .executeAt(executeAt)
        );

        // 方式 2：或者通过我们封装的接口（如果有）
    }

    /**
     * 取消一个任务
     */
    public void cancelTask(String taskName) {
        schedulerManager.cancel(taskName);
    }

    /**
     * 暂停 / 恢复任务
     */
    public void pauseTask(String taskName) {
        schedulerManager.pauseTask(taskName);
    }

    public void resumeTask(String taskName) {
        schedulerManager.resumeTask(taskName);
    }

    /**
     * 手动触发一次（不管 cron 表达式）
     */
    public void triggerNow(String taskName) {
        schedulerManager.trigger(taskName);
    }

    // ===== 查询 =====

    /**
     * 查询所有已注册的任务及当前状态
     */
    public List<TaskExecutionVO> listAllTasks() {
        return taskQueryService.listTasks();
    }

    /**
     * 查询某个任务最近的执行历史
     */
    public List<TaskHistoryVO> getHistory(String taskName, int limit) {
        return taskQueryService.history(taskName, limit);
    }

    /**
     * 查询某个任务下次预计执行时间
     */
    public Instant getNextExecuteTime(String taskName) {
        return taskQueryService.nextExecuteTime(taskName);
    }
}
```

### 在 Controller 中使用（system 模块任务管理页面）

```java
@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    private final SchedulerManager schedulerManager;
    private final TaskQueryService taskQueryService;

    public SchedulerController(
            SchedulerManager schedulerManager,
            TaskQueryService taskQueryService) {
        this.schedulerManager = schedulerManager;
        this.taskQueryService = taskQueryService;
    }

    @GetMapping("/tasks")
    public Result<List<TaskExecutionVO>> listTasks() {
        return Result.success(taskQueryService.listTasks());
    }

    @GetMapping("/tasks/{taskName}/history")
    public Result<List<TaskHistoryVO>> history(
            @PathVariable String taskName,
            @RequestParam(defaultValue = "20") int limit) {
        return Result.success(taskQueryService.history(taskName, limit));
    }

    @PostMapping("/tasks/{taskName}/trigger")
    public Result<Boolean> trigger(@PathVariable String taskName) {
        schedulerManager.trigger(taskName);
        return Result.success(true);
    }

    @PostMapping("/tasks/{taskName}/pause")
    public Result<Boolean> pause(@PathVariable String taskName) {
        schedulerManager.pauseTask(taskName);
        return Result.success(true);
    }

    @PostMapping("/tasks/{taskName}/resume")
    public Result<Boolean> resume(@PathVariable String taskName) {
        schedulerManager.resumeTask(taskName);
        return Result.success(true);
    }

    @PostMapping("/tasks/{taskName}/cancel")
    public Result<Boolean> cancel(@PathVariable String taskName) {
        schedulerManager.cancel(taskName);
        return Result.success(true);
    }
}
```

## 核心功能详解

### 1. 自动扫描与注册

Starter 在所有 Spring 单例初始化完成后（`SmartInitializingSingleton`）统一收集：
- 所有标注 `@McdullScheduled` 注解的方法（反射调用）
- 所有实现 `McdullScheduledTask` 接口的 Bean

收集完成后一次性构建 db-scheduler 的 Scheduler 并启动，因此 `definition()` 中可以安全地访问数据库读取配置。

### 2. 集群安全

db-scheduler 通过 `SELECT ... FOR UPDATE` 行锁保证集群安全：
- 调度器实例定期扫描 `scheduled_tasks` 表
- 抢到行锁的实例执行任务
- 执行期间持续更新心跳（`heartbeat-interval-ms`）
- 心跳超时（执行实例挂了）的任务会被其他实例接管执行

因此 **Starter 不需要分布式锁注解**，db-scheduler 内部已经保证了。

### 3. 失败重试

```java
@McdullScheduled(name = "sync", cron = "0 0 * * * ?",
    maxRetries = 3,
    backoff = BackoffStrategy.EXPONENTIAL)
```

| 策略 | 说明 |
|---|---|
| FIXED | 固定间隔重试（基于 fixedDelay） |
| EXPONENTIAL | 指数退避（2^n 秒后重试） |
| NONE | 不重试 |

### 4. 状态持久化（StatefulTask）

```java
@Override
public boolean isStateful() { return true; }

@Override
public void execute(TaskInstance<DataSyncState> instance, ExecutionContext context) {
    DataSyncState lastState = instance.getData();  // 拿到上次执行遗留的状态
    // ... 执行逻辑 ...
    context.persist(newState);  // 持久化新状态，下次执行时可用
}
```

### 5. 拦截器扩展

`McdullExecutionInterceptor` 统一处理：
- TraceId 注入（从 MDC 取，任务执行完成后清理）
- 耗时统计
- UserContextHolder 关联（如果任务需要用户上下文）

`AlarmExecutionInterceptor` 处理：
- 任务失败时自动调用 `SchedulerAlarmService.onFailure()`
- 默认实现打日志，业务方可以 override 接钉钉/企微告警

### 6. 自动建表

Starter 启动时自动执行 db-scheduler 官方 DDL：

```sql
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
);
```

默认开启，自动创建执行历史表（`mcdull.scheduler.history.enabled=false` 可关闭）：

```sql
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
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '定时任务执行历史';
```

### 7. 执行历史

db-scheduler 的 `scheduled_tasks` 每条任务只有一行，只保留 `last_success` / `last_failure` 等"最后一次"状态，**没有执行流水**。
开启（默认开启）后，`McdullExecutionInterceptor` 会在每次执行时：

1. 执行前插入一条 `RUNNING` 记录（写入失败只打印 warn，**不影响任务执行**）
2. 执行结束更新为 `SUCCESS` / `FAILED`，并记录耗时与失败原因

`trace_id` 会自动取 MDC 中的 `traceId`（没有则生成），可直接串到业务日志。

查询方式：

```java
@Resource
private TaskQueryService taskQueryService;

// 查某任务最近 50 次执行
List<TaskExecutionRecord> records = taskQueryService.executions("task-database-backup", null, 50);

// 查全库最近的失败记录
List<TaskExecutionRecord> failures = taskQueryService.executions(null, "FAILED", 20);
```

字段说明：

| 字段 | 说明 |
|---|---|
| `scheduled_at` | 计划执行时间，取 `ExecutionContext.getExecution().getExecutionTime()`；与实际开始时间的差值即调度延迟 |
| `attempt_number` | 取 `Execution.consecutiveFailures + 1`，反映该任务连续第几次执行 |
| `status` | `RUNNING`（执行中）/ `SUCCESS` / `FAILED` / `LOST`（进程异常退出未回写） |
| `executor_node` | 执行节点主机名，多实例部署时区分是哪个节点跑的 |

**注意**：进程被强制 kill 时，`RUNNING` 记录不会被更新。需要定期调用 `TaskHistoryRepository.markLost(timeoutMs)` 兜底，把超时仍为 `RUNNING` 的记录标记为 `LOST`（`timeoutMs` 应大于任务最长执行时间）。

**清理建议**：`TaskHistoryRepository.purgeExpired()` 已提供按保留天数清理的能力，可自行包一个定时任务周期执行：

```java
@Component
public class HistoryCleanupTask implements McdullScheduledTask<Void> {
    @Resource
    private TaskHistoryRepository historyRepository;

    @Override
    public McdullScheduleDefinition definition() {
        return McdullScheduleDefinition.builder()
                .name("task-history-cleanup")
                .cron("0 30 3 * * ?")
                .build();
    }

    @Override
    public void execute(TaskInstance<Void> instance, ExecutionContext context) {
        // 先把超时未回写的 RUNNING 记录标记为 LOST（阈值需大于任务最长执行时间）
        historyRepository.markLost(TimeUnit.HOURS.toMillis(2));
        historyRepository.purgeExpired();
    }
}
```

## 注意事项

### 1. 统一 DataSource

`SchedulerDataSourceProvider` 按以下顺序解析调度表库数据源：

1. `mcdull.scheduler.data-source-url`（显式指定）
2. 容器中名为 `dataSource` 的普通数据源
3. 基于 `spring.datasource.*` 自建独立连接池（推荐，默认走这里）
4. 容器中唯一的数据源

**为什么自建连接池**：业务模块的数据源是 `GlobalDataRoutingDataSource`（`AbstractRoutingDataSource`），依赖 `DynamicContextHolder` 的 ThreadLocal 选择目标库。db-scheduler 的调度线程不会经过业务请求链路，ThreadLocal 为空会导致拿不到连接，因此调度器必须使用独立连接池。

**为什么不用 `@Bean DataSource`**：容器中一旦出现第二个 `DataSource` 类型 Bean，`MysqlAutoConfiguration` 中的按类型注入（`@Resource private DataSource dataSource`）就会报 `required a single bean, but 2 were found`。所以这里只暴露 `SchedulerDataSourceProvider` 包装类，不暴露 `DataSource` 类型 Bean。

**不要让各模块的调度表库指向不同的库**，否则集群锁失效。

### 2. ArchUnit 约束

`mcdull-framework-enforcer` 中会加入以下约束（CI 强制检查）：
- `@McdullScheduled` 不能标注在 Controller 层方法上
- `McdullScheduledTask` 实现类不能依赖 Controller 层
- `McdullScheduledTask` 必须在 service / task / config 包下

### 3. 异常处理

任务执行抛出的异常会被 db-scheduler 捕获，根据 `maxRetries` 配置自动重试。最终失败后：
- `scheduled_tasks` 表中 `consecutive_failures` 计数器 +1
- `last_failure` 更新为当前时间
- `AlarmExecutionInterceptor` 触发告警（如果已注册）

### 4. Starter 不提供 Controller

Starter 只提供 Bean（SchedulerManager、TaskQueryService、SchedulerAlarmService）。任务管理的 Controller 和 UI 由 system 模块后续实现。

### 5. 线程池

db-scheduler 有自己的调度线程池（`thread-count` 默认 10）。如果任务执行非常耗时，建议在 `execute()` 内部使用 `CompletableFuture` 或异步执行逻辑，避免阻塞调度线程。

### 6. 多实例启动顺序

所有业务模块都可以启动 Scheduler。Scheduler 只负责注册本模块的任务，集群锁由 db-scheduler 保证跨模块协调。**不依赖任何模块的启动顺序**。
