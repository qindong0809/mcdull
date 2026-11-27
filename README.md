<div align="center">

# McDull

### 企业级 Spring Cloud 微服务开发框架

从真实线上环境中提炼，提供标准化、可扩展的微服务架构脚手架

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.16-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.3-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Sa-Token](https://img.shields.io/badge/Sa--Token-1.42.0-blue)](https://sa-token.cc)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.16-blue)](https://baomidou.com)
[![License](https://img.shields.io/badge/License-Apache%202.0-green)](LICENSE)
[![Gitee](https://img.shields.io/badge/Gitee-dqcer%2Fmcdull-C71D23?logo=gitee&logoColor=white)](https://gitee.com/dqcer/mcdull)

[快速开始](#快速开始) · [模块说明](#项目结构) · [亮点功能](#亮点功能) · [文档](#文档) · [问题反馈](https://gitee.com/dqcer/mcdull/issues)

</div>

---

## 关于项目

McDull 是一个基于 **Spring Boot 3 + Spring Cloud** 的企业级微服务开发框架，从真实线上生产环境提炼而来，提供一套开箱即用的标准化脚手架。

**设计目标：**

- 最小依赖原则，杜绝循环重复依赖
- 模块化架构，层次清晰，方便按需引入
- 前沿技术与成熟度的平衡选型
- 有节制地使用第三方组件，最大程度实现自主可控
- 通过 ArchUnit 静态检查在 CI/CD 层面强制架构规范

---

## 技术栈

| 类别 | 技术 | 版本 |
|---|---|---|
| 基础框架 | Spring Boot | 3.5.16 |
| 微服务 | Spring Cloud / Spring Cloud Alibaba | 2025.0.3 / 2025.0.0.0 |
| 服务注册与配置 | Nacos | 2.x |
| API 网关 | Spring Cloud Gateway | — |
| 服务调用 | OpenFeign | — |
| 流量染色与灰度 | Spring Cloud LoadBalancer / TransmittableThreadLocal | — / 2.14.5 |
| 认证授权 | Sa-Token | 1.42.0 |
| ORM | MyBatis-Plus | 3.5.16 |
| 数据库 | MySQL | 8.x |
| 缓存 | Redis (Redisson) | 3.48.0 |
| 文档数据库 | MongoDB | — |
| 流程引擎 | Warm Flow | — |
| 对象存储 | x-file-storage | 2.2.1 |
| API 文档 | Knife4j | 4.5.0 |
| 监控 | Spring Boot Admin / JavaMelody | — |
| 架构检查 | ArchUnit | 1.5.0 |
| 构建工具 | Maven | 3.6+ |
| CI/CD | Gitee Go 流水线 | — |

---

## 亮点功能

> 按能力层次分为两组：**框架基础能力**（可独立引入的 Starter）和**业务系统能力**（内置于业务模块）。

### ── 框架基础能力 ──


### 🏗️ ArchUnit 架构守护

`mcdull-framework-enforcer` 提供规则库，由各业务模块在自己的 `src/test` 中声明 `@ArchTest` 落地检查。

内置约束规则（以 system-impl 实测校准）：
- 分层调用：`Controller` 不得被任何层访问；`Service` 可被 Controller/Entry/Manager 调用；`Manager` 可被 Service/Entry 调用；仅 Service/Manager 可调用 `Mapper`
- `Entry` 层覆盖 Feign 服务端、facade SPI 实现、流程编排、监听器与定时任务等入口适配
- 命名规范按包判定：`..dto..` ⇒ `DTO` 结尾、`..vo..` ⇒ `VO` 结尾、`..entity..` ⇒ `Entity` 结尾、`..bo..` ⇒ `BO` 结尾、`..web.mapper..` ⇒ `Mapper` 结尾、枚举 ⇒ `Enum` 结尾
- 角色归属按标记接口判定：实现 `DTO`/`VO`/`Entity` 的类必须位于对应角色包，防止 `MenuVO extends MenuEntity` 这类跨角色继承
- `facade` 契约包不得依赖 `provider` 实现包，防止契约反向引用实现
- 禁止 `System.out/err`、`e.printStackTrace()`
- 禁止直接抛出 `Exception`、`RuntimeException`、`Throwable`

```java
// 按包根限定分析范围；不要排除 jar，否则同组的 *-facade 契约类不参与分析，
// facade 隔离规则会 0 命中并静默通过（现由 facadeClassesLoadedRule 自动校验）
@AnalyzeClasses(
        packages = "io.gitee.dqcer.mcdull.system",
        importOptions = ImportOption.DoNotIncludeTests.class)
class SystemArchitectureTest {

    // 自动校验 facade 契约类已进入测试 classpath，取代原先靠注释维持的约束
    @ArchTest
    static final ArchRule FACADE_CLASSES_LOADED = ArchitectureEnforcer.facadeClassesLoadedRule();

    @ArchTest
    static final ArchRule LAYER_CALL_RULES = ArchitectureEnforcer.layerChecks();

    @ArchTest
    static final ArchRule FACADE_MUST_NOT_DEPEND_ON_PROVIDER =
            ArchitectureEnforcer.facadeDependencyRules();
}
```

> `@ArchTest` 字段类型必须是 `ArchRule`，ArchUnit 不支持直接标注 `List<ArchRule>`。
> 架构守护采用**零容忍**策略：规则直接声明，任何违规都会导致构建失败，不生成基线文件。
> 若某模块确有无法立即整改的存量违规，可临时改用 `ArchitectureEnforcer.frozen(...)` 冻结基线；此时需在测试 classpath 提供 `archunit.properties` 开启 `freeze.store.default.allowStoreCreation=true`，并将生成的 `archunit_store/` 基线文件随代码提交（否则 CI 每次都相当于首次运行）。基线只是过渡手段，目标仍是清零后回到零容忍。
> `facadeClassesLoadedRule()` 用 `allowEmptyShould(false)` 把“0 命中”变成失败；仅适用于 facade 中确有契约类的模块（system / workflow / demo）。blaze 的 facade 为空模块、focus-facade 仅有一个不在 `facade` 包的 Main，这两个模块不声明该规则，也不声明本就空跑的 `facadeDependencyRules`。

运行架构测试（与单元测试同一套生命周期，默认就会执行）：

```bash
# 全部模块的单元测试 + 架构测试
mvn test

# 只跑某个模块
mvn -B -pl mcdull-business/mcdull-business-system/system-impl test

# 只跑某个架构测试类
mvn -B -pl mcdull-business/mcdull-business-system/system-impl test -Dtest=SystemArchitectureTest
```

### 🔐 多端点认证体系（Sa-Token）

基于 Sa-Token 实现管理端、App 端、普通用户端三套独立的会话体系，通过 `StpKit` 统一管理。每次请求由 `HttpTraceLogFilter` 完成：
- TraceId 注入（支持从上游 Header 透传）
- 用户会话加载到 `UnifySession`（`UserContextHolder` ThreadLocal 存储）
- 数据源路由切换
- 安全响应头自动注入（`X-Content-Type-Options`、`X-Frame-Options`、`X-XSS-Protection`）

### 🔒 声明式分布式锁

基于 Redisson 封装 `@RedisLock` 注解，支持 SpEL 表达式动态生成 key，零侵入加锁。

```java
@RedisLock(key = "'order:submit:' + #userId", timeout = 10)
public void submitOrder(Long userId) { ... }
```

获取锁失败时**默认抛出业务异常**（`system.busy`），调用方不会静默拿到 `null`；幂等补偿等允许降级的场景可显式设 `throwOnFailure = false`。中断会恢复线程中断标记，释放锁前校验持锁线程，避免看门狗租约过期后误解锁。

同时提供编程式 `ConcurrentRateLimiter`，支持限流和带超时的分布式锁，并可选是否抛出异常（适用于幂等补偿场景）。

### ⏱️ @CacheExpire — 缓存防雪崩

扩展 Spring `@Cacheable`，支持精确控制每个方法的缓存过期时间，并内置随机浮动范围（`floatRange`），消除大量 key 同时失效导致的缓存雪崩。

```java
@Cacheable(value = "user", key = "#id")
@CacheExpire(value = 300, floatRange = 0.1)  // 300s ± 随机浮动
public UserVO getUser(Long id) { ... }
```

### 🗂️ Redis 缓存

基于 Redisson 的分布式缓存，多实例共享同一份数据；通过 `@Cacheable` / `@CacheEvict` 注解使用，或直接注入 `ICache` 契约操作。

### 🏷️ @Transform — 返回值字段自动翻译

`TranslatorAspect` + `@Transform` 注解，在 Controller 返回值切面中自动将枚举 code、字典 ID 翻译为显示文本，无需在每个 Service 方法中手动转换，支持分页列表、集合、嵌套对象的递归翻译。

```java
@Transform(from = "statusCode", dataSource = StatusEnum.class)
private String statusName;
```

### 🔣 统一枚举体系（IEnum）

`IEnum<T>` 接口将枚举涉及的六个横切关注点统一处理，业务枚举只需实现接口并在构造器调用 `init()` 即可获得所有能力：

```java
public enum StatusEnum implements IEnum<Integer> {
    ACTIVE(1, "status.active"),
    INACTIVE(0, "status.inactive");

    StatusEnum(Integer code, String text) {
        this.init(code, text);  // 自动注册到 DictPool
    }
}
```

| 能力 | 说明 |
|---|---|
| **内存字典缓存** | `DictPool` ConcurrentHashMap，`init()` 时自动注册，查询零 DB 开销 |
| **@Deprecated 自动过滤** | 枚举值标记废弃后，`getAll()` 自动从下拉列表排除，旧数据兼容不受影响 |
| **i18n 自动集成** | `getText()` 优先走 `DynamicLocaleMessageSource` 查 i18n key，多语言零额外代码 |
| **入参合法性校验** | `@EnumsIntValid(StatusEnum.class)` 标在 DTO 字段，Bean Validation 自动拦截非法值 |
| **API 文档自动枚举** | `@SchemaEnum(StatusEnum.class)` 标在 DTO 字段，Knife4j 文档自动列出所有枚举值 |
| **`@Transform` 翻译** | `TranslatorAspect` 直接识别 `IEnum` 类型，code → label 翻译零配置 |

契约包（`*-facade`）例外：跨模块共享的枚举不实现 `IEnum`，只带 `code`/`text` 与自己的静态查找方法，
避免契约包为了字典能力依赖 starter-web；下拉项由调用方解析成 `{code, text}` 后传给 `DynamicFieldBO`。

### 🌐 国际化 + 多时区 + 自定义日期格式

三个能力组合，满足企业级多地区部署需求：
- `CustomizeLocaleResolver` 根据请求动态解析语言，`DynamicLocaleMessageSource` 支持运行时切换
- `@DynamicDateFormat(enableTimezone = true)` + `DynamicDateSerialize` 按用户会话中的时区和日期格式偏好序列化，无需业务代码感知
- 导出场景提供 `CustomSerializer.serializeDate()` 工具方法，统一日期处理入口


### ⚙️ 自研业务流程编排框架（Process Flow）

`mcdull-framework-starter-flow` 提供轻量级流程编排能力，替代 if-else 堆叠。流程定义与节点实现完全解耦，流程图外置为 JSON，运行时动态装配。

```json
// flow/process_flow_login.json
{
  "id": "process_flow_login",
  "nodeList": ["Validate Captcha", "Validate Login Name Password", "Authority"]
}
```

```java
@Component("Validate Captcha")
@TreeNode(code = "Validate Captcha")
public class ValidateCaptcha implements ProcessHandler<LoginContext> {
    @Override
    public void execute(LoginContext context) { ... }

    @Override
    public void catchException(Exception e, LoginContext context) { ... }

    @Override
    public void finallyException(ProcessHandler handler, Exception e, LoginContext context) { ... }
}
```

关键设计：
- **节点级 try-catch-finally**：每个节点独立定义异常处理，不污染其他节点
- **父节点 try 共享**：`extendParentNodeTryAttr() = true` 时，子节点复用父节点的 catch/finally 逻辑
- **终止节点**：`endNode() = true` 时流程在当前节点后停止
- **Context 透传**：贯穿全流程，节点间通过 `Dict` 传递中间结果，无需方法入参层层传递
- **整体事务**：`ProcessFlow.run()` 带 `@Transactional`，整个流程在一个事务内

### 🗄️ 多层动态数据源路由

`mcdull-framework-starter-mysql` 实现三层叠加的数据源路由体系，支持多租户数据隔离：

| 层次 | 实现类 | 用途 |
|---|---|---|
| 平台层 | `GlobalDataRoutingDataSource` | 全局默认数据源，请求进入时切换 |
| 租户层 | `TenantDataRoutingDatSource` | 按 tenantId 动态路由到对应租户库 |
| 方法层 | `@DynamicDataSource` + `DataSourceAspect` | 注解声明，SpEL 表达式动态计算 key |

`SwitchableDataSource` 接口统一抽象切换行为，业务代码通过 `get(supplier)` 模式在指定数据源上执行，自动 finally 清理上下文。

### 🔗 声明式第三方系统对接（Integration Starter）

`mcdull-framework-starter-integration` 提供类似 OpenFeign 的编程模型，专门用于对接外部第三方 HTTP 系统。只需定义接口 + 注解，框架自动生成动态代理，处理认证、序列化、重试和日志，业务代码零侵入。

```yaml
mcdull:
  integration:
    clients:
      demo:
        base-url: https://demo.example.com
        auth:
          type: jwt-account   # none | jwt-account | credentials | api-key
          account-name: admin
          secret-key: ${SECRET_KEY}
          token-path: /api/auth/token
```

```java
@IntegrationApi(name = "demo")
public interface DemoClient {
    @IntegrationPost("/upload/presign")
    PreSignVO presignUpload(@Body PreSignRequest request);

    @IntegrationGet("/user/{userId}/info")
    UserVO getStudy(@PathVar("userId") Long userId);
}
```

四种认证策略：`none` / `jwt-account`（自动缓存续期，token 过期前 60s 预刷新）/ `credentials` / `api-key`。

### ⏰ 定时任务调度（Scheduler Starter）

调度契约位于 `mcdull-framework-api-scheduler`，运行时由 `mcdull-framework-starter-scheduler` 基于
[db-scheduler](https://github.com/kagkarlsson/db-scheduler) 装配，提供集群安全、单表持久化、
失败重试的企业级定时任务能力，替代 Spring 原生 `@Scheduled` 和 Hutool CronUtil。
业务模块只需依赖 `api-scheduler`，`db-scheduler` 的类型被适配器挡在 starter 内部，不会泄漏到业务代码。
bootstrap 依赖 `starter-scheduler` 提供运行时装配。

采用"三层暴露"设计，业务方按需选型：

```java
// 第一层：声明式注解（覆盖 80% 简单场景）
@McdullScheduled(name = "db-backup", cron = "0 0 2 * * ?", maxRetries = 2)
public void backup() { /* ... */ }

// 第二层：接口实现（StatefulTask / 链式调度 / 动态 cron）
@Component
public class DataSyncTask implements McdullScheduledTask<DataSyncState> {
    @Override
    public McdullScheduleDefinition definition() {
        return McdullScheduleDefinition.builder()
            .name("data-sync")
            .cron(nacosConfig.getProperty("data-sync.cron"))
            .stateful(true).build();
    }
    @Override
    public void execute(TaskContext<DataSyncState> context) {
        // 可通过 context.data() 读取上次持久化的状态、拿到实例 ID 与执行诊断信息
    }
}

// 第三层：SchedulerManager（运行时管理）
schedulerManager.trigger("db-backup");       // 手动触发
schedulerManager.pauseTask("db-backup");     // 暂停
schedulerManager.resumeTask("db-backup");    // 恢复
```

核心特性：
- **集群安全**：所有调度器实例读写同一张 `scheduled_tasks` 表，数据库行锁保证多实例下单次执行
- **自动建表**：启动时 `CREATE TABLE IF NOT EXISTS`，业务方无需手动建表
- **失败重试**：支持 FIXED / EXPONENTIAL 退避策略
- **状态持久化**：有状态任务由底层调度器维护 data，下次执行可通过 `TaskContext.data()` 读取
- **运行时管理**：`SchedulerManager` 提供手动触发/暂停/恢复，`TaskQueryService` 提供任务列表和执行历史查询

### 📦 Facade 发布通知 Maven 插件

`mcdull-devtools-notify` 在 Facade 模块执行 `mvn deploy` 后，自动向 IM 群机器人推送 **“发布了什么 · 改了什么 · 影响谁”**，解决上游接口发布后下游无感知、靠人工通知的问题。

业务 Facade 只需引入插件，版本与默认配置由根 POM 的 `pluginManagement` 统一管理：

```xml
<plugin>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-devtools-notify</artifactId>
</plugin>
```

核心特性：
- **变更摘要**：通过 JGit 自动获取当前分支、版本及最近提交，并生成可点击的 commit 链接
- **下游影响**：分析 Maven Reactor 模块依赖，自动列出直接依赖当前 Facade 的下游模块
- **默认安全**：默认跳过 SNAPSHOT 版本，未配置 Webhook 时静默跳过，不在仓库中保存机器人地址
- **永不阻断构建**：Git 信息解析、下游分析或通知发送失败时仅记录警告，不影响 `deploy` 结果
- **灵活配置**：支持通过命令行、环境变量、`settings.xml` 或 POM 设置 `mcdull.notify.*` 参数

```bash
mvn deploy "-Dmcdull.notify.webhook=https://oapi.dingtalk.com/robot/send?access_token=XXXX"
```

完整接入与配置说明见 [mcdull-devtools-notify/README.md](mcdull-devtools/mcdull-devtools-notify/README.md)。

### 🔌 Feign 跨服务异常透传

`mcdull-framework-starter-feign` 解决微服务间 Feign 调用时异常信息丢失的问题：

- `FeignErrorDecoder`：HTTP 非 2xx 响应时统一日志记录 + 封装为可识别异常
- `ResultApiParse.getInstance()`：区分**系统异常**（`FeignServiceErrorException`）和**业务异常**（`FeignBizException`），调用方可分别捕获处理

### 🎯 全链路流量染色与环境隔离（Tag Starter）

`mcdull-framework-starter-tag` 基于 Spring Cloud LoadBalancer + TransmittableThreadLocal 实现灰度与泳道路由：请求头 `X-Env-Tag` 与 Nacos 实例元数据 `env-tag` 联动，业务代码零侵入。

```yaml
# 实例打标：本地开发自动携带环境变量或操作系统登录用户名
spring:
  cloud:
    nacos:
      discovery:
        metadata:
          env-tag: ${ENV_TAG:${user.name}}

mcdull:
  tag:
    enabled: true        # 是否启用标签路由（默认 true）
    strict: false        # true = 找不到标签实例直接阻断，绝不回流基准池（生产强隔离）
    clean-header: false  # true = 网关剥离外部传入的 X-Env-Tag，防伪造染色
```

选址按三级漏斗降级，兼顾隔离精度与调用链可用性：

| 级别 | 规则 |
|---|---|
| 精准匹配 | 取 `X-Env-Tag`（无请求头时取线程上下文），命中 `metadata.env-tag` 相同的健康实例 |
| 基准兜底 | 未命中且 `strict=false` ⇒ 路由到 `env-tag` 为空的公共基准实例 |
| 紧急容灾 | 连基准实例都不存在（如新服务仅在特性分支首次上线）⇒ 全部健康实例随机选取 |

透传链路覆盖 `WebFlux 网关 → Servlet 微服务 → OpenFeign 下游 → @Async 线程池`，网关与业务微服务共用同一个依赖，各环节由条件注解按需装配（`WebFilter` / `FilterRegistrationBean` / `RequestInterceptor` / `AsyncContextPropagator`）：

- **异步线程池联动**：注册 `envTagContextPropagator` Bean（`mcdull-framework-spi` 的 `AsyncContextPropagator` 实现），被 `mcdull-framework-starter-web` 的 `AsyncContextSupport` 自动收集；「捕获-替换-还原」由线程池统一执行，兼容 `CallerRunsPolicy` 拒绝策略
- **生产安全**：网关开启 `clean-header=true` 后自动清洗外部染色头，内部灰度应在鉴权通过后由服务端写入
- **典型场景**：多人共用一套开发环境的本地联调隔离、仅给改动的 1~2 个服务打标签的测试泳道（无需克隆整套测试集群）、生产金丝雀灰度

> 保留字 `X-Env-Tag: default` 强制路由到无标签基准实例——本地起了服务但想直接对比公共开发服时，无需关闭 IDEA。
> 完整接入与联调验证步骤见 [mcdull-framework-starter-tag/README.md](mcdull-framework/mcdull-framework-starters/mcdull-framework-starter-tag/README.md)。

### 🔬 Java Agent 零侵入性能探针

`mcdull-framework-agent` 通过 JVM `-javaagent` 参数挂载，**无需修改任何业务代码**，采集内容远超普通 APM 埋点：

| 采集维度 | 说明 |
|---|---|
| 方法耗时 | 精确到毫秒 |
| 入参 / 出参 | 序列化记录，便于问题复现 |
| 异常捕获 | 自动关联到对应方法调用 |
| TraceId | 与日志链路打通 |
| 堆内存快照 | 方法执行**前后**分别记录 init/used/committed/max 及使用率 |
| 非堆内存快照 | 同上，覆盖 Metaspace 等区域 |
| GC 明细 | 方法执行前后 GC 次数和时间，按 GC 类型分组 |

通过前后内存对比，可以精确定位是哪个方法调用引起了内存抖动或 GC 压力。采集结果写入 `service_log` + `service_gc_log` 两张表。

### 📊 JMX 运维管理端点

`ThreadPoolJmxAdapter`、`DatabaseJmxAdapter`、`RedisJmxAdapter` 三个 MBean，通过 JConsole 或 Jolokia（HTTP 协议）实时查看和调整线程池参数、连接池状态、Redis 连接情况，生产问题不需要发版即可临时处置。

### 🛠️ 开箱即用的工程防护

引入 starter 即自动生效，无需业务代码干预：

- **全局 String 自动 Trim**（`TrimStringConfig`）：Jackson 反序列化时对所有 String 字段做 `trim()`，防止首尾空格导致的数据异常
- **ERROR/WARN 日志告警扩展点**（`AlarmRollingFileAppender`）：继承 Logback `RollingFileAppender`，子类 override `title()` 后即可对接钉钉/企微/邮件告警
- **构建版本追踪**（`VersionInfoImplComponent`）：读取 `build-info.properties` 和 `git.properties`，通过 API 可获知线上运行的是哪次构建、哪个 commit
- **HTTP 安全响应头**（`HttpTraceLogFilter`）：每个响应自动添加 `X-Content-Type-Options`、`X-Frame-Options`、`X-XSS-Protection`


### ── 业务系统能力 ──

### 🔍 多维审计体系

mcdull 提供两个层面互相补充的审计能力，覆盖"谁做了什么"和"改了哪些字段"两个维度。

**① 操作日志（请求级）**

`OperationLogsAspect` 对所有 Controller 方法环绕拦截，零侵入自动采集：请求路径、HTTP Method、客户端 IP、User-Agent、请求参数（自动过滤 Stream/MultipartFile）、接口耗时、TraceId、是否成功。日志通过 `LogFeignClient` 异步写入日志中心，`AuditAspect` 同步提取权限码写入会话，实现操作与权限的完整关联。

**② 字段级变更审计（业务级）**

`AuditUtil` + `@AuditDescription` 注解实现字段级 diff，精确描述"改了什么"：

```java
public class UserAudit implements Audit {
    @AuditDescription(label = "账号", sort = 1)
    private String loginName;

    @AuditDescription(label = "邮箱", sort = 3)
    private String email;
}

// 自动对比并生成人类可读描述
String diffText = AuditUtil.compareStr(before, after);
// 输出：用户信息<邮箱:"old@example.com"更新为"new@example.com">
```

支持：仅新增记录初始值 / 变更对比只输出有变化的字段 / 自定义包裹字符和分隔符 / 字段排序 / 日期格式化。

已覆盖：用户、角色、菜单、部门、字典、配置、消息、表单等核心业务实体。

### 📄 动态表单 + 文档导出

**动态表单引擎**

完整的低代码表单运行时，表单结构以 JSON Schema 存储，无需改动数据库和代码即可新增表单：

- 支持嵌套布局（`children` 递归解析提取叶子节点）
- 11 种控件：input、textarea、select、radio、checkbox、switch、date、time、datetime、number、file
- 发布机制：未发布时可反复修改字段，发布后锁定结构保证数据一致性
- 查询时自动将 select/radio/checkbox/switch 的 value 反查 options 还原为 label 文本

**动态 Excel 表单**

`DynamicFieldTemplate` 驱动动态列导出：表头 `*` 自动标红，下拉选项写入隐藏 Sheet 公式约束，首行冻结 + 自动筛选，导入时多 Sheet 批量处理并自动跳过 index 页。

**PDF / Word / HTML 文档生成**

- `HtmlConvertPdf`：HTML 模板 → PDF，内嵌中英文字体，支持左/右页脚分离
- `updatePdfLeftFooter`：对已生成 PDF **增量写入页脚**，不重新渲染整个文档
- `ReplaceWordPlaceholder`：Word 模板 `${key}` 占位符替换，兼容 POI 分 Run 存储问题

### 📁 文件管理 — 菜单感知目录树 + OSS 多后端

上传文件时，系统自动感知当前操作的菜单路径，在目录树中按需创建对应层级并按月自动归档：

```
系统文件/
└── 用户管理/          ← 按当前菜单自动创建
    └── 导出记录/      ← 按当前菜单自动创建
        └── 2024-07/   ← 按月自动归档
            └── export_xxx.xlsx
```

`addIfAbsent(name, parentId)` 幂等创建目录，`FolderEntity` 存储 `idPath` 保证路径可追溯。`batchFileUpload(fileList, bizId, clazz)` 通过 `@TableName` 反射建立文件与任意业务表的通用关联，删除业务对象时级联清理关联文件。`OssService` 接口屏蔽底层存储实现，切换存储后端只需改配置。

### ⏲️ 数据库自动备份

启动时从系统配置表读取 cron 表达式，动态注册定时任务：dump SQL → zip 压缩 → 上传到文件系统"数据备份"目录，自动按日期命名。分布式锁防多实例重复执行，备份策略完全由配置驱动，无需重启服务。

> 该功能已迁移至 [`mcdull-framework-starter-scheduler`](#-定时任务调度scheduler-starter)，由 db-scheduler 提供集群锁、失败重试与执行历史，cron 表达式仍由系统配置项 `task-database-backup` 驱动。

### 🔢 业务序列号生成引擎

可配置的业务单号生成系统，不依赖 Redis 自增，支持持久化历史记录：

- **格式模板**：`{YYYY}{MM}{DD}{nnnnn}`，日期部分运行时动态替换，序号部分自动补零
- **步长随机偏移**（`stepRandomRange`）：每次生成在上次基础上随机步进，防止枚举攻击推测单号规律
- **业务类型隔离**：不同业务模块独立维护序列，互不干扰
- **批量生成**：单次可生成多个连续单号，历史记录持久化到 `sys_serial_number_record`

### 🧩 领域通用工具

**子表 Diff 引擎（`DomainEngine`）**

表单提交包含子表数据时，自动对比 DB 快照与前端提交，归类为三个操作集合：

```java
CompareBean<T, PK> result = DomainEngine.compare(dbList, frontEndList);
result.getInsertList();  // 新增项（前端有、DB 无）
result.getUpdateList();  // 更新项（前端有 ID）
result.getRemoveList();  // 删除项（DB 有、前端无）
```

**协同过滤推荐算法**

内置基于用户（UserCF）和基于物品（ItemCF）的协同过滤推荐算法，泛型设计 `<S, T>` 与具体业务完全解耦：

```java
List<T> items = UserCF.recommend(userId, relateDataList);
List<T> similar = ItemCF.recommend(itemId, relateDataList);
```


---

## 项目结构

```
mcdull/
├── mcdull-framework/          # 框架层 — 基础组件与通用功能
│   ├── mcdull-framework-dependencies   # 统一依赖管理
│   ├── mcdull-framework-base           # 基础组件
│   ├── mcdull-framework-spi            # 跨 Starter 的扩展点契约（只放接口）
│   ├── mcdull-framework-json           # 日期序列化契约（@DynamicDateFormat、TimeZoneUtil）
│   ├── mcdull-framework-agent          # Java Agent 性能探针
│   ├── mcdull-framework-enforcer       # 架构约束（ArchUnit）
│   ├── mcdull-framework-apis/          # 能力契约层（业务只依赖这里，不含自动装配）
│   │   ├── mcdull-framework-api-web / api-redis / api-feign
│   │   ├── mcdull-framework-api-oss / api-flow / api-scheduler / api-security
│   ├── mcdull-framework-cores/         # 可复用行为基类与工具（如 CRUD 基座）
│   │   └── mcdull-framework-core-mysql
│   └── mcdull-framework-starters/      # 功能 Starter 集合（自动配置与运行时实现）
│       ├── mcdull-framework-starter-doc        # API 文档（Knife4j）
│       ├── mcdull-framework-starter-integration # 外部系统对接
│       ├── mcdull-framework-starter-feign      # 服务调用
│       ├── mcdull-framework-starter-flow       # 流程编排
│       ├── mcdull-framework-starter-mongodb    # MongoDB 集成
│       ├── mcdull-framework-starter-monitor    # 监控
│       ├── mcdull-framework-starter-mysql      # MySQL 集成与 CRUD 基座
│       ├── mcdull-framework-starter-nacos      # Nacos 集成
│       ├── mcdull-framework-starter-oss        # 对象存储
│       ├── mcdull-framework-starter-redis      # Redis 集成
│       ├── mcdull-framework-starter-scheduler  # 定时任务（db-scheduler）
│       ├── mcdull-framework-starter-security   # 安全认证（Sa-Token）
│       ├── mcdull-framework-starter-tag        # 流量染色与环境隔离
│       └── mcdull-framework-starter-web        # Web 基础
├── mcdull-devtools/           # 研发工具 — 开发辅助与发布协作
│   ├── mcdull-devtools-portal  # 开发工具门户（日志查看、数据库导入）
│   └── mcdull-devtools-notify  # Facade 发布通知 Maven 插件
├── mcdull-business/           # 业务层 — 具体业务实现
│   ├── mcdull-business-common     # 业务通用组件
│   ├── mcdull-business-system     # 系统管理（用户、角色、权限、代码生成）
│   ├── mcdull-business-blaze      # 核心业务模块
│   ├── mcdull-business-focus      # Focus 业务模块
│   ├── mcdull-business-workflow   # 工作流业务
│   └── mcdull-business-demo       # 可运行示例：CRUD + 架构守护 + 配置模板
├── mcdull-support/            # 支持层 — 运行支撑服务
│   ├── mcdull-gateway         # API 网关
│   ├── mcdull-monitor-server  # 监控服务
│   ├── mcdull-ai              # AI 相关功能
│   └── mcdull-agent           # AI Agent 服务
├── doc/                       # 项目文档
└── release/                   # 数据库脚本与版本发布包
    ├── sql/                   # 源脚本（按系统归集）
    └── v2.0.0/                # v2.0.0 全量/增量发布包
```

每个业务模块采用标准三层拆分：

```
business-module/
├── module-facade/      # 接口定义（API 契约、DTO、枚举）
├── module-impl/        # 业务实现（Controller / Service / Manager / DAO）
└── module-bootstrap/   # 启动入口
```

---

## 快速开始

### 环境要求

| 工具 | 版本要求 |
|---|---|
| JDK | 17+ |
| Maven | 3.6+ |
| MySQL | 5.7+ 或 8.0.14+ |
| Redis | 5.0+ |
| Nacos | 2.0+ |

### 安装与运行

**1. 克隆项目**

```bash
git clone https://gitee.com/dqcer/mcdull.git
cd mcdull
```

**2. 初始化数据库**

详见 `release/ORDER.txt`。全新库按序执行：

```bash
# 台账表 + 全量脚本（必须）
release/sql/00_bootstrap.sql
release/v2.0.0/system/v2.0.0_system.full.sql
release/v2.0.0/workflow/v2.0.0_workflow.full.sql

# 可选：行政区域数据、Focus 业务
release/sql/sys_area.sql
release/sql/focus.sql
```

已有库升级执行 `release/v2.0.0/<system>/v2.0.0_<system>_upgrade.sql`。源脚本变更后运行 `release/build-full.ps1` 重新生成全量。

**3. 配置 Nacos**

业务服务全环境默认接入 Nacos，**仅作为服务注册中心**（服务注册与发现），不承载业务配置。Nacos 默认使用 `public` 命名空间；如需隔离，可设置 `NACOS_NAMESPACE`。业务配置通过环境变量或本地 `application-*.yml` 注入，无需在 Nacos 中创建共享 Data ID。

环境差异通过 `SPRING_PROFILES_ACTIVE`（`local` / `test` / `prod`）以及各服务前缀的环境变量注入，无需修改 jar 内 yml。

**4. 编译项目**

```bash
# 默认执行单元测试与 ArchUnit 架构检查（比 -DskipTests 多花 1~2 分钟，请勿省略）
mvn clean install
```

> **关于跳过测试**：`skipTests` 只能由命令行 `-DskipTests` 显式传入，仅限本地紧急排查或热修时使用，提交前与主干合并必须以 `mvn clean install` 通过为准。各模块 POM 中**不得**写死 `<skipTests>`——它会静默关掉该模块及其后续新增的全部测试。

**5. 启动服务**

按以下顺序启动：
1. Nacos（服务注册中心）
2. `mcdull-gateway`（API 网关）
3. `mcdull-business-system`（系统核心服务，其他服务依赖）
4. 其他业务服务（system / blaze / focus / workflow / demo，按需启动）

**6. 启动前端**

配套前端工程：[mcdull-vue3](https://gitee.com/dqcer/mcdull-vue3)，具体运行方式参见前端工程的 README。

---

## 文档

详细文档分布在 `doc/` 目录和各模块目录：

| 文档 | 说明 |
|---|---|
| [部署说明](#部署说明) | 打包与部署说明（见本文） |
| [本地启动说明.md](doc/本地启动说明.md) | 每个服务一键复制的环境变量与本地启动说明 |
| [mcdull-business-demo/README.md](mcdull-business/mcdull-business-demo/README.md) | 业务模块脚手架示例与最佳实践 |
| [mcdull-devtools-notify/README.md](mcdull-devtools/mcdull-devtools-notify/README.md) | Facade 发布通知插件使用说明 |
| [mcdull-framework-agent/README.md](mcdull-framework/mcdull-framework-agent/README.md) | Java Agent 性能探针使用说明 |
| [mcdull-framework-enforcer/README.md](mcdull-framework/mcdull-framework-enforcer/README.md) | ArchUnit 架构约束使用说明 |
| [mcdull-framework-starter-scheduler/README.md](mcdull-framework/mcdull-framework-starters/mcdull-framework-starter-scheduler/README.md) | 定时任务调度使用说明 |
| [mcdull-framework-starter-tag/README.md](mcdull-framework/mcdull-framework-starters/mcdull-framework-starter-tag/README.md) | 全链路流量染色与环境隔离使用说明 |

---

## 开发指南

### 代码规范

- 遵循 Java 编码规范，使用 Lombok 简化代码
- 统一异常处理与日志管理
- 严格遵守 Controller → Service → Manager → DAO 四层架构
- 模块间通信通过 facade 接口，禁止跨模块直接依赖 impl 层
- 使用 ArchUnit 验证架构约束：提交前 `mvn test` 必须全绿，各模块 `*ArchitectureTest` 不得跳过

### 测试

**默认执行**：`mvn clean install` 与 `mvn test` 会跑全部单元测试和各模块的 ArchUnit 架构测试，无需任何额外参数。

```bash
# 全部测试
mvn test

# 单个模块
mvn -B -pl mcdull-business/mcdull-business-common test

# 单个测试类
mvn -B -pl mcdull-business/mcdull-business-demo/demo-impl test -Dtest=DemoTaskEnumTest
```

**工程约定**

| 约定 | 原因 |
|---|---|
| `maven-surefire-plugin` 的版本与行为只在根 `pom.xml` 的 `pluginManagement` 中定义 | 避免各模块版本漂移；模块 POM 不再声明 surefire |
| 任何模块 POM **不得**写死 `<skipTests>` | 写死会静默关掉该模块及后续新增的全部测试；且 POM 里的 `<skipTests>` 会**覆盖**命令行 `-DskipTests`，让"临时跳过"永久化 |
| 跳过测试只允许命令行 `-DskipTests` | 仅限本地紧急排查或热修，提交前必须用 `mvn clean install` 复验 |
| 集成测试用 `@Disabled` 并写清原因 | 跳过必须是可见、可审计的；不要注释掉 `@Test` 冒充"这个模块没有测试" |

**当前基线**：14 个测试类、92 个用例（含 2 个带原因的显式跳过）、0 失败；其中 66 个用例是 ArchUnit 架构规则（含 3 个 facade 契约类装载校验），另有 1 个无断言的架构诊断用例（`ArchitectureDiagnosticTest`），仅用于输出报告，不作为门禁。

### 开发流程

1. 从 `master` 创建功能分支
2. 完成功能开发和测试，确保 `mvn clean install` 全绿（含 ArchUnit 架构检查）
3. 提交代码，发起 Pull Request
4. 代码审查通过后合并

---

## 部署说明

### 本地开发

- IDE：IntelliJ IDEA（推荐）
- 激活 profile：`SPRING_PROFILES_ACTIVE=local`
- 配置文件：各 `*-bootstrap/src/main/resources/application-local.yml`（仅本地默认值）
- 数据库 / Redis：默认本机实例，可用 `SYSTEM_DB_HOST` / `BLAZE_DB_HOST` 等环境变量覆盖（`*_DB_HOST` 只写主机名，端口与库名在 yml 中写死）
- Nacos：全环境默认启用，需保证 `NACOS_ADDR` 可达

### 测试 / 生产环境

- 部署方式：Docker 容器
- 激活 profile：`test` 或 `prod`
- 配置文件：各 `*-bootstrap` 下的 `application-test.yml` / `application-prod.yml`（只含环境变量占位符）
- 数据库 / Redis / 凭据：由部署平台以环境变量或 Secret 注入
- 服务注册与配置：Nacos 集群（`NACOS_ADDR`、`NACOS_NAMESPACE`）

---

## 贡献

欢迎提交 Issue 和 Pull Request。

1. Fork 本仓库
2. 创建功能分支：`git checkout -b feature/your-feature`
3. 提交改动：`git commit -m 'feat: add some feature'`
4. 推送分支：`git push origin feature/your-feature`
5. 发起 Pull Request

---

## License

本项目基于 [Apache License 2.0](LICENSE) 开源。

---

## 联系

- 作者：dqcer
- 邮箱：dqcer@sina.com
- Gitee：[https://gitee.com/dqcer/mcdull](https://gitee.com/dqcer/mcdull)
