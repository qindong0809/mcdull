# mcdull-framework-starter-doc

mcdull-framework-starter-doc 是 MCDull 框架的 API 文档生成模块，基于 Knife4j 和 Spring OpenAPI 实现，提供了自动化的 API 文档生成和展示功能。

## 项目简介

mcdull-framework-starter-doc 是 MCDull 框架的 API 文档生成组件，集成了 Knife4j 和 Spring OpenAPI，为 Spring Boot 应用提供自动化的 API 文档生成和展示功能。该模块支持 OpenAPI 3.0 规范，提供了美观、交互式的 API 文档界面。

## 功能特性

- 基于 OpenAPI 3.0 规范生成 API 文档
- 集成 Knife4j 提供美观的文档界面
- 支持接口权限配置
- 支持枚举类型的文档展示
- 自动扫描并生成 API 文档
- 支持自定义文档配置

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| knife4j-openapi3-jakarta-spring-boot-starter | - | Knife4j OpenAPI 3.0 集成 |
| springdoc-openapi-ui | - | Spring OpenAPI 实现 |
| mcdull-framework-base | provided | MCDull 框架基础模块 |
| hutool-json | provided | JSON 工具库 |
| sa-token-core | provided | 权限认证库 |

## 项目结构

```
mcdull-framework-starter-doc/
├── src/
│   └── main/
│       ├── java/io/gitee/dqcer/mcdull/framework/doc/
│       │   ├── Knife4jOperationCustomizer.java      # Knife4j 操作自定义器
│       │   ├── OpenApiConfig.java                    # OpenAPI 配置
│       │   ├── PermissionOperationCustomizer.java    # 权限操作自定义器
│       │   └── SchemaEnumPropertyCustomizer.java     # 模式枚举属性自定义器
│       └── resources/META-INF/spring/
│           └── org.springframework.boot.autoconfigure.AutoConfiguration.imports  # 自动配置导入
├── README.md          # 项目说明文档
└── pom.xml            # Maven 配置文件
```

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-doc</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 配置示例

在 `application.yml` 或 `application.properties` 文件中添加配置：

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html

knife4j:
  enable: true
  setting:
    language: zh_cn
```

### 访问文档

启动应用后，可以通过以下地址访问 API 文档：

- Knife4j 文档界面：`http://localhost:8090/doc.html`

## 核心功能

### 1. 接口权限配置

通过 `PermissionOperationCustomizer` 类，可以为 API 接口添加权限信息，方便前端和其他开发者了解接口的访问权限。

### 2. 枚举类型展示

通过 `SchemaEnumPropertyCustomizer` 类，可以在 API 文档中展示枚举类型的详细信息，包括枚举值和描述。

### 3. 操作自定义

通过 `Knife4jOperationCustomizer` 类，可以自定义 API 操作的展示信息，包括标签、描述等。

### 4. 全局配置

通过 `OpenApiConfig` 类，可以配置全局的 OpenAPI 信息，包括文档标题、描述、版本等。

## 注解使用

### 接口注解

```java
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Operation(summary = "获取用户列表", description = "分页获取用户列表")
    @GetMapping("/list")
    public R<PageVO<UserVO>> list(@Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                 @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        // 实现逻辑
    }

    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping
    public R<Boolean> create(@Valid @RequestBody UserDTO userDTO) {
        // 实现逻辑
    }
}
```

### 模型注解

```java
@Data
@Schema(name = "UserDTO", description = "用户数据传输对象")
public class UserDTO {

    @Schema(description = "用户名", required = true)
    private String username;

    @Schema(description = "密码", required = true)
    private String password;

    @Schema(description = "用户状态", enumAsRef = true)
    private UserStatusEnum status;
}

@Getter
public enum UserStatusEnum {
    @Schema(description = "启用")
    ENABLED(1, "启用"),
    @Schema(description = "禁用")
    DISABLED(0, "禁用");

    private final Integer value;
    private final String desc;

    UserStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
```