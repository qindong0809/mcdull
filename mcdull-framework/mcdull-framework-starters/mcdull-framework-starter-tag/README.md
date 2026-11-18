# `mcdull-framework-starter-tag` 使用说明书

**`mcdull-framework-starter-tag` 是基于 Spring Cloud LoadBalancer 与全链路上下文透传（TTL）实现的轻量级流量染色与环境隔离 Starter。** 它通过 `HTTP Header (X-Env-Tag)` 与 `Nacos Metadata (env-tag)` 的联动，在零入侵业务代码的前提下，实现开发联调隔离、多特性并行测试（泳道隔离）以及生产环境金丝雀灰度发布。

---

## 一、 核心能力与适用场景

*   **本地开发联调隔离**：多位开发者共用一套开发 Nacos 时，本地启动的微服务自动携带本机用户名标签；前端或 Postman 请求头带 `X-Env-Tag: 用户名` 即可精准命中本地实例，未修改的其他微服务自动兜底走公共开发服务器。
*   **多特性分支并行测试（测试泳道）**：仅需为改动的 1~2 个微服务部署带特性标签（如 `ENV_TAG=feat-pay`）的容器，其余几十个未改动服务直接复用基准测试环境，无需克隆整套测试集群。
*   **生产环境安全防护与灰度**：支持在网关入口开启请求头清洗过滤器，自动剥离外部客户端恶意传入的 `X-Env-Tag` 请求头，防止越权探测未公开的灰度接口。
*   **全链路自动透传**：支持 **WebFlux 网关 -> Servlet 微服务 -> OpenFeign 下游调用 -> `@Async` 异步线程池** 的完整上下文接力（采用“捕获-替换-还原”机制，完美兼容 `CallerRunsPolicy` 拒绝策略）。

---

## 二、 快速接入指南

### 1. 引入 Maven 依赖

在 **API 网关（`mcdull-gateway`）** 以及需要参与全链路路由的 **业务微服务** 的 `pom.xml` 中引入本组件：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-tag</artifactId>
</dependency>
```

*(注：组件已通过条件注解自动隔离 WebFlux 与 Servlet 环境，网关与业务微服务共用同一个依赖，无需额外配置或添加启动类注解。)*

### 2. 配置实例元数据（打标）

#### ① 本地开发环境（`application-dev.yml`）
建议在开发配置文件中开启自动打标，默认读取环境变量 `ENV_TAG` 或当前操作系统登录用户名：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        metadata:
          env-tag: ${ENV_TAG:${user.name}}
```

#### ② 公共基准服务器 / 容器部署
公共开发服、基准测试服及生产正式节点**不要设置 `ENV_TAG`（或将其置为空字符串 `""`）**，使其作为无标签的“公共基准兜底池”：

```bash
docker run -d --name mcdull-gateway -p 9090:9090 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e ENV_TAG="" \
  mcdull-gateway:latest
```

#### ③ 特性泳道或灰度容器部署
为特定特性分支或灰度验证节点注入专属标签：

```bash
docker run -d --name system-bootstrap-feat-pay -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=test \
  -e ENV_TAG=feat-pay \
  system-bootstrap:feat-pay
```

---

## 三、 配置项说明（YAML）

支持在 `application.yml` 或 Nacos 配置中心通过 `mcdull.tag` 前缀进行精细化控制：

```yaml
mcdull:
  tag:
    # 是否启用全链路标签路由（默认 true）
    enabled: true
    # 严格模式（默认 false）：
    # false - 找不到目标标签实例时，自动降级路由到无标签的公共基准实例（适合开发联调与测试泳道）
    # true  - 找不到目标标签实例时，直接阻断并返回 503，绝不回流公共实例（适合生产强隔离灰度）
    strict: false
    # 网关入口防伪造清洗开关（默认 false，强烈建议在网关的 application-prod.yml 中设为 true）：
    # 开启后，网关 WebFilter 会自动剥离外部客户端传入的 X-Env-Tag 请求头
    clean-header: false
```

---

## 四、 路由选址漏斗规则

当网关或微服务通过 `lb://服务名` 发起调用时，`TagLoadBalancer` 严格按以下三级优先级选择目标实例：

1.  **第一级（精准匹配）**：提取请求头 `X-Env-Tag`（或线程上下文中的标签）。若存在且不为 `default`，则从注册中心筛选 `metadata.env-tag` 相同的健康实例进行负载均衡。
2.  **第二级（基准兜底）**：若未匹配到目标标签实例（且 `strict=false`），自动降级选择 **`metadata.env-tag` 为空** 的公共基准实例。
3.  **第三级（紧急容灾）**：极端情况下，若连无标签的基准实例都不存在（例如某新服务仅在特性分支首次上线），则从当前所有健康实例中兜底选择，确保调用链不断。

---

## 五、 调试技巧与注意事项

*   **临时跳过本地实例（防串台开关）**：开发者本地启动了微服务，但临时想直接调用公共开发服务器的接口进行对比测试时，无需关闭本地 IDEA，只需在请求头中传入保留字 **`X-Env-Tag: default`**，即可强制路由至无标签的公共基准实例。
*   **异步线程池联动**：本组件会自动向 Spring 容器注册名为 `envTagTaskDecorator` 的 `TaskDecorator` Bean。配合 `mcdull-framework-starter-web` 中的 `ThreadPoolConfig`，所有提交至 `@Async` 或 `threadPoolTaskExecutor` 的异步任务均可自动继承并还原 `ENV_TAG` 上下文。
*   **生产环境安全限制**：建议在生产网关配置中开启 `mcdull.tag.clean-header=true`（或激活 `prod` Profile），确保外部请求无法伪造染色标签；若生产环境需实施内部员工灰度，请在网关鉴权过滤器验证身份后，通过 `ServerHttpRequest.mutate()` 在服务端内部安全写入标签。

---

## 六、 联调快速验证闭环

代码编译通过并接入后，建议按以下 3 步快速验证全链路是否闭环：

1.  **验证 1（网关单跳隔离）**：本地启动 `mcdull-gateway` 和 `system-bootstrap`（IDEA `dev` 环境自动带本机用户名标签），用 Postman 请求网关并带上 `X-Env-Tag: 你的用户名`，确认断点命中本地 `system-bootstrap`。
2.  **验证 2（基准兜底能力）**：将请求头改为 `X-Env-Tag: default` 或不传该请求头，确认请求直接路由到开发服务器的公共实例。
3.  **验证 3（异步线程与 Feign 接力）**：在 `system-bootstrap` 的接口里开一个 `@Async` 异步方法调用另一个微服务，确认下游微服务的 `EnvTagContextHolder.getTag()` 依然能准确打印出你的标签。
