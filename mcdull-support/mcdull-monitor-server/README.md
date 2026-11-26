# mcdull-monitor-server

基于 Spring Boot Admin 的监控服务，默认端口 `9000`。

## 安全说明

监控平台是独立信任域，**已启用 Spring Security**，未认证无法访问除健康检查外的任何端点。

| 环境变量 | 必填 | 默认值 | 说明 |
|---|---|---|---|
| `MONITOR_SECURITY_PASSWORD` | 是 | 无 | 至少 12 位；缺失或过短直接启动失败 |
| `MONITOR_SECURITY_USER` | 否 | `admin` | 登录用户名 |
| `MONITOR_BIND_ADDRESS` | 否 | `127.0.0.1` | 监听地址；生产勿直接绑定 `0.0.0.0` 对外网暴露 |

放行端点：`/favicon.ico`、`/assets/**`、`/error`、`/actuator/health`；其余全部要求认证。登录成功后强制回到监控首页，避免受保护资源触发错误分发后被保存到登录回跳地址，出现 `/error?continue` 白页。

启动：

```powershell
$env:MONITOR_SECURITY_PASSWORD = "change-me-12chars-plus"
# 浏览器访问 http://localhost:9000 并登录
```

## 最佳实践功能

1. **日志级别调整控制**
   - 通过监控界面可以动态调整应用的日志级别，无需重启应用
   - 支持调整不同包路径的日志级别

2. **暴露 JMX 接口**
   - 监控应用上下文信息
   - 数据处理场景
   - 监控线程池状态
   - 监控 JVM 运行状态

3. **实例发现**
   - 基于 Nacos 服务发现自动注册实例
   - `seata-server` 无健康检查能力，已排除

## 企业级演进路线

当前为阶段 1（本地认证 + 回环绑定）。后续建议：

- 阶段 2：接企业 IdP（OIDC/SAML），按 group 区分只读/管理员，补审计事件
- 阶段 3：监控平面网络隔离 + mTLS，凭据迁移到 Vault/K8s Secret

注意 SBA 的 `/instances/{id}/events` 是 SSE 长连接，前置 Ingress/代理需支持 `text/event-stream` 并关闭缓冲。
