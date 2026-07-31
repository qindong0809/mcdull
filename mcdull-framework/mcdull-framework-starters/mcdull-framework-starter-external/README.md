# mcdull-framework-starter-external

外部系统集成 Spring Boot Starter — 声明式 API 客户端，内置透明认证、自动刷新 Token、重试机制。

---

## 解决的问题

对接三方系统的通用流程：

```
1. 用凭证换 token   →   POST /auth/token  {账号, 密钥}  →  获得 access token
2. 业务请求带 token →   每次请求 Header 中携带 token
3. token 过期自动刷新 →  下次请求前发现过期，透明重新换 token
```

**不用本 Starter（伪代码）：**

```java
public class ManualIntegration {

    private String cachedToken;
    private long refreshAfter;

    // 每个业务方法都要手动处理 token
    public PreSignVo presignUpload(PreSignRequest request) {
        String token = getOrRefreshToken();                              // 手动管理
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "myAccount " + token);             // 手动注入
        ResponseEntity<PreSignVo> resp =
            restTemplate.postForEntity(url, new HttpEntity<>(request, headers), PreSignVo.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("failed");                        // 手动处理错误
        }
        return resp.getBody();
    }

    // 每对接一个系统都要重写这段逻辑
    private synchronized String getOrRefreshToken() {

        // ① 检查缓存
        if (cachedToken != null && System.currentTimeMillis() < refreshAfter) {
            return cachedToken;
        }

        // ② 用 secretKey 签发短期 JWT 凭证（证明我是谁）
        String credentialJwt = Jwts.builder()
                .claim("account", "myAccount")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 300_000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        // ③ 拿凭证换 access token
        Map<String, String> body = Map.of("accountName", "myAccount", "accountSecret", credentialJwt);
        ResponseEntity<Map> resp = restTemplate.postForEntity(tokenUrl, body, Map.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Token request failed");
        }

        // ④ 存储 token，设置刷新时间（过期前 1 分钟刷新）
        cachedToken = (String) resp.getBody().get("payload");
        refreshAfter = System.currentTimeMillis() + 4 * 60 * 1000L;    // 存 4 分钟

        return cachedToken;
    }
}
```

**用本 Starter（配置即用）：**

```yaml
mcdull:
  external:
    clients:
      prodigy:
        base-url: https://prodigy.example.com
        auth:
          type: jwt-account
          account-name: myAccount
          secret-key: your-secret
          token-path: /api/auth/token
```

```java
@ExternalApi(name = "prodigy")
public interface ProdigyApi {
    @ExternalPost("/integration/upload/presign")
    PreSignVo presignUpload(@Body PreSignRequest request);
}

// 业务代码 — 无需关心 token 的任何细节
PreSignVo result = prodigyApi.presignUpload(request);
```

---

## Token 生命周期设计

### 获取、存储、刷新

```
┌──────────────────────────────────────────────────────────────┐
│                     JwtAccountAuthStrategy                   │
│                                                              │
│  cachedToken: String        ← 存储 access token              │
│  tokenRefreshAfter: long    ← 刷新截止时间（= 获取时间 + 4min）│
│                                                              │
│  getToken():                                                 │
│    if (cachedToken != null && now < tokenRefreshAfter)       │
│        return cachedToken                    ← 直接返回缓存   │
│    else                                                      │
│        synchronized {                        ← 双重检查锁     │
│            if still expired → fetchAndCacheToken()           │
│        }                                                     │
│                                                              │
│  fetchAndCacheToken():                                       │
│    ① signCredentialJwt()   → 用 secretKey 签发凭证 JWT       │
│    ② POST tokenPath        → {accountName, accountSecret}   │
│    ③ tokenExtractor.extract(response)  → 解析 access token  │
│    ④ cachedToken = token                                     │
│    ⑤ tokenRefreshAfter = now + 4min - 1min(提前刷新边距)     │
└──────────────────────────────────────────────────────────────┘
```

### 时间线示意

```
0min          4min(刷新)    5min(真正过期)
│─────────────│─────────────│
Token 有效期
│──────────────────── 5 min ─────────────────────│

缓存有效期（4min - 1min边距 = 3min）
│──────────── 3 min ─────────│
                             ↑ 下次请求自动重新换 token
```

### 线程安全

使用双重检查锁（DCL）保证高并发下只有一个线程执行换 token 操作：

```java
// 第一次检查（无锁，性能高）
if (isTokenValid()) return cachedToken;

// 进入同步块
synchronized (this) {
    // 第二次检查（防止多线程都通过第一次检查后重复刷新）
    if (isTokenValid()) return cachedToken;
    return fetchAndCacheToken();
}
```

---

## 本组件做了什么

| 能力 | 说明 |
|------|------|
| 声明式 API 代理 | 扫描 `@ExternalApi` 接口，自动生成动态代理 Bean |
| Token 获取 | 用凭证（JWT/API Key）换 access token |
| Token 存储 | 内存缓存（volatile + 过期时间） |
| Token 刷新 | 到期前 1 分钟自动刷新，双重检查锁保证线程安全 |
| Token 注入 | 每次请求通过 OkHttp 拦截器透明注入 Authorization header |
| HTTP 通信封装 | OkHttpClient 构建、超时、连接池 |
| 请求序列化 | 方法参数 → JSON body / 路径变量 / 查询参数 |
| 响应反序列化 | JSON → 方法返回类型（泛型安全） |
| 失败重试 | 指数退避重试（仅 5xx 和网络异常，4xx 不重试） |
| 请求日志 | 可开关的 HTTP 请求/响应日志 |
| 统一异常 | `ExternalApiException`（含 httpStatus、clientName） |
| 多实例 | 一个应用同时对接 N 个外部系统，独立配置 |

## 本组件不做什么

| 不做 | 理由 |
|------|------|
| 不管 payload 构建 | 业务领域知识，由业务侧 DTO 和 Builder 处理 |
| 不管调用时机/防抖 | 业务逻辑决定何时调 |
| 不管失败后的业务补偿 | 各业务容错策略不同 |
| 不持久化 token | 内存缓存已满足大多数场景；集群场景需自定义 |
| 不管服务发现 | 外部系统是固定 URL |
| 不做业务幂等 | 属于业务层 |

---

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-external</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 配置 application.yml

```yaml
mcdull:
  external:
    clients:
      prodigy:
        base-url: https://prodigy.example.com
        auth:
          type: jwt-account            # 认证类型：jwt-account | api-key | none
          account-name: my-service     # 服务账号
          secret-key: your-secret      # 签名密钥（勿提交到代码库）
          token-path: /api/auth/token  # token 获取端点
        connect-timeout: 30
        read-timeout: 120
        log-enabled: false
        retry:
          max-attempts: 3
          backoff-ms: 2000
```

### 3. 声明 API 接口

```java
@ExternalApi(name = "prodigy")
public interface ProdigyApi {

    @ExternalPost("/sponsor/{sponsorId}/study/{studyId}/upload/presign")
    PreSignVo presignUpload(@PathVar("sponsorId") String sponsorId,
                            @PathVar("studyId") String studyId,
                            @Body PreSignRequest request);

    @ExternalPost("/sponsor/{sponsorId}/study/{studyId}/upload/delivered")
    DeliverVo deliverUpload(@PathVar("sponsorId") String sponsorId,
                            @PathVar("studyId") String studyId,
                            @Body DeliverRequest request);

    @ExternalGet("/kb/files")
    List<FileInfo> getFiles(@QueryParam("folder") String folder,
                            @QueryParam("lifecycle") String lifecycle);
}
```

### 4. 启用扫描

```java
@EnableExternalClients(basePackages = "com.example.api.external")
@SpringBootApplication
public class MyApplication { }
```

### 5. 注入使用

```java
@Service
public class MyService {

    @Resource
    private ProdigyApi prodigyApi;

    public void sync() {
        // token 已透明管理，直接调业务方法
        PreSignVo result = prodigyApi.presignUpload("sponsor1", "study1", request);
    }
}
```

---

## 认证策略

| 类型 | auth.type | 适用 |
|------|-----------|------|
| 用户名+密码换 Token | `credentials` | 大多数三方系统的标准登录模式 |
| JWT 服务账号 | `jwt-account` | 用密钥签 JWT 再换 token 的 S2S 模式 |
| 静态 API Key | `api-key` | 每次带固定 key |
| 无认证 | `none` | 公开 API |

### credentials（最常用）

适合你描述的标准场景：用户名 + 密码调登录接口换 token，后续请求带 token。

```yaml
auth:
  type: credentials
  username: my-service-account        # 用户名 / client_id
  password: my-password               # 密码 / client_secret
  token-path: /api/login              # 登录接口路径
  token-field: token                  # 响应里哪个字段是 token（默认 token）
  extra-params:                        # 可选：附加请求参数
    grant_type: password
```

请求体：`{ "username": "xxx", "password": "yyy", "grant_type": "password" }`

响应：`{ "token": "eyJxxx..." }` → 提取 `token` 字段缓存

后续请求自动带：`Authorization: Bearer eyJxxx...`

```java
// 不同三方系统 token 字段名不同，通过 token-field 适配
// {"access_token": "xxx"}  →  token-field: access_token
// {"data": {"token": "xxx"}}  →  自定义 TokenExtractor（见下方）
```

### jwt-account（Prodigy 等 S2S 场景）

```yaml
auth:
  type: jwt-account
  account-name: my-service
  secret-key: your-secret
  token-path: /api/auth/token
  token-field: payload               # 响应里哪个字段是 token（默认 payload）
```

请求体：`{ "accountName": "xxx", "accountSecret": "用secretKey签的JWT" }`

### 自定义 Token 响应解析

嵌套路径的 token（如 `{"data": {"token": "xxx"}}`）通过 `TokenExtractor` 适配：

```java
// 直接用 TokenExtractor.ofPath 解析嵌套路径
new CredentialsAuthStrategy(baseUrl, tokenPath, username, password,
        TokenExtractor.ofPath("data", "token"), extraParams, ttlMs, objectMapper);
```

### 自定义认证策略（如 OAuth2）

实现 `AuthStrategy` 接口：

```java
public class OAuth2AuthStrategy implements AuthStrategy {
    @Override
    public Map<String, String> getAuthHeaders() {
        return Map.of("Authorization", "Bearer " + getAccessToken());
    }

    private String getAccessToken() {
        // 自行实现 OAuth2 Client Credentials 流程
    }
}
```

---

## 注解一览

| 注解 | 位置 | 说明 |
|------|------|------|
| `@ExternalApi(name)` | 接口 | 声明外部系统客户端 |
| `@EnableExternalClients` | 启动类 | 启用接口扫描 |
| `@ExternalPost(path)` | 方法 | HTTP POST |
| `@ExternalGet(path)` | 方法 | HTTP GET |
| `@ExternalPut(path)` | 方法 | HTTP PUT |
| `@PathVar(name)` | 参数 | URL 路径变量 |
| `@QueryParam(name)` | 参数 | URL 查询参数 |
| `@Body` | 参数 | 请求体（JSON） |

---

## 多系统同时对接

```yaml
mcdull:
  external:
    clients:
      prodigy:
        base-url: https://prodigy.example.com
        auth: { type: jwt-account, account-name: ctms, secret-key: xxx, token-path: /api/auth/token }
      wechat:
        base-url: https://api.weixin.qq.com
        auth: { type: none }
      payment:
        base-url: https://pay.example.com
        auth: { type: api-key, api-key: sk-xxx, header-name: X-Pay-Key }
```

---

## 架构概览

```
业务代码
  ↓ 调用接口方法
@ExternalApi 动态代理 (ExternalClientProxy)
  ↓ 解析注解 → 构建 HTTP Request
OkHttpClient 拦截器链:
  ├── AuthInterceptor
  │     ↓ 调 AuthStrategy.getAuthHeaders()
  │     ↓ JwtAccountAuthStrategy:
  │         ① isTokenValid? → 直接返回缓存 token
  │         ② 过期 → fetchAndCacheToken()
  │              → signCredentialJwt()
  │              → POST /auth/token
  │              → 缓存新 token
  │         → 注入 Authorization header
  ├── RetryInterceptor    (5xx 指数退避重试)
  └── LoggingInterceptor  (可选)
  ↓
外部系统
```

---

## 设计原则

1. **声明式** — 接口 + 注解，不写实现
2. **配置驱动** — yml 控制所有行为
3. **认证透明** — Token 获取/存储/刷新对业务代码不可见
4. **可插拔** — 认证策略、Token 解析均可扩展
5. **零侵入** — 引 jar 即用
6. **故障隔离** — 每个 client 独立超时和重试
