# mcdull-framework-base

mcdull-framework-base 是 MCDull 框架的基础模块，提供了核心依赖和基础功能支持。

## 项目简介

mcdull-framework-base 是 MCDull 框架的基础组件，为其他模块提供通用的依赖管理和基础功能。该模块包含了框架运行所需的核心依赖，确保各模块间的依赖一致性。

## 功能特性

- 提供统一的依赖管理
- 集成常用工具库
- 支持数据验证
- 提供日志支持

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| mybatis-plus-core | provided | MyBatis-Plus 核心库（按需引入） |
| hibernate-validator | - | 数据验证框架 |
| slf4j-api | - | 日志接口 |
| hutool-core | - | 工具类库 |

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-base</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

