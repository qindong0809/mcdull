# mcdull-framework-starter-mysql

mcdull-framework-starter-mysql 是 MCDull 框架的 MySQL 数据库集成模块，基于 MyBatis-Plus 和 Druid 数据源实现，提供了 MySQL 数据库的访问、操作和动态数据源管理功能。

## 项目简介

mcdull-framework-starter-mysql 是 MCDull 框架的 MySQL 数据库集成组件，为应用提供了 MySQL 数据库的访问、操作和动态数据源管理能力。该模块集成了 MyBatis-Plus、Druid 数据源等，简化了数据库操作和配置，同时支持动态数据源切换和租户数据源隔离。

## 功能特性

- 集成 MyBatis-Plus，提供增强的 ORM 功能
- 集成 Druid 数据源，提供高性能的数据库连接池
- 支持 MySQL 数据库访问
- 简化数据库配置
- 支持 SQL 执行监控
- 支持动态数据源，实现多数据源切换
- 支持租户数据源隔离
- 与 Spring 生态无缝集成

## 核心依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| mybatis-plus-spring-boot3-starter | - | MyBatis-Plus Spring Boot 启动器 |
| mybatis-plus-jsqlparser-4.9 | - | MyBatis-Plus SQL 解析器 |
| mybatis-plus-spring | - | MyBatis-Plus Spring 集成 |
| druid-spring-boot-3-starter | - | Druid 数据源 Spring Boot 启动器 |
| mysql-connector-j | - | MySQL 连接器 |
| HikariCP | provided | Hikari 连接池 |
| spring-jdbc | provided | Spring JDBC |
| aspectjweaver | provided | AspectJ 织入器 |
| mcdull-framework-base | provided | MCDull 框架基础模块 |

## 项目结构

```
mcdull-framework-starter-mysql/
├── README.md          # 项目说明文档
└── pom.xml            # Maven 配置文件
```

## 如何使用

### Maven 依赖

在项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-mysql</artifactId>
    <version>${mcdull.version}</version>
</dependency>
```

### 配置示例

在 `application.yml` 或 `application.properties` 文件中添加数据库配置：

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: password
    druid:
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      validation-query: SELECT 1 FROM DUAL
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      pool-prepared-statements: true
      max-pool-prepared-statement-per-connection-size: 20
      filters: stat,wall,log4j
      connection-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000

mybatis-plus:
  mapper-locations: classpath:mapper/**/*.xml
  type-aliases-package: com.example.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-value: 1
      logic-not-delete-value: 0
```

### 定义实体类

```java
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private Integer age;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

### 动态数据源使用示例

```java
@Service
public class DynamicDataSourceService {

    @Autowired
    private GlobalDataRoutingDataSource globalDataRoutingDataSource;
    
    @Autowired
    private TenantDataRoutingDatSource tenantDataRoutingDatSource;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 使用全局数据源执行操作
     */
    public List<User> getUsersFromGlobalDataSource() {
        return globalDataRoutingDataSource.get(() -> {
            return userMapper.selectList(null);
        });
    }
    
    /**
     * 使用租户数据源执行操作
     */
    public List<User> getUsersFromTenantDataSource(String tenantId) {
        return tenantDataRoutingDatSource.get(() -> {
            return userMapper.selectList(null);
        }, tenantId);
    }
}
```

## 核心功能

### 1. 数据库连接管理

集成 Druid 数据源，提供高性能的数据库连接池，支持连接池监控和管理。

### 2. MyBatis-Plus 集成

集成 MyBatis-Plus，提供增强的 ORM 功能，包括自动填充、逻辑删除、分页查询等。

### 3. SQL 执行监控

通过 Druid 提供的监控功能，支持 SQL 执行监控，帮助优化 SQL 性能。

### 4. 代码生成

支持 MyBatis-Plus 的代码生成功能，可以根据数据库表结构自动生成实体类、Mapper、Service 等代码。

### 5. 事务支持

集成 Spring 事务管理，确保数据库操作的原子性和一致性。

### 6. 动态数据源管理

支持动态数据源切换，通过 `SwitchableDataSource` 接口和 `RoutingDataSource` 实现多数据源的动态管理和切换。

### 7. 租户数据源隔离

支持基于租户 ID 的数据源隔离，通过 `TenantDataRoutingDatSource` 实现不同租户的数据隔离。

## 注意事项

- 确保 MySQL 服务已启动并可访问
- 配置正确的数据库连接信息和认证凭据
- 合理设计数据库表结构和索引，提高查询性能
- 对于复杂查询，考虑使用 MyBatis-Plus 的条件构造器或自定义 SQL
- 生产环境中，建议配置适当的 Druid 连接池参数，以获得最佳性能

