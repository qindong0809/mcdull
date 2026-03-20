# mcdull-framework-starter-security

mcdull-framework-starter-security 是 MCDull 框架的安全认证模块，基于 Sa-Token 实现，提供了完整的权限认证和安全管理功能。

## 项目简介

mcdull-framework-starter-security 是 MCDull 框架的安全认证组件，为应用提供了完整的权限认证和安全管理能力。该模块集成了 Sa-Token、Spring Security Crypto 等，支持登录认证、权限控制、会话管理等功能。

## 功能特性

- 集成 Sa-Token，提供完整的权限认证框架
- 支持登录认证和会话管理
- 支持基于角色和权限的访问控制
- 支持 Redis 存储会话信息
- 集成 Spring Security Crypto，提供密码加密功能
- 支持注解式权限控制
- 与 Spring 生态无缝集成

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| sa-token-spring-boot3-starter | - | Sa-Token Spring Boot 3 启动器 |
| sa-token-spring-el | - | Sa-Token Spring EL 支持 |
| sa-token-redis-jackson | - | Sa-Token Redis 存储（使用 Jackson 序列化） |
| spring-security-crypto | - | Spring Security 加密模块 |
| commons-pool2 | - | 连接池 |
| spring-webmvc | provided | Spring Web MVC |
| mcdull-framework-base | provided | MCDull 框架基础模块 |

## 项目结构

```
mcdull-framework-starter-security/
├── README.md          # 项目说明文档
└── pom.xml            # Maven 配置文件
```

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-security</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 配置示例

在 `application.yml` 或 `application.properties` 文件中添加安全配置：

```yaml
sa-token:
  # token 名称（同时也是 cookie 名称）
  token-name: Authorization
  # token 有效期（默认30天，单位秒）
  timeout: 2592000
  # token 临时有效期（默认30分钟，单位秒）
  activity-timeout: 1800
  # 是否允许同一账号多地同时登录（默认 false）
  is-concurrent: false
  # 在多人登录同一账号时，是否共用一个 token（默认 false）
  is-share: false
  # token 风格（默认 uuid）
  token-style: uuid
  # 是否输出操作日志（默认 false）
  is-log: false

# Redis 配置（用于存储会话信息）
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
```

### 登录认证

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDTO loginDTO) {
        // 验证用户名和密码
        User user = userService.validateUser(loginDTO.getUsername(), loginDTO.getPassword());
        if (user == null) {
            return R.fail("用户名或密码错误");
        }
        
        // 登录并生成 token
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        return R.ok(token);
    }

    @PostMapping("/logout")
    public R<Boolean> logout() {
        StpUtil.logout();
        return R.ok(true);
    }
}
```

### 权限控制

#### 注解式权限控制

```java
@RestController
@RequestMapping("/user")
@SaCheckLogin  // 需要登录才能访问
public class UserController {

    @GetMapping("/info")
    public R<UserVO> getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        // 获取用户信息
        return R.ok(userService.getUserById(userId));
    }

    @PostMapping("/add")
    @SaCheckPermission("user:add")  // 需要 user:add 权限
    public R<Boolean> addUser(@RequestBody UserDTO userDTO) {
        return R.ok(userService.saveUser(userDTO));
    }

    @PostMapping("/update")
    @SaCheckRole("admin")  // 需要 admin 角色
    public R<Boolean> updateUser(@RequestBody UserDTO userDTO) {
        return R.ok(userService.updateUser(userDTO));
    }
}
```

#### 编程式权限控制

```java
@Service
public class UserService {

    public void deleteUser(Long id) {
        // 检查是否具有删除用户权限
        if (!StpUtil.hasPermission("user:delete")) {
            throw new RuntimeException("无权限删除用户");
        }
        // 执行删除操作
        userRepository.deleteById(id);
    }
}
```

## 核心功能

### 1. 登录认证

支持用户名密码登录、第三方登录等多种登录方式，生成和管理 token。

### 2. 权限控制

支持基于角色和权限的访问控制，可通过注解或编程方式进行权限检查。

### 3. 会话管理

支持会话的创建、管理和销毁，可存储在 Redis 中实现分布式会话。

### 4. 密码加密

集成 Spring Security Crypto，提供密码加密和验证功能。

### 5. 注解式权限控制

提供 `@SaCheckLogin`、`@SaCheckRole`、`@SaCheckPermission` 等注解，方便进行权限控制。

### 6. 异常处理

提供统一的权限异常处理机制，返回标准化的错误信息。

## 注意事项

- 确保 Redis 服务已启动并可访问（如果使用 Redis 存储会话）
- 合理设计权限体系，避免权限过度或不足
- 生产环境中，建议配置适当的 token 有效期和安全参数
- 对于敏感操作，建议使用二次认证或其他额外的安全措施

