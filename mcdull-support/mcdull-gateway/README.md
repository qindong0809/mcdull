# `mcdull-gateway` 快速开始说明书

`mcdull-gateway` 是基于 Spring Cloud Gateway 的统一微服务网关，默认监听 `9090` 端口。配置规范与业务服务保持一致：

- profile 统一为 `local` / `test` / `prod`
- `SPRING_PROFILES_ACTIVE` 必填，缺失时启动失败（fail-fast）
- 全环境默认接入 Nacos：服务发现 + 共享配置导入
- 核心路由写在本地并随 Git 版本受控管理

---

## 1. 一键复制环境变量

### 1.1 本地 local

```text
SPRING_PROFILES_ACTIVE=local;NACOS_ADDR=mcdull.io:8848;NACOS_USER=nacos;NACOS_PWD=123456;GATEWAY_SERVER_PORT=9090;GATEWAY_REDIS_HOST=mcdull.io;GATEWAY_REDIS_PORT=6379;GATEWAY_REDIS_DB=1;GATEWAY_REDIS_PASSWORD=;GATEWAY_REDIS_TIMEOUT=3s;GATEWAY_LOG_PATH=./logs
```

粘贴到 IDEA 的 `Environment variables` 后，运行 `io.gitee.dqcer.mcdull.gateway.GatewayApplication` 即可。

> 若本机 Nacos 仍是默认的 `nacos/nacos`，把 `NACOS_PWD=123456` 改为 `NACOS_PWD=nacos`。
> 若 Nacos 在本地，把 `NACOS_ADDR` 改为 `localhost:8848`。

### 1.2 测试 test

```text
SPRING_PROFILES_ACTIVE=test;NACOS_ADDR=mcdull.io:8848;NACOS_USER=nacos;NACOS_PWD=123456;GATEWAY_SERVER_PORT=9090;GATEWAY_REDIS_HOST=mcdull.io;GATEWAY_REDIS_PORT=6379;GATEWAY_REDIS_DB=1;GATEWAY_REDIS_PASSWORD=;GATEWAY_REDIS_TIMEOUT=3s;GATEWAY_LOG_PATH=./logs;GATEWAY_TAG_ENABLED=true;SPRING_BOOT_ADMIN_URL=http://localhost:9000
```

### 1.3 生产 prod（示例值需替换为真实值）

```text
SPRING_PROFILES_ACTIVE=prod;NACOS_ADDR=prod.mcdull.io:8848;NACOS_USER=prod_nacos;NACOS_PWD=请替换;GATEWAY_SERVER_PORT=9090;GATEWAY_REDIS_HOST=prod.mcdull.io;GATEWAY_REDIS_PORT=6379;GATEWAY_REDIS_DB=1;GATEWAY_REDIS_PASSWORD=请替换;GATEWAY_REDIS_TIMEOUT=3s;GATEWAY_LOG_PATH=/data/logs/mcdull-gateway;GATEWAY_TAG_ENABLED=true;SPRING_BOOT_ADMIN_URL=http://admin.prod:9000
```

---

## 2. Nacos 配置

默认使用 Nacos 的 `public` 命名空间。若需要使用独立命名空间，追加：

```text
NACOS_NAMESPACE=local
```

`DEFAULT_GROUP` 下需准备以下 Data ID（可导入 `doc/nacos_config_export_20240421173102.zip` 作为基线）：

| Data ID | 用途 |
|---|---|
| `datasource.yaml` | 数据源公共配置 |
| `redis.yaml` | Redis 公共配置 |
| `sa-token.yaml` | Sa-Token 公共配置 |
| `log-level.properties` | 日志级别公共配置 |
| `springboot-admin.yaml` | Spring Boot Admin 公共配置 |
| `mcdull-gateway-local.yaml` | 网关私有配置（按需） |

导入均使用 `optional:nacos:`，缺少某个 Data ID 不会阻断启动，但对应配置不会生效。

---

## 3. 环境变量速查

| 变量 | 必填 | 示例值 | 说明 |
|---|---|---|---|
| `SPRING_PROFILES_ACTIVE` | 是 | `local` | 可选 `local` / `test` / `prod` |
| `NACOS_ADDR` | 否 | `mcdull.io:8848` | Nacos 地址 |
| `NACOS_USER` | 否 | `nacos` | Nacos 账号 |
| `NACOS_PWD` | 否 | `123456` | Nacos 密码，按实际环境调整 |
| `NACOS_NAMESPACE` | 否 | 空 | 默认 `public` 命名空间 |
| `ENV_TAG` | 否 | 本机用户名 | `local` 下的流量染色标签 |
| `GATEWAY_SERVER_PORT` | 否 | `9090` | 网关端口 |
| `GATEWAY_REDIS_HOST` | local 否 / test、prod 是 | `localhost` | Redis 地址，全局限流与 Sa-Token 会话依赖 |
| `GATEWAY_REDIS_PORT` | 否 | `6379` | Redis 端口 |
| `GATEWAY_REDIS_DB` | 否 | `1` | Redis 库，需与 `SYSTEM_REDIS_DB` 保持一致 |
| `GATEWAY_REDIS_PASSWORD` | 否 | 空 | Redis 密码 |
| `GATEWAY_REDIS_TIMEOUT` | 否 | `3s` | Redis 连接超时 |
| `GATEWAY_LOG_PATH` | 否 | `./logs` | 日志根目录 |
| `GATEWAY_TAG_ENABLED` | 否 | `true` | 标签路由开关，`local` 固定关闭 |
| `SPRING_BOOT_ADMIN_URL` | 否 | `http://localhost:9000` | `prod` 必填 |

---

## 4. 路由规则

当前本地受控路由：

| 路由 ID | 路径 | 目标服务 | 说明 |
|---|---|---|---|
| `system-bootstrap` | `/uac/**` | `lb://system-bootstrap` | `StripPrefix=1` |
| `workflow-bootstrap` | `/workflow/**` | `lb://workflow-bootstrap` | 保留原路径 |
| `mcdull-mdc-provider` | `/mdc/**` | `lb://mcdull-mdc-provider` | `StripPrefix=1` |
| `demo-http` | `/httpbin/**` | `https://httpbin.org` | 外部演示路由 |

`local` 开启服务发现自动路由，`test` / `prod` 一律关闭，避免内部接口绕过网关鉴权。

---

## 5. 启动与验证

```bash
mvn -pl mcdull-support/mcdull-gateway spring-boot:run
```

启动成功后验证：

- 健康检查：`http://localhost:9090/actuator/health`
- 外部示例路由：`http://localhost:9090/httpbin/get`
- 权限模块转发：`http://localhost:9090/uac/xxx`
- 工作流转发：`http://localhost:9090/workflow/center/overview`
- MDC 模块转发：`http://localhost:9090/mdc/xxx`
