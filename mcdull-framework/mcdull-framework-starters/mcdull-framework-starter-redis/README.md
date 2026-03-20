# mcdull-framework-starter-redis

mcdull-framework-starter-redis 是 MCDull 框架的 Redis 集成模块，基于 Redisson 实现，提供了 Redis 缓存、分布式锁、分布式对象等功能。

## 项目简介

mcdull-framework-starter-redis 是 MCDull 框架的 Redis 集成组件，为应用提供了 Redis 的访问和操作能力。该模块集成了 Redisson、Spring Cache、Caffeine 等，支持分布式缓存、分布式锁、分布式对象等功能。

## 功能特性

- 集成 Redisson，提供丰富的 Redis 客户端功能
- 集成 Spring Cache，支持注解式缓存
- 集成 Caffeine，提供本地缓存支持
- 支持分布式锁
- 支持分布式对象
- 支持 Redis 哨兵模式和集群模式
- 与 Spring 生态无缝集成

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| redisson-spring-boot-starter | - | Redisson Spring Boot 启动器 |
| caffeine | - | Caffeine 本地缓存 |
| spring-boot-starter-cache | - | Spring Cache 启动器 |
| jackson-datatype-jsr310 | - | Java 8 日期时间支持 |
| aspectjweaver | - | AspectJ 织入器 |
| mcdull-framework-base | provided | MCDull 框架基础模块 |
| mcdull-framework-config | provided | MCDull 框架配置模块 |
| hutool-json | provided | JSON 工具库 |

## 项目结构

```
mcdull-framework-starter-redis/
├── README.md          # 项目说明文档
└── pom.xml            # Maven 配置文件
```

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-redis</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 配置示例

在 `application.yml` 或 `application.properties` 文件中添加 Redis 配置：

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
    timeout: 10000

redisson:
  config:
    singleServerConfig:
      address: redis://localhost:6379
      password: 
      database: 0
      connectionMinimumIdleSize: 10
      connectionPoolSize: 64
      idleConnectionTimeout: 10000
      connectTimeout: 10000
      timeout: 3000
      retryAttempts: 3
      retryInterval: 1500

spring:
  cache:
    type: redis
    redis:
      time-to-live: 600000  # 10分钟
```

### 使用缓存注解

```java
@Service
@CacheConfig(cacheNames = "user")
public class UserService {

    @Cacheable(key = "#id")
    public User getUserById(Long id) {
        // 从数据库查询用户信息
        return userRepository.findById(id).orElse(null);
    }
    
    @CachePut(key = "#user.id")
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    @CacheEvict(key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

### 使用 Redisson 分布式锁

```java
@Autowired
private RedissonClient redissonClient;

public void doSomethingWithLock() {
    RLock lock = redissonClient.getLock("myLock");
    try {
        // 尝试获取锁，最多等待10秒，持有锁最多10秒
        if (lock.tryLock(10, 10, TimeUnit.SECONDS)) {
            // 执行需要加锁的业务逻辑
            System.out.println("获取锁成功，执行业务逻辑");
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    } finally {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
```

## 核心功能

### 1. 分布式缓存

集成 Spring Cache，支持通过注解方式使用 Redis 作为缓存，简化缓存操作。

### 2. 分布式锁

通过 Redisson 提供分布式锁功能，支持可重入锁、公平锁、读写锁等多种锁类型。

### 3. 分布式对象

支持分布式对象，如分布式 Map、List、Set、Queue 等，方便在分布式环境中共享数据。

### 4. 本地缓存

集成 Caffeine 作为本地缓存，提高缓存性能，减少 Redis 访问压力。

### 5. 多种部署模式

支持 Redis 单机模式、哨兵模式和集群模式，适应不同的部署环境。

## 注意事项

- 确保 Redis 服务已启动并可访问
- 配置正确的 Redis 连接信息和认证凭据
- 合理设计缓存键和缓存策略，避免缓存穿透、缓存击穿和缓存雪崩
- 对于分布式锁，确保在 finally 块中释放锁，避免死锁
- 生产环境中，建议配置适当的连接池参数和超时设置

