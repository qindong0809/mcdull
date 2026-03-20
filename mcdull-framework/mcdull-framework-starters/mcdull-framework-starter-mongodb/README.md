# mcdull-framework-starter-mongodb

mcdull-framework-starter-mongodb 是 MCDull 框架的 MongoDB 集成模块，基于 Spring Boot Starter Data MongoDB 实现，提供了 MongoDB 数据库的访问和操作功能。

## 项目简介

mcdull-framework-starter-mongodb 是 MCDull 框架的 MongoDB 集成组件，为应用提供了 MongoDB 数据库的访问和操作能力。该模块集成了 Spring Boot Starter Data MongoDB，简化了 MongoDB 的配置和使用。

## 功能特性

- 集成 Spring Boot Starter Data MongoDB
- 提供 MongoDB 数据库访问能力
- 支持 Spring Data MongoDB  repositories
- 简化 MongoDB 配置
- 与 Spring 生态无缝集成

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| spring-boot-starter-data-mongodb | - | Spring Boot MongoDB 启动器 |
| mcdull-framework-base | provided | MCDull 框架基础模块 |

## 项目结构

```
mcdull-framework-starter-mongodb/
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
    <artifactId>mcdull-framework-starter-mongodb</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 配置示例

在 `application.yml` 或 `application.properties` 文件中添加 MongoDB 配置：

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/mydb
      # 或使用以下配置
      # host: localhost
      # port: 27017
      # database: mydb
      # username: admin
      # password: password
```

### 定义实体类

```java
@Document(collection = "users")
public class User {

    @Id
    private String id;
    
    private String username;
    
    private String email;
    
    private Integer age;
    
    //  getter 和 setter 方法
}
```

### 定义 Repository

```java
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);
    
    List<User> findByAgeGreaterThan(Integer age);
    
    @Query("{ 'email' : { '$regex' : ?0 } }")
    List<User> findByEmailLike(String email);
}
```

### 使用示例

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public List<User> findUsersByAgeGreaterThan(Integer age) {
        return userRepository.findByAgeGreaterThan(age);
    }
    
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
```

## 核心功能

### 1. MongoDB 连接管理

自动配置 MongoDB 连接，支持通过 URI 或详细参数配置连接信息。

### 2. Spring Data MongoDB 集成

支持 Spring Data MongoDB 的 repositories 模式，简化数据访问层代码。

### 3. 文档操作

提供完整的 MongoDB 文档操作能力，包括增删改查、聚合查询等。

### 4. 事务支持

支持 MongoDB 的事务功能，确保数据操作的一致性。

## 注意事项

- 确保 MongoDB 服务已启动并可访问
- 配置正确的连接信息和认证凭据
- 合理设计文档结构和索引，提高查询性能
- 对于复杂查询，考虑使用 @Query 注解或自定义查询方法


