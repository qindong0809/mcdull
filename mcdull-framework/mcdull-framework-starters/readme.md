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

- **mcdull-framework-starter-mysql**: MySQL数据库集成，包括MyBatis Plus配置和连接池
- **mcdull-framework-starter-redis**: Redis缓存集成，支持分布式锁和缓存操作
- **mcdull-framework-starter-mongodb**: MongoDB文档数据库集成

### Integration & Service Starters（集成服务启动器）
处理微服务间通信和外部服务集成。

- **mcdull-framework-starter-nacos**: Nacos服务发现和配置中心集成
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