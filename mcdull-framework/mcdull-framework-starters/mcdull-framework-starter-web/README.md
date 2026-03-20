# mcdull-framework-starter-web

mcdull-framework-starter-web 是 MCDull 框架的 Web 启动器模块，整合了多个核心模块，为 Spring Boot Web 应用提供完整的功能支持。

## 项目简介

mcdull-framework-starter-web 是 MCDull 框架的 Web 启动器组件，整合了框架的多个核心模块，为 Spring Boot Web 应用提供完整的功能支持。该模块集成了安全认证、API 文档、缓存、数据库访问、文件存储、监控等功能，简化了 Web 应用的开发和配置。

## 功能特性

- 整合多个核心模块，提供一站式 Web 应用解决方案
- 集成安全认证功能（基于 Sa-Token）
- 集成 API 文档生成功能（基于 Knife4j）
- 集成 Redis 缓存功能
- 集成 MySQL 数据库访问功能（基于 MyBatis-Plus）
- 集成文件存储功能（基于 OSS）
- 集成服务调用功能（基于 Feign）
- 集成应用监控功能
- 提供统一的异常处理和响应格式
- 支持国际化(多语言、多时区)
- 个性化日期格式、字符串预处理、JMX 支持
- 支持当前Git最新提交信息

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| mcdull-framework-starter-oss | - | 对象存储模块 |
| mcdull-framework-starter-doc | - | API 文档模块 |
| mcdull-framework-starter-security | - | 安全认证模块 |
| mcdull-framework-base | - | 基础模块 |
| mcdull-framework-starter-redis | - | Redis 缓存模块 |
| mcdull-framework-starter-feign | provided | 服务调用模块 |
| mcdull-framework-config | - | 配置模块 |
| mcdull-framework-starter-mysql | - | MySQL 数据库模块 |
| mcdull-framework-starter-monitor | - | 监控模块 |
| hutool-json | - | JSON 工具库 |
| log4j-over-slf4j | - | 日志桥接 |
| jcl-over-slf4j | - | 日志桥接 |
| jdframe | - | 工具框架 |

## 项目结构

```
mcdull-framework-starter-web/
├── src/
│   └── main/
│       └── resources/
│           └── i18n/                  # 国际化资源文件
│               ├── framework.properties
│               ├── framework_en_US.properties
│               └── framework_zh_CN.properties
├── README.md          # 项目说明文档
└── pom.xml            # Maven 配置文件
```

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-web</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 配置示例

在 `application.yml` 或 `application.properties` 文件中添加配置：

```yaml
# 服务器配置
server:
  port: 8080
  servlet:
    context-path: /api

# 数据源配置
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: password

# Redis 配置
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0

# Sa-Token 配置
sa-token:
  token-name: Authorization
  timeout: 2592000
  activity-timeout: 1800
  is-concurrent: false
  is-share: false
  token-style: uuid
  is-log: false

# Knife4j 配置
knife4j:
  enable: true
  setting:
    language: zh_cn

# 国际化配置
spring:
  messages:
    basename: i18n/message
    encoding: UTF-8
    fallback-to-system-locale: false
```

### 启动类配置

```java
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.feign")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 控制器示例

```java
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户列表", description = "分页获取用户列表")
    @GetMapping("/list")
    @SaCheckLogin
    public R<PageVO<UserVO>> list(@Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                 @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        return R.ok(userService.list(page, size));
    }

    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping
    @SaCheckPermission("user:add")
    public R<Boolean> create(@Valid @RequestBody UserDTO userDTO) {
        return R.ok(userService.saveUser(userDTO));
    }

    @Operation(summary = "获取用户详情", description = "根据ID获取用户详情")
    @GetMapping("/{id}")
    @SaCheckLogin
    public R<UserVO> getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        return R.ok(userService.getUserById(id));
    }

    @Operation(summary = "更新用户", description = "更新用户信息")
    @PutMapping
    @SaCheckPermission("user:update")
    public R<Boolean> update(@Valid @RequestBody UserDTO userDTO) {
        return R.ok(userService.updateUser(userDTO));
    }

    @Operation(summary = "删除用户", description = "根据ID删除用户")
    @DeleteMapping("/{id}")
    @SaCheckPermission("user:delete")
    public R<Boolean> delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        return R.ok(userService.deleteUser(id));
    }
}
```

## 核心功能

### 1. 统一响应格式

提供统一的响应格式 `R<T>`，包含状态码、消息和数据，简化接口返回值处理。

### 2. 统一异常处理

提供统一的异常处理机制，捕获并处理各类异常，返回标准化的错误信息。

### 3. 国际化支持

支持多语言国际化，可根据请求头自动切换语言。

### 4. 安全认证

集成 Sa-Token，提供完整的权限认证和会话管理功能。

### 5. API 文档

集成 Knife4j，自动生成 API 文档，提供交互式的文档界面。

### 6. 缓存支持

集成 Redis 缓存，支持注解式缓存和分布式锁。

### 7. 数据库访问

集成 MyBatis-Plus，提供增强的 ORM 功能，简化数据库操作。

### 8. 文件存储

集成 OSS 模块，支持文件的上传、下载和管理。

### 9. 服务调用

集成 Feign，支持声明式的服务间调用。

### 10. 应用监控

集成监控模块，提供应用的健康状态和性能监控。

## 注意事项

- 确保配置了正确的数据库、Redis 等服务连接信息
- 合理设计权限体系，避免权限过度或不足
- 生产环境中，建议配置适当的安全参数和监控设置
- 对于大型应用，建议根据实际需求选择性引入模块，避免不必要的依赖
