# Mcdull 项目

## 项目简介

Mcdull 是一个基于 Spring Cloud 的企业级应用开发框架，提供了完整的微服务架构解决方案。该项目旨在简化企业应用的开发、部署和维护，为开发者提供一套标准化、可扩展的开发框架。

## 技术栈

- **基础框架**: Spring Boot 2.x, Spring Cloud
- **服务注册与发现**: Nacos
- **配置中心**: Nacos
- **API 网关**: Spring Cloud Gateway
- **服务调用**: Feign
- **熔断器**: Sentinel
- **数据库**: MySQL, MongoDB, Redis
- **消息队列**: RocketMQ
- **认证授权**: Spring Security
- **流程引擎**: Warm Flow
- **构建工具**: Maven
- **CI/CD**: GitHub Actions

## 亮点功能

### 1. 字段级别的日志审计稽查

提供了细粒度的字段级别审计功能，能够追踪实体字段的变更历史，记录变更前后的值，支持对业务操作的完整审计。

- 通过 `@AuditDescription` 注解标记需要审计的字段
- 自动记录字段变更前后的值
- 支持自定义审计描述和排序
- 提供完整的审计记录查询接口

### 2. 动态数据源

实现了灵活的动态数据源切换功能，支持在运行时根据业务需求切换不同的数据源。

- 通过 `@DynamicDataSource` 注解指定数据源
- 支持多数据源配置和管理
- 提供数据源切换的切面处理
- 支持全局和局部数据源切换

### 3. 国际化和多语言

内置了完善的国际化支持，支持多语言环境和动态语言切换。

- 支持多语言资源文件
- 提供动态语言切换功能
- 支持基于用户偏好的语言设置
- 内置中英文语言包

### 4. 多时区和自定义用户日期格式

支持多时区处理和用户自定义日期格式，满足不同地区和用户的时间显示需求。

- 支持多时区设置
- 基于用户偏好的日期格式
- 动态日期序列化和反序列化
- 时区自动转换

### 5. JMX 口子

提供了丰富的 JMX 接口，用于系统监控和管理。

- 线程池监控
- 数据库连接池监控
- Redis 连接监控
- 系统运行状态监控

### 6. 日志全链路

实现了完整的日志全链路追踪功能，支持请求的全链路跟踪和分析。

- 请求链路追踪
- 日志上下文传递
- 分布式追踪支持
- 性能分析和监控

## 项目架构

Mcdull 采用分层架构设计，主要包含以下模块：

1. **框架层 (mcdull-framework)**：提供基础组件和通用功能
2. **业务层 (mcdull-bussiness)**：包含具体业务逻辑实现
3. **支持层 (mcdull-support)**：提供网关、监控等支持服务
4. **文档层 (doc)**：项目文档和API说明
5. **SQL层 (sql)**：数据库脚本和初始化数据

## 快速开始

### 环境要求

- JDK 1.8+ 
- Maven 3.6+
- MySQL 5.7+
- Redis 5.0+
- Nacos 2.0+

### 安装与运行

1. **克隆项目**

```bash
git clone https://gitee.com/dqcer/mcdull.git
cd mcdull
```

2. **编译项目**

```bash
mvn clean install -DskipTests
```

3. **初始化数据库**

执行 `sql` 目录下的初始化脚本：
- `common.sql` - 通用表结构
- `3.0.sql` - 系统核心表结构
- `3.0_workflow_1.7.4.sql` - 工作流表结构

4. **配置 Nacos**

启动 Nacos 服务，并导入配置文件：
- 配置文件位置：`doc/nacos_config_export_20240421173102.zip`

5. **启动服务**

按以下顺序启动服务：
1. 配置中心 (Nacos)
2. 网关服务 (mcdull-gateway)
3. 业务服务 (mcdull-business-*)
4. 支持服务 (mcdull-support-*)

## 模块说明

### 框架层 (mcdull-framework)

- **mcdull-framework-dependencies**: 依赖管理
- **mcdull-framework-base**: 基础组件
- **mcdull-framework-config**: 配置管理
- **mcdull-framework-agent**: 服务代理和监控
- **mcdull-framework-enforcer**: 架构约束
- **mcdull-framework-starters**: 各种功能的启动器
  - mcdull-framework-starter-doc: 文档生成
  - mcdull-framework-starter-feign: 服务调用
  - mcdull-framework-starter-flow: 工作流
  - mcdull-framework-starter-mongodb: MongoDB 集成
  - mcdull-framework-starter-monitor: 监控
  - mcdull-framework-starter-mysql: MySQL 集成
  - mcdull-framework-starter-nacos: Nacos 集成
  - mcdull-framework-starter-oss: 对象存储
  - mcdull-framework-starter-redis: Redis 集成
  - mcdull-framework-starter-security: 安全认证
  - mcdull-framework-starter-web: Web 基础

### 业务层 (mcdull-bussiness)

- **mcdull-business-common**: 业务通用组件
- **mcdull-business-system**: 系统管理业务
- **mcdull-business-blaze**: 业务处理模块
- **mcdull-business-workflow**: 工作流业务
- **mcdull-business-demo**: 示例业务

### 支持层 (mcdull-support)

- **mcdull-gateway**: API 网关
- **mcdull-mdc**: 元数据中心
- **mcdull-monitor**: 监控服务
- **mcdull-tools**: 开发工具
- **mcdull-ai**: AI 相关功能

## 开发指南

### 代码规范

- 遵循 Java 编码规范
- 使用 Lombok 简化代码
- 统一异常处理
- 统一日志管理

### 开发流程

1. 从 `develop` 分支创建功能分支
2. 完成功能开发和测试
3. 提交代码并发起 Pull Request
4. 代码审查通过后合并到 `develop` 分支
5. 定期从 `develop` 分支发布到 `master` 分支

## 部署说明

### 本地开发环境

- 开发工具：IntelliJ IDEA 或 Eclipse
- 运行模式：使用 `application-dev.yml` 配置
- 数据库：本地 MySQL 实例
- 缓存：本地 Redis 实例

### 生产环境

- 部署方式：Docker 容器
- 运行模式：使用 `application-prod.yml` 配置
- 数据库：生产 MySQL 集群
- 缓存：生产 Redis 集群
- 服务注册：Nacos 集群

## 更新日志

### 版本 1.0
- 初始版本
- 实现基础业务功能
- 搭建项目框架