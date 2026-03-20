# mcdull-framework-starter-feign

mcdull-framework-starter-feign 是 MCDull 框架的 Feign 服务调用模块，基于 Spring Cloud OpenFeign 实现，提供了微服务间的声明式 HTTP 客户端。

## 项目简介

mcdull-framework-starter-feign 是 MCDull 框架的服务调用组件，集成了 Spring Cloud OpenFeign，为微服务架构提供了声明式的 HTTP 客户端。该模块简化了微服务间的调用方式，支持负载均衡和服务发现。

## 功能特性

- 基于 Spring Cloud OpenFeign 实现声明式服务调用
- 集成负载均衡功能
- 支持 HTTP 客户端配置
- 解决 Feign 调用 GET 请求时的参数问题
- 与 Spring Cloud 生态无缝集成

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| spring-cloud-starter-openfeign | - | Spring Cloud OpenFeign 核心库 |
| feign-httpclient | - | Feign HTTP 客户端 |
| spring-cloud-starter-loadbalancer | - | 负载均衡组件 |
| mcdull-framework-base | provided | MCDull 框架基础模块 |
| jakarta.servlet-api | provided | Servlet API |

## 项目结构

```
mcdull-framework-starter-feign/
├── src/
│   └── main/
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
    <artifactId>mcdull-framework-starter-feign</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 启用 Feign

在 Spring Boot 应用的启动类上添加 `@EnableFeignClients` 注解：

```java
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.feign")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 定义 Feign 客户端

```java
@FeignClient(name = "user-service", path = "/api/user")
public interface UserFeignClient {

    @GetMapping("/list")
    R<PageVO<UserVO>> list(@RequestParam("page") Integer page, @RequestParam("size") Integer size);

    @PostMapping
    R<Boolean> create(@RequestBody UserDTO userDTO);

    @GetMapping("/{id}")
    R<UserVO> getById(@PathVariable("id") Long id);
}
```

### 配置示例

在 `application.yml` 或 `application.properties` 文件中添加配置：

```yaml
feign:
  httpclient:
    enabled: true
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000

spring:
  cloud:
    loadbalancer:
      ribbon:
        enabled: false
```

## 核心功能

### 1. 声明式服务调用

通过注解方式定义服务接口，无需手动实现 HTTP 调用逻辑。

### 2. 负载均衡

集成 Spring Cloud LoadBalancer，支持服务实例的负载均衡。

### 3. HTTP 客户端配置

支持配置 HTTP 客户端参数，如连接超时、读取超时等。

### 4. 解决 GET 请求参数问题

通过集成 feign-httpclient，解决 Feign 调用 GET 请求时 body 有参导致服务端自动转换为 POST 请求的问题。

## 注意事项

- 确保服务提供者已注册到服务注册中心（如 Nacos）
- 服务名必须与注册中心中的服务名一致
- 接口方法的参数和返回值类型必须与服务提供者保持一致

