# mcdull-framework-starter-monitor

mcdull-framework-starter-monitor 是 MCDull 框架的监控模块，集成了 Spring Boot Admin、Spring Boot Actuator、Jolokia 和 JavaMelody，提供了全面的应用监控能力。

## 项目简介

mcdull-framework-starter-monitor 是 MCDull 框架的监控组件，为应用提供了全面的监控能力。该模块集成了多种监控工具，包括 Spring Boot Admin 客户端、Spring Boot Actuator、Jolokia 和 JavaMelody，帮助开发者实时监控应用的运行状态和性能。

## 功能特性

- 集成 Spring Boot Admin 客户端，支持应用状态监控
- 集成 Spring Boot Actuator，提供健康检查、指标监控等功能
- 集成 Jolokia，支持 JMX Bean 管理
- 集成 JavaMelody，提供应用性能监控
- 支持自定义监控指标
- 与 Spring 生态无缝集成

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| spring-boot-admin-starter-client | - | Spring Boot Admin 客户端 |
| spring-boot-starter-actuator | - | Spring Boot Actuator |
| jolokia-support-spring | - | JMX Bean 管理 |
| javamelody-spring-boot-starter | - | 应用性能监控 |

## 项目结构

```
mcdull-framework-starter-monitor/
├── README.md          # 项目说明文档
└── pom.xml            # Maven 配置文件
```

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-monitor</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 配置示例

在 `application.yml` 或 `application.properties` 文件中添加监控配置：

```yaml
spring:
  boot:
    admin:
      client:
        url: http://localhost:8080  # Spring Boot Admin 服务端地址
        instance:
          prefer-ip: true

management:
  endpoints:
    web:
      exposure:
        include: '*'  # 暴露所有端点
  endpoint:
    health:
      show-details: always  # 显示详细的健康信息

javamelody:
  enabled: true  # 启用 JavaMelody
  init-parameters:
    monitoring-path: /monitoring  # JavaMelody 监控路径
```

### 访问监控端点

启动应用后，可以通过以下地址访问监控端点：

- Spring Boot Actuator 端点：`http://localhost:8080/actuator`
- JavaMelody 监控界面：`http://localhost:8080/monitoring`
- Spring Boot Admin 控制台：`http://localhost:8080`（需要部署 Spring Boot Admin 服务端）

## 核心功能

### 1. 应用健康检查

通过 Spring Boot Actuator 提供应用的健康状态检查，包括磁盘空间、数据库连接、缓存等。

### 2. 性能监控

通过 JavaMelody 提供应用的性能监控，包括请求响应时间、SQL 执行时间、内存使用等。

### 3. 应用状态管理

通过 Spring Boot Admin 客户端，将应用注册到 Spring Boot Admin 服务端，实现应用状态的集中管理和监控。

### 4. JMX Bean 管理

通过 Jolokia，支持通过 HTTP 访问和管理 JMX Bean，方便查看和管理应用的内部状态。

### 5. 自定义监控指标

支持通过 Spring Boot Actuator 的 Metrics 功能，添加自定义监控指标，满足特定业务场景的监控需求。

## 注意事项

- 生产环境中，建议配置适当的安全措施，保护监控端点的访问
- 对于 JavaMelody，建议根据实际需求配置监控参数，避免过度监控影响性能
- 对于 Spring Boot Admin，需要部署独立的服务端才能使用其完整功能
- 合理配置 Actuator 端点的暴露范围，避免敏感信息泄露
