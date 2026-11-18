# `mcdull-gateway` 快速开始说明书

**`mcdull-gateway` 是基于 Spring Cloud Gateway 搭建的统一微服务网关，默认监听 `9090` 端口。** 该项目采用“本地路由受控 + Nacos 基础配置共享”的云原生架构，支持通过启动环境变量无缝切换开发与生产环境。

---


## 三、 多环境启动指南

本项目支持“一次构建，处处运行”，通过注入环境变量即可动态切换环境。

### 1. 本地 IDEA 开发启动（默认 `dev` 环境）

*   **直接启动**：无需配置任何参数，直接运行网关启动类即可。系统默认使用 `SPRING_PROFILES_ACTIVE=dev`，自动连接 `mcdull.io:8848` 的 `dev` 命名空间。
*   **自定义本地参数**：如需连接本地单机 Nacos，可在 IDEA 的 **Run/Debug Configurations -> Environment variables** 中注入：
    ```text
    SPRING_PROFILES_ACTIVE=dev;NACOS_ADDR=mcdull.io:8848;NACOS_PWD=nacos
    ```

### 2. Linux 服务器 Jar 包部署（`prod` 环境）

使用 `export` 导出环境变量后再启动 Jar 包，可避免敏感密码暴露在进程列表中：

```bash
export SPRING_PROFILES_ACTIVE=prod
export NACOS_ADDR=prod.mcdull.io:8848
export NACOS_USER=prod_nacos
export NACOS_PWD=StrongProdPwd@2026

nohup java -jar mcdull-gateway.jar > /dev/null 2>&1 &
```

### 3. Docker 容器化部署

通过 `-e` 参数注入环境变量即可直接运行镜像：

```bash
docker run -d --name mcdull-gateway -p 9090:9090 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e NACOS_ADDR=10.0.2.100:8848 \
  -e NACOS_USER=prod_nacos \
  -e NACOS_PWD=StrongProdPwd@2026 \
  mcdull-gateway:latest
```

---

## 四、 核心环境变量速查表

| 环境变量名 | 必填 | 默认值 | 说明 |
| :--- | :--- | :--- | :--- |
| **`SPRING_PROFILES_ACTIVE`** | 否 | `dev` | 激活的环境标识（可选 `dev`、`test`、`prod`）。 |
| **`NACOS_ADDR`** | 否 | `mcdull.io:8848` | Nacos 注册中心与配置中心地址。 |
| **`NACOS_USER`** | 否 | `nacos` | Nacos 登录用户名。 |
| **`NACOS_PWD`** | 否 | `nacos` | Nacos 登录密码（生产环境务必覆盖）。 |
| **`NACOS_NAMESPACE`** | 否 | 跟随 `active` 值 | 强制指定 Nacos 命名空间 ID，默认自动与当前激活环境保持一致。 |
| **`ENV_TAG`** | 否 | 本机系统用户名 | 仅在 `dev` 环境生效，用于全链路开发联调流量染色隔离。 |

---

## 五、 路由转发规则与快速验证

启动成功后，可通过以下路径验证网关是否正常工作：

*   **网关健康检查**：访问 `http://localhost:9090/actuator/health`，返回 `{"status":"UP"}` 表示网关已就绪。
*   **外部示例路由测试**：访问 `http://localhost:9090/httpbin/get`，网关会自动剥离 `/httpbin` 前缀并转发至 `https://httpbin.org`（注意该公共测试服务单 IP 限流约 17 QPS）。
*   **权限模块转发**：访问 `http://localhost:9090/uac/xxx`，转发至微服务 `system-bootstrap` 的 `/xxx` 接口。
*   **MDC 模块转发**：访问 `http://localhost:9090/mdc/xxx`，转发至微服务 `mcdull-mdc-provider` 的 `/xxx` 接口。
