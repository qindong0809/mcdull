# mcdull-framework-config

mcdull-framework-config 是 MCDull 框架的配置管理模块，提供了统一的配置管理和自动配置功能。

## 项目简介

mcdull-framework-config 是 MCDull 框架的配置管理组件，负责集中管理框架的各项配置，支持 Spring Boot 自动配置，为框架的其他模块提供配置支持。

## 功能特性

- 提供统一的配置管理
- 支持 Spring Boot 自动配置
- 集成多种配置项，包括网关、线程池、对象存储、邮件等
- 支持通过配置文件自定义配置

## 核心配置项

| 配置项 | 说明 | 默认值 |
|-------|------|-------|
| mcdull.gateway | 网关配置 | - |
| mcdull.threadPool | 线程池配置 | - |
| mcdull.oss | 对象存储配置 | - |
| mcdull.mail | 邮件配置 | - |
| spring.application.name | 应用名称 | unknown |

## 项目结构

```
mcdull-framework-config/
├── src/
│   └── main/
│       ├── java/io/gitee/dqcer/mcdull/framework/config/
│       │   ├── McdullConfig.java                 # 主配置类
│       │   └── properties/
│       │       └── McdullProperties.java         # 配置属性类
│       └── resources/META-INF/
│           └── spring.factories                  # Spring 自动配置文件
├── README.md          # 项目说明文档
└── pom.xml            # Maven 配置文件
```

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-config</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 配置示例

在 `application.yml` 或 `application.properties` 文件中添加配置：

```yaml
mcdull:
  gateway:
    # 网关配置
  threadPool:
    # 线程池配置
  oss:
    # 对象存储配置
  mail:
    # 邮件配置

spring:
  application:
    name: your-application-name
```

### 自动配置

该模块通过 Spring Boot 的自动配置机制，会自动加载配置类，无需手动配置。

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| spring-boot-configuration-processor | - | 配置处理器（可选） |
| spring-boot | provided | Spring Boot 核心库 |
| mcdull-framework-base | provided | MCDull 框架基础模块 |

