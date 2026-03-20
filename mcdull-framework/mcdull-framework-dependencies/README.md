# mcdull-framework-dependencies

mcdull-framework-dependencies 是 MCDull 框架的依赖管理模块，负责统一管理框架中所有模块的依赖版本。

## 项目简介

mcdull-framework-dependencies 是 MCDull 框架的依赖管理组件，采用 Maven 的依赖管理机制，集中管理框架中所有模块的依赖版本，确保依赖的一致性和兼容性。该模块作为父 POM 项目，为整个框架提供统一的依赖版本控制。

## 功能特性

- 统一管理所有依赖版本
- 集中配置 Maven 插件版本
- 支持多环境配置（开发、测试、预发布、生产）
- 集成常用的第三方库和框架
- 提供完整的依赖管理体系

## 核心依赖管理

| 类别 | 依赖 | 版本 |
|------|------|------|
| 基础框架 | Spring Boot | 3.2.4 |
| 微服务 | Spring Cloud | 2023.0.1 |
| 微服务生态 | Spring Cloud Alibaba | 2023.0.1.0 |
| 监控 | Spring Boot Admin | 3.0.4 |
| ORM | MyBatis-Plus | 3.5.10.1 |
| 数据库 | MySQL | 8.3.0 |
| 验证 | Hibernate Validator | 6.2.5.Final |
| API文档 | Knife4j | 4.5.0 |
| JSON | Fastjson | 1.2.83 |
| 缓存 | Redisson | 3.43.0 |
| 工具库 | Hutool | 5.8.43 |
| 架构测试 | ArchUnit | 1.0.1 |
| 数据源 | Druid | 1.2.23 |
| 验证码 | Kaptcha | 2.3.3 |
| Excel处理 | EasyExcel | 4.0.1 |
| 文档处理 | POI | 4.1.2 |
| PDF处理 | iText7 | 7.2.5 |
| 权限认证 | Sa-Token | 1.42.0 |
| 系统信息 | OSHI | 6.1.4 |
| JMX监控 | Jolokia | 2.2.9 |
| 文件存储 | X-File-Storage | 2.2.1 |
| 工具框架 | JDFrame | 0.2.0 |
| 性能监控 | JavaMelody | 2.5.0 |

## 项目结构

```
mcdull-framework-dependencies/
├── README.md          # 项目说明文档
└── pom.xml            # Maven 依赖管理配置文件
```

## 如何使用

### 作为父项目使用

在项目的 `pom.xml` 文件中继承此依赖管理模块：

```xml
<parent>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-dependencies</artifactId>
    <version>${mcdull.version}</version>
    <relativePath/>
</parent>
```

### 作为依赖管理使用

在项目的 `pom.xml` 文件中导入此依赖管理模块：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.gitee.dqcer</groupId>
            <artifactId>mcdull-framework-dependencies</artifactId>
            <version>${mcdull.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 环境配置

模块支持多种环境配置，可通过 Maven  profiles 激活不同环境：

- `dev`：开发环境（默认）
- `test`：测试环境
- `pre`：预发布环境
- `prod`：生产环境

## 版本管理

模块使用 `${mcdull.version}` 属性统一管理版本号，确保所有模块使用相同的版本。

