# mcdull-framework-starter-redis

mcdull-framework-starter-redis 是 MCDull 框架的 Redis 集成模块，基于 Redisson 实现，提供分布式缓存、分布式锁与限流能力。

## 项目简介

引入本 starter 后，Redis 能力自动装配生效，无需在启动类标注任何启用注解。业务代码依赖零依赖的 `mcdull-framework-api-redis` 契约包，运行时实现由本模块提供。

## 功能特性

- 集成 Redisson，提供 Redis 客户端能力
- 集成 Spring Cache，支持 `@Cacheable` / `@CacheEvict` 注解式缓存
- 提供 `ICache` 统一契约，支持对象缓存（含回源防击穿）与常用 KV 操作
- 基于 `@RedisLock` 的分布式锁
- 基于 `@CacheExpire` 的缓存 TTL 控制
- 分布式限流

## 核心依赖

| 依赖 | 说明 |
|------|------|
| redisson-spring-boot-starter | Redisson 客户端 |
| spring-boot-starter-cache | Spring Cache |
| jackson-datatype-jsr310 | Java 8 日期时间支持 |
| aspectjweaver | 切面支持 |
| mcdull-framework-api-redis | 零依赖契约包 |
| mcdull-framework-base | provided |

## 如何使用

### Maven 依赖

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-redis</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 配置示例

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password:
    database: 0
    timeout: 10s
```

当前仅支持 Redis 单机模式，连接参数统一从 `spring.redis.*` 读取。

### 使用缓存注解

```java
@Service
@CacheConfig(cacheNames = "user")
public class UserService {

    @Cacheable(key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @CacheEvict(key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

### 使用 ICache

`*Prefixed` 方法自动加应用名前缀，无后缀方法直接操作原始 key：

```java
@Resource
private ICache cache;

// 应用作用域缓存（自动加前缀）
cache.putPrefixed("user:1", user, 60);
User user = cache.getOrSetPrefixed("user:1", User.class, () -> loadFromDb(1), 60);

// 原始 key 操作（不加前缀）
cache.set("captcha:uuid", text, 120);
String text = cache.get("captcha:uuid");
```

### 使用分布式锁

```java
@Autowired
private RedissonClient redissonClient;

public void doSomethingWithLock() {
    RLock lock = redissonClient.getLock("myLock");
    try {
        if (lock.tryLock(10, 10, TimeUnit.SECONDS)) {
            // 业务逻辑
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    } finally {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
```

## 核心功能

### 1. 分布式缓存

`@Cacheable` / `@CacheEvict` 统一落到 Redis，多实例共享同一份缓存；`ICache` 契约提供显式的对象读写与回源能力。

### 2. 分布式锁

通过 `@RedisLock` 注解或 `RedissonClient` 使用分布式锁，支持可重入锁、公平锁、读写锁等类型。

`@RedisLock` 失败语义：

| 属性 | 默认值 | 说明 |
|---|---|---|
| `throwOnFailure` | `true` | 竞争锁失败时抛 `BusinessException`（`system.busy`），不再静默返回 `null` |
| `messageCode` | 空 | 自定义失败 i18n code，为空时用 `system.busy` |

```java
// 幂等补偿场景可显式降级：竞争失败时返回 null
@RedisLock(key = "'compensate:' + #id", throwOnFailure = false)
public void compensate(Long id) { ... }
```

锁等待被中断时会恢复线程中断标记并抛业务异常；释放锁前校验 `isHeldByCurrentThread()`，避免看门狗租约过期后误解锁。

### 3. 序列化

Redisson codec 与 Spring Cache 序列化器共用同一套 Jackson 配置（含日期格式与多态类型白名单），同一 Redis 中只有一种数据格式。

## 注意事项

- 确保 Redis 服务已启动并可访问
- 从旧版本升级时，原 `StringCodec` / fastjson2 格式缓存数据与新格式不兼容，建议清空相关 key 后重新预热
- 合理设计缓存键与过期时间，避免缓存穿透、击穿和雪崩
- 分布式锁务必在 finally 块中释放
