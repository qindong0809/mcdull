# mcdull-framework-starter-flow

mcdull-framework-starter-flow 是 MCDull 框架的工作流模块，提供了工作流的定义、执行和管理功能。

## 项目简介

mcdull-framework-starter-flow 是 MCDull 框架的工作流组件，为应用提供了工作流的定义、执行和管理能力。该模块支持流程的配置和执行，帮助开发者快速构建具有工作流功能的应用。

## 功能特性

- 支持工作流的定义和配置
- 提供工作流执行引擎
- 支持流程状态管理
- 集成 Spring 上下文
- 支持事务管理

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| spring-context | provided | Spring 上下文 |
| spring-web | provided | Spring Web |
| spring-tx | provided | Spring 事务 |
| hutool-json | provided | JSON 工具库 |
| jakarta.annotation-api | 2.1.1 | Jakarta 注解 API |
| mcdull-framework-base | provided | MCDull 框架基础模块 |

## 项目结构

```
mcdull-framework-starter-flow/
├── src/
│   └── main/
│       └── resources/
│           ├── META-INF/
│           │   └── spring.factories                  # Spring 自动配置文件
│           └── flow/
│               └── process_flow_simple.json         # 简单流程定义示例
├── README.md          # 项目说明文档
└── pom.xml            # Maven 配置文件
```

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-flow</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 流程定义

在 `resources/flow` 目录下创建流程定义文件，例如 `process_flow_simple.json`：

```json
{
  "id": "simple-flow",
  "name": "简单流程",
  "description": "一个简单的工作流程示例",
  "steps": [
    {
      "id": "step1",
      "name": "第一步",
      "description": "流程的第一步",
      "type": "task",
      "next": "step2"
    },
    {
      "id": "step2",
      "name": "第二步",
      "description": "流程的第二步",
      "type": "task",
      "next": "end"
    },
    {
      "id": "end",
      "name": "结束",
      "description": "流程结束",
      "type": "end"
    }
  ]
}
```

### 流程执行

```java
@Autowired
private FlowEngine flowEngine;

public void executeFlow() {
    // 初始化流程参数
    Map<String, Object> params = new HashMap<>();
    params.put("userId", 123L);
    params.put("action", "submit");
    
    // 执行流程
    FlowResult result = flowEngine.execute("simple-flow", params);
    
    // 处理执行结果
    if (result.isSuccess()) {
        System.out.println("流程执行成功");
    } else {
        System.out.println("流程执行失败: " + result.getErrorMsg());
    }
}
```

## 核心功能

### 1. 流程定义管理

支持通过 JSON 文件定义工作流程，包括流程步骤、条件和转换规则。

### 2. 流程执行引擎

提供流程执行引擎，负责按照流程定义执行工作流，处理流程状态和转换。

### 3. 状态管理

支持流程状态的管理，包括流程的启动、暂停、恢复和终止。

### 4. 事务支持

集成 Spring 事务管理，确保流程执行的原子性和一致性。

## 配置示例

在 `application.yml` 或 `application.properties` 文件中添加配置：

```yaml
mcdull:
  flow:
    enabled: true
    base-path: classpath:flow/
    default-process: simple-flow
```
