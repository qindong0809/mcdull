# mcdull-framework-starter-feign

基于 Spring Cloud OpenFeign 深度封装的企业级内部 RPC 调用组件。

支持**微服务分布式部署（HTTP 调用）**与**单体合并部署（JVM 内存直调）**双模无缝切换；提供**内外边界隔离的响应包装 (`InnerResult`)**、**跨服务业务异常原样还原**以及**全链路上下文自动透传**能力。

---

## 核心特性

1. **双模架构零改造切换（`@RpcClient`）**：
  - **微服务模式**：消费方通过 `@EnableFeignClients` 激活 `@RpcClient` 代理，自动走远程 HTTP 通信。
  - **单体合并模式**：合并打包时无需修改任何业务代码，由于 `@RpcClient` 默认 `primary = false`，Spring 自动优先注入标注了 `@Primary` 的本地实现 Bean，走纯 JVM 内存直调。
2. **内外契约严格隔离**：
  - 对外网关/前端接口统一使用 `Result<T>`。
  - 内部服务间 RPC 调用统一使用 `InnerResult<T>`（路径规范 `/inner/**`），从编译期杜绝内外接口混用。
3. **零样板代码拆包（`.unwrap()`）**：
  - 内置 `unwrap()`、`unwrapOrNull()`、`checkOk()` 等实例方法，业务层无需再手动编写 `if (!res.isOk())` 或引入额外的静态解析工具类。
4. **跨服务业务异常原样还原**：
  - 摒弃高风险的 `Exception` 对象直接序列化，改用纯字符串 `exceptionClass` 传递异常元数据。
  - 当上游服务抛出 `BusinessException` 时，下游调用 `.unwrap()` 会自动通过反射还原并原样抛出，支持精准 `try-catch` 业务补偿。
5. **双向全局异常拦截与全场景上下文透传**：
  - `FeignExceptionAdvice` 自动识别 `/inner/**` 内部调用与对外聚合调用，实现多级 RPC 异常无损透传与错误码转换。
  - `FeignConfiguration` 优先从 `UserContextHolder` 提取会话（兼容 `@Async` 异步线程与定时任务），兜底透传 Web 请求头中的 `U_ID`、`T_ID` 与 `TraceId`。

---

## 快速接入

### 1. 引入依赖

* **契约模块（`*-facade`）**：仅需引入 `mcdull-framework-base`（提供 `InnerResult` 与 `BusinessException`）及 `mcdull-framework-api-feign`（内含 `@RpcClient`，仅传递依赖 `spring-cloud-openfeign-core`），保持零重型依赖污染。
* **服务实现与调用模块（`*-impl` / `*-bootstrap`）**：引入本 Starter：

```xml
<dependency>
  <groupId>io.gitee.dqcer</groupId>
  <artifactId>mcdull-framework-starter-feign</artifactId>
  <version>\${revision}</version>
</dependency>
```

---

## 标准开发规范（单层契约继承式）

### 第一步：定义 RPC 契约接口（`*-facade`）

直接标注 `@RpcClient`，**必须指定 `value`（目标服务名）和唯一的 `contextId`**，内部路径统一以 `/inner/` 开头：

```java
package io.gitee.dqcer.mcdull.business.system.facade.api;

import io.gitee.dqcer.mcdull.business.system.facade.constants.SystemConstants;
import io.gitee.dqcer.mcdull.business.system.facade.model.dto.UserSaveDTO;
import io.gitee.dqcer.mcdull.business.system.facade.model.vo.UserDetailVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import org.springframework.web.bind.annotation.*;

@RpcClient(
  value = SystemConstants.SERVICE_NAME,
  contextId = "sysUserApi",
  path = "/inner/user"
)
public interface SysUserApi {

  @GetMapping("/{id}")
  InnerResult<UserDetailVO> getById(@PathVariable("id") Long id);

  @GetMapping("/by-phone")
  InnerResult<UserDetailVO> findByPhone(@RequestParam("phone") String phone);

  @PostMapping("/save")
  InnerResult<Void> save(@RequestBody UserSaveDTO dto);
}
```

### 第二步：上游服务端实现（`*-impl`）

实现类直接 `implements SysUserApi`，并同时标注 **`@Primary`**、**`@RestController`** 与对应的 **`@RequestMapping`**：

```java
package io.gitee.dqcer.mcdull.business.system.provider.web.controller.inner;

import io.gitee.dqcer.mcdull.business.system.facade.api.SysUserApi;
import io.gitee.dqcer.mcdull.business.system.facade.model.dto.UserSaveDTO;
import io.gitee.dqcer.mcdull.business.system.facade.model.vo.UserDetailVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1. @Primary：确保单体合并部署时，消费方优先注入此本地实现（走 JVM 内存直调）
 * 2. @RestController + @RequestMapping：确保微服务拆分部署时，对外暴露标准 HTTP 接口
 */
@Primary
@RestController
@RequestMapping("/inner/user")
public class SysUserInnerController implements SysUserApi {

  @Override
  public InnerResult<UserDetailVO> getById(Long id) {
    return InnerResult.success(userService.getDetail(id));
  }

  @Override
  public InnerResult<UserDetailVO> findByPhone(String phone) {
    return InnerResult.success(userService.findByPhone(phone));
  }

  @Override
  public InnerResult<Void> save(UserSaveDTO dto) {
    userService.save(dto);
    return InnerResult.success();
  }
}
```

---

## 下游业务调用指南

消费方直接注入 `SysUserApi`，根据业务对空值（`null`）的容忍度选择对应的拆包方法：

| 业务场景 | 调用方式 | 行为说明 |
| :--- | :--- | :--- |
| **强制非空查询** | `UserDetailVO user = sysUserApi.getById(id).unwrap();` | 失败时原样抛出上游异常；若 `data == null` 则抛出 `FeignServiceErrorException` |
| **允许为空查询** | `UserDetailVO user = sysUserApi.findByPhone(phone).unwrapOrNull();` | 失败时原样抛出上游异常；正常查询无数据时安全返回 `null` |
| **无返回值写操作** | `sysUserApi.save(dto).checkOk();` | 适用于 `InnerResult<Void>`，仅校验调用是否成功，失败自动抛异常 |
| **函数式链式处理** | `sysUserApi.findByPhone(phone).ifOkPresent(u -> ...);` | 调用成功且对象非空时自动执行消费回调 |

### 业务异常精准捕获示例

```java
try {
UserDetailVO user = sysUserApi.getById(userId).unwrap();
} catch (BusinessException e) {
  // 无论是远程 HTTP 调用还是单体本地直调，均可直接 catch 到上游抛出的具体业务异常及错误码
  log.warn("业务校验未通过, code={}, msg={}", e.getCode(), e.getMessage());
  }
```

---

## 注意事项

1. **必须配置 `contextId`**：同一个微服务（`value` 相同）下的多个 `@RpcClient` 接口，必须指定各自唯一的 `contextId`，否则远程部署启动时会抛出 `BeanDefinitionOverrideException`。
2. **HTTP 状态码约定**：上游 `/inner/**` 接口发生业务异常时，统一通过 `FeignExceptionAdvice` 拦截并返回 **HTTP `200 OK` + `InnerResult.error(...)`**，确保客户端走正常的业务异常还原流程；非 `2xx` 响应（如 `404`、`503`）将直接由 `FeignErrorDecoder` 判定为底层协议异常。
3. **日志级别隔离**：框架已默认根据 `spring.profiles.active` 自动切换 Feign 日志级别（`prod` 环境下为 `NONE`，非生产环境为 `BASIC`），避免大报文刷满生产磁盘或泄露敏感请求头。
