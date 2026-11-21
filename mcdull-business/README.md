# mcdull Business Modules

## 概述

本模块包含mcdull框架的所有业务模块，实现具体的业务功能和应用逻辑。

## 模块分类

### Core Business Components（核心业务组件）
提供跨业务模块的共享功能和工具类。

- **mcdull-business-common**: 业务通用组件
  - 文档处理工具（PDF、Excel、Word）
  - 推荐算法引擎
  - 业务工具类和通用DTO
  - 第三方集成工具

### Business Applications（业务应用模块）
实现具体的业务功能模块，每个模块采用标准的三层架构。

- **mcdull-business-system**: 系统管理模块
  - 用户管理、角色权限
  - 系统配置、参数管理
  - 代码生成器
  - 系统监控和日志管理

- **mcdull-business-blaze**: 核心业务模块
  - 具体业务逻辑实现
  - 业务流程处理
  - 数据分析和报表

- **mcdull-business-workflow**: 工作流模块
  - 基于自研starter-flow引擎的流程管理
  - 复杂业务流程定义和执行
  - 流程实例和任务管理
  - 流程监控和审计

### Samples & Examples（示例演示模块）
提供学习和参考的示例代码。

- **mcdull-business-demo**: 可运行的演示模块
  - 演示任务 CRUD：Controller → Service → Manager → Mapper 完整链路
  - facade/provider 分包：契约枚举、DTO/VO、标记接口与校验注解
  - 配置与验证：本地/生产配置模板、SQL 初始化脚本、ArchUnit 架构守护测试
  - 详细接入说明见 [mcdull-business-demo/README.md](mcdull-business-demo/README.md)

## 模块架构

每个业务模块采用标准的分层架构：

```
business-module/
├── module-facade/      # 接口定义层（API契约）
├── module-impl/        # 实现层（业务逻辑）
└── module-bootstrap/   # 启动层（应用入口）
```

### 分层职责

- **facade**: 定义服务接口、DTO和枚举，供其他模块调用；枚举是不实现框架 `IEnum` 的纯 POJO，因此 facade 不依赖 `mcdull-framework-starter-web`
- **impl**: 实现具体的业务逻辑，包含Controller、Service、Manager、DAO等
- **bootstrap**: 应用启动类和配置，用于独立部署

## 设计原则

1. **模块解耦**: 业务模块之间通过facade接口通信，避免impl层直接依赖
2. **分层清晰**: 严格按照Controller → Service → Manager → DAO的分层架构
3. **职责单一**: 每个模块专注于特定的业务领域
4. **可独立部署**: 每个模块都可以独立打包和部署

## 依赖关系

- 业务模块可以依赖其他模块的facade
- 禁止业务模块直接依赖其他模块的impl
- 所有业务模块都可以使用business-common中的工具类
- 通过Feign客户端实现模块间的服务调用

## 注意事项

1. **避免循环依赖**: 设计模块依赖关系时要避免循环引用
2. **接口优先**: 模块间通信优先使用facade接口
3. **配置管理**: 使用Nacos进行集中配置管理；环境配置（含 `application*.yml` 与 `logback*.xml`）统一放在各 `*-bootstrap/src/main/resources`，`*-impl` 只保留 mapper、i18n、模板等静态业务资源
4. **环境命名**: 全链路统一 `local` / `test` / `prod`，`SPRING_PROFILES_ACTIVE` 必填（fail-fast）
5. **事务处理**: 跨模块操作时注意分布式事务处理