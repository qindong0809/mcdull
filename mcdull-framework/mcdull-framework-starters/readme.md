# mcdull Framework Starters

## 概述

本模块包含了mcdull框架的所有启动器组件，为微服务应用提供开箱即用的功能模块。

## 启动器分类

### Web Application Starters（Web应用启动器）
负责Web应用的核心功能，包括基础Web框架、安全认证和API文档生成。

- **mcdull-framework-starter-web**: Web应用基础功能，包括Spring MVC、异常处理、参数验证等
- **mcdull-framework-starter-security**: 安全认证功能，基于Sa-Token的权限控制
- **mcdull-framework-starter-doc**: API文档生成，基于Knife4j和SpringDoc OpenAPI

### Data Storage Starters（数据存储启动器）
提供各种数据存储解决方案的集成。

- **mcdull-framework-starter-mysql**: MySQL数据库集成，包括MyBatis Plus配置、连接池与 CRUD 基座
  （`BasicServiceImpl`、`BasicCurdServiceImpl`、`GenericLogic`、`LogicCheckUtil`）
- **mcdull-framework-starter-redis**: Redis缓存集成，支持分布式锁和缓存操作
- **mcdull-framework-starter-mongodb**: MongoDB文档数据库集成

### Integration & Service Starters（集成服务启动器）
处理微服务间通信和外部服务集成。

- **mcdull-framework-starter-nacos**: Nacos 服务注册与发现集成（仅注册中心，不接入配置中心）
- **mcdull-framework-starter-feign**: 基于Feign的服务间调用组件
- **mcdull-framework-starter-oss**: 对象存储服务集成，支持阿里云OSS等

### Business & Monitoring Starters（业务监控启动器）
提供业务流程和系统监控功能。

- **mcdull-framework-starter-flow**: 自研流程编排引擎
  - 基于注解的节点定义（@TreeNode）
  - 支持复杂业务流程编排
  - 事务管理和异常处理
  - JSON配置的流程定义
  - 节点注册和动态加载
  - 上下文传递和状态管理
- **mcdull-framework-starter-monitor**: 应用监控和健康检查功能

## 使用方式

在需要使用特定功能的模块中，添加对应的启动器依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-flow</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 流程编排使用示例

1. 定义流程节点：
```java
@TreeNode(code = "user.param.check")
public class ParamCheck implements ProcessHandler<SimpleContext> {
    @Override
    public void execute(SimpleContext context) {
        // 处理逻辑
    }
}
```

2. 配置流程定义（JSON文件）：
```json
[{
  "id": "process_flow_simple",
  "remark": "test process flow",
  "nodeList": [
    "user.param.check",
    "user.get.id"
  ]
}]
```

3. 执行流程：
```java
@Resource
private ProcessFlow processFlow;

public void executeFlow() {
    SimpleContext context = new SimpleContext();
    context.setId("process_flow_simple");
    processFlow.run(context);
}
```

## 设计原则

1. **最小依赖**: 每个启动器只包含必要的依赖，避免引入不需要的组件
2. **自动配置**: 提供合理的默认配置，支持通过配置文件自定义
3. **模块化**: 各启动器之间解耦，可以独立使用
4. **企业级**: 面向生产环境，注重性能和稳定性

## 依赖契约

`mcdull-framework-starter-web` 只传递 `mcdull-framework-base`、`mcdull-framework-config`、
`mcdull-framework-spi`。它**不再**传递 `starter-security`、`starter-oss`、`starter-doc`、
`starter-monitor`、`starter-redis`、`starter-mysql`，也不再携带日期序列化器（已独立成
`mcdull-framework-json`）与 CRUD 基座（已下沉到 `core-mysql`）。

因此使用方必须显式声明自己直接引用的模块，不要再依赖 web 的传递依赖：

| 你要用的能力 | 必须显式声明 |
| --- | --- |
| `StpKit`、`AbstractWebMvcConfigurer`、`AbstractUserDetailsService`、`PasswordUtil` | `mcdull-framework-api-security` |
| `@SaCheckEL`、`@SaCheckPermission` 等注解鉴权 + 登录态闸门 | `mcdull-framework-starter-security`（`api-security` 传递 sa-token 注解） |
| `IService`、`ServiceImpl`、`IRepository`、`Page`、`LambdaQueryWrapper` 等 MyBatis-Plus 类型 | `mcdull-framework-core-mysql` |
| `@EnableDynamicDataSource`、`DynamicContextHolder` | `mcdull-framework-starter-mysql` |
| `SwitchableDataSource`、`BasicServiceImpl`、`BasicCurdServiceImpl`、`GenericLogic`、`LogicCheckUtil` | `mcdull-framework-core-mysql` |
| `@DynamicDateFormat`、`DynamicDateSerialize`、`CustomSerializer`、`TimeZoneUtil` | `mcdull-framework-json` |
| `@EnableCache` | `mcdull-framework-starter-redis` |
| `@CacheExpire`、`@RedisLock`、`IRedisClient`、`IRedissonCache` | `mcdull-framework-api-redis` |
| `@EnableFileStorage` | `mcdull-framework-starter-oss` |
| `OssService` | `mcdull-framework-api-oss` |
| `ProcessHandler`、`@TreeNode`、`Context`、`IProcessFlow` | `mcdull-framework-api-flow` |
| 流程引擎自动装配、节点注册、`ProcessFlow` 实现 | `mcdull-framework-starter-flow` |
| `McdullScheduledTask`、`McdullScheduleDefinition`、`BackoffStrategy`、`TaskContext`、`@McdullScheduled` | `mcdull-framework-api-scheduler` |
| 调度器自动装配、`SchedulerManager`、`TaskQueryService`、db-scheduler 运行时 | `mcdull-framework-starter-scheduler` |
| `@Tag`、`@Operation`、`@Schema` 等 OpenAPI 注解与 `/doc.html` | `mcdull-framework-starter-doc` |
| `IEnum`、`EnumVO`、`BasicController`、`SystemEnvironment` 等 Web 契约与基类 | `mcdull-framework-api-web` |
| `@EnableWebCore`、Filter/AOP/自动装配 | `mcdull-framework-starter-web` |
| actuator、spring-boot-admin、javamelody | `mcdull-framework-starter-monitor` |

### 日期序列化的归属

`mcdull-framework-json` 不是启动器：没有自动配置，只有 `io.gitee.dqcer.mcdull.framework.json` 下的
注解、序列化器与 `TimeZoneUtil`，依赖面为 base + hutool + jackson（`spring-context` 仅 `provided`，
只服务 `TimeZoneUtil` 的两个 `LocaleContextHolder` 便捷重载）。

它单独成模块的原因是契约包的处境：facade 的 VO 要按登录用户时区输出日期，字段上写着
`@DynamicDateFormat` + `@JsonSerialize(using = DynamicDateSerialize.class)`，而 facade 不该为了两个
注解去依赖整个 starter-web。CRUD 基座反过来走另一条路：它们直接操作 `IRepository`/`BaseMapper`，
属于持久化能力，因此落在 `starter-mysql`。

### 契约包（facade）的枚举

`IEnum`/`EnumVO`/`DictPool` 属于 starter-web，所以契约包里的枚举不实现它：facade 的枚举只写 `code`/`text`
两个 final 字段和自己的静态查找方法（`of(code)`、`textOf(code)`），`system-facade` 因此不需要
`mcdull-framework-starter-web`。代价是 i18n 取名、`@Deprecated` 过滤、字典注册这三项能力只留给 impl 侧枚举。

需要给前端下拉项时，由调用方把枚举解析成 `{code, text}` 再交给 `DynamicFieldBO`，BO 不认识任何字典框架类型。
`system-facade` 的 `extraObj` 输出形状不变，仍与 `EnumVO` 序列化结果逐字段一致。

### 鉴权闸门的装配

starter-security 自带装配入口（`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
→ `AuthAutoConfiguration`），把 jar 放到 classpath 即完成接入，消费方不需要 `@Import`：

- `AuthSessionFilter`（order=3）：按路径选择 `StpKit.ADMIN/APP_USER/DEFAULT` 会话并回填 `UnifySession`，
  未登录时渲染 401。它依赖 servlet，非 servlet 应用由 `@ConditionalOnWebApplication(type = SERVLET)` 跳过。
- `SaTokenExceptionAdvice`：处理过滤器链内抛出、由 MVC 接管的 `SaTokenException`。
- `AuditAspect`：把 `@SaCheckEL` 校验到的权限码写进会话；注解本身的拦截由 `sa-token-spring-el` 完成。
- 白名单仍读 `mcdull.gateway.no-auth`（`McdullProperties`），语义与迁移前一致；`mcdull-devtools-portal`
  配的是 `/**` 全量放行，因此它只需 `cn.dev33:sa-token-core` 提供 `@SaIgnore` 注解，不需要闸门。

web 的 `HttpTraceLogFilter`（order=2）只做 traceId、`UnifySession` 骨架与数据源路由，闸门晚一层执行，
所以它是在 web 建好的会话上补登录态字段。

### web 与能力启动器之间的扩展点

`mcdull-framework-spi` 是跨 starter 的契约层，只放接口，不依赖任何框架模块与三方库。
web 通过它反向解耦，而不是直接依赖能力实现：

- `RequestDataRouting`：按请求切换数据源。`SwitchableDataSource` 继承该接口，
  由 starter-mysql 提供实现。容器内没有实现时，web 退化为 `RequestDataRouting.NOOP`，
  因此不接数据库的应用同样可以使用 `@EnableWebCore`。
- `AsyncContextPropagator`：线程池/异步任务的上下文传播契约。web 自带 MDC 与登录态两个实现，
  starter-mysql 通过 `@EnableDynamicDataSource` 追加 `DataSourceContextPropagator`，starter-tag 追加
  `EnvTagContextPropagator`。新增一类上下文只需再贡献一个 `AsyncContextPropagator` bean，无需改动
  `AsyncContextSupport` 与 `ThreadPoolConfig`。
- `ThreadPoolConfig` 另外收集的 `List<TaskDecorator>` 只用于改写任务本身（包装异常、埋点计数一类），
  不要再往里塞上下文传递逻辑：传播一律走 `AsyncContextPropagator`，捕获-回放-还原由引擎统一做。
- `DynamicLocaleMessageSource`：国际化文案取值契约，实现（依赖 `MessageSource`/`LocaleContextHolder`）
  留在 starter-web，鉴权等能力模块按接口注入；拿不到实现时回落成文案 key 本身。

同时注意 bean 装配入口的归属已经随能力一起迁移：

- `ConcurrentRateLimiter` 的 Redis 实现由 `@EnableCache` 注册（原先挂在 `@EnableWebCore` 上），
  接口本身位于 `mcdull-framework-base` 的 `base.ratelimit` 包。
- `RedisJmxAdapter` 由 `@EnableCache` 注册，不再属于 `@EnableWebCore`。
- 不使用 Redis 的应用可自行注册一个 `ConcurrentRateLimiter` 空实现，
  参考 `mcdull-devtools-portal` 的 `ToolsAutoConfiguration`。
