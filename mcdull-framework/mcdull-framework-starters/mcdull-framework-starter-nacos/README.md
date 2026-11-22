# mcdull-framework-starter-nacos

Nacos 集成启动器。当前定位：**仅作为服务注册中心**（服务注册与发现），不接入 Nacos 配置中心。

## 能力

- 服务注册与发现：业务服务启动后自动注册到 `spring.cloud.nacos.server-addr` 指定的 Nacos。
- 命名空间隔离：通过 `NACOS_NAMESPACE` 设置，默认 `public`。
- Prometheus 公共标签（`application`）与优雅停机时的注销补偿。

## 配置项

| 环境变量 | 默认值 | 说明 |
|---|---|---|
| `NACOS_ADDR` | `mcdull.io:8848` | Nacos 地址 |
| `NACOS_USER` | `nacos` | 账号 |
| `NACOS_PWD` | `nacos` | 密码 |
| `NACOS_NAMESPACE` | 空 | 命名空间，默认 `public` |

业务配置（数据源、Redis、Sa-Token、日志级别等）通过环境变量或本地 `application-*.yml` 注入，无需在 Nacos 中创建共享 Data ID。

## 日志级别

原先基于 Nacos Data ID `log-level.properties` 的动态日志级别监听已关闭（`LogLevelListener` 不装配）。需要调整日志级别时，修改对应服务的 `application-*.yml` 或 `logback-spring.xml`。
