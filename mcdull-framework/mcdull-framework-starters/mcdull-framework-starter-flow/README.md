# mcdull-framework-starter-flow

`mcdull-framework-starter-flow` 是进程内的轻量流程编排 Starter。它按 JSON 中声明的节点顺序调用 Spring Bean，适用于登录、校验、数据装配等不需要持久化的短流程。

它**不是**完整工作流引擎：流程实例、人工任务、审批历史和 UI 由 Warm Flow 承担。需要可持久化的审批流时，应使用 `mcdull-business-workflow` / Warm Flow，而不是本模块。

## 能力边界

- 按 `nodeList` 顺序执行节点。
- 节点通过 `@TreeNode(code = "...")` 注册到流程注册表。
- 支持节点级 `catchException` / `finallyException` 回调，以及子节点继承父节点回调。
- `ProcessFlow.run()` 带 `@Transactional(rollbackFor = Exception.class)`。
- 不提供流程实例持久化、人工任务、流程状态机、UI 或动态配置开关。

## 模块职责

| 模块 | 内容 |
|---|---|
| `mcdull-framework-api-flow` | `IProcessFlow`、`Context`、`ProcessHandler`、`@TreeNode` |
| `mcdull-framework-starter-flow` | `ProcessFlow` 实现、JSON 读取、节点注册、自动装配 |

## 核心 API

| API | 说明 |
|---|---|
| `IProcessFlow.run(Context)` | 执行流程，无返回值；结果由业务自定义 Context 承载 |
| `Context.getId()` | 返回流程 id，必须与 JSON 中的 `id` 一致 |
| `ProcessHandler<C>.execute(C)` | 节点业务逻辑，必须实现 |
| `ProcessHandler<C>.endNode()` | 返回 `true` 时提前终止流程，默认 `false` |
| `ProcessHandler<C>.extendParentNodeTryAttr()` | 异常时是否复用父节点的回调，默认 `false` |
| `ProcessHandler<C>.catchException(Exception, C)` | 节点异常回调，默认记录日志 |
| `ProcessHandler<C>.finallyException(ProcessHandler, Exception, C)` | 节点 finally 回调，默认记录日志 |

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-flow</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. 定义流程节点

```java
@TreeNode(code = "user.param.check")
public class ParamCheck implements ProcessHandler<SimpleContext> {

    @Override
    public void execute(SimpleContext context) {
        // 参数校验逻辑
    }
}
```

`code` 是节点在流程定义中的唯一标识，不能重复。

### 3. 定义流程

在 `resources/flow/` 下创建 `process_flow_*.json`。默认扫描路径为 `classpath*:flow/process_flow_*.json`。

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

字段说明：

| 字段 | 说明 |
|---|---|
| `id` | 业务唯一标识，`Context.getId()` 必须返回它 |
| `remark` | 流程描述 |
| `nodeList` | 节点 `code` 的有序列表 |

### 4. 执行流程

```java
@Resource
private IProcessFlow processFlow;

public void executeFlow() {
    SimpleContext context = new SimpleContext();
    context.setId("process_flow_simple");
    processFlow.run(context);
}
```

`SimpleContext` 是业务自定义上下文，实现 `Context` 接口：

```java
public class SimpleContext implements Context {

    private String id;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
```

## 自动装配

自动配置类通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 注册：

```text
io.gitee.dqcer.mcdull.framework.flow.config.ProcessFlowAutoConfiguration
```

它注册 `ProcessFlow`、`ProcessFlowRegistry`、`NodeBeanPostProcessor` 和 `JsonFileProcessDefinitionReader`。当前没有 `mcdull.flow.*` 配置项；流程定义只能通过 classpath 下的 JSON 文件提供。

## 事务与外部副作用

`ProcessFlow.run()` 整体带数据库事务。节点内部如果写 Redis、调用 Sa-Token 登录或发起 Feign 调用，这些外部副作用**不会随数据库事务回滚**。

典型场景：登录节点在事务内调用 `StpUtil.login()` 写 Redis，若后续节点失败，数据库回滚但 Redis 会话已存在。因此：

- 外部副作用尽量放到流程成功后的事件或 after-commit 回调中。
- 如果暂时无法改造，必须为外部副作用设计补偿逻辑。
- 不要在带事务的流程节点中直接吞掉关键异常。

## 与 Warm Flow 的关系

| 能力 | 本模块 | Warm Flow |
|---|---|---|
| 进程内节点编排 | 支持 | 不负责 |
| 流程实例持久化 | 不支持 | 支持 |
| 人工任务 / 审批 | 不支持 | 支持 |
| 流程历史与 UI | 不支持 | 支持 |
| 事务包裹 | `ProcessFlow.run()` 整体事务 | 由业务 Service 控制 |

简单判断：**登录、校验、装配用 starter-flow；审批、任务、历史用 Warm Flow。**
