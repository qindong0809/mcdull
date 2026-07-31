<div align="center">

# McDull

### 企业级 Spring Cloud 微服务开发框架

从真实线上环境中提炼，提供标准化、可扩展的微服务架构脚手架

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.1-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Sa-Token](https://img.shields.io/badge/Sa--Token-1.42.0-blue)](https://sa-token.cc)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.16-blue)](https://baomidou.com)
[![License](https://img.shields.io/badge/License-Apache%202.0-green)](LICENSE)
[![Gitee](https://img.shields.io/badge/Gitee-dqcer%2Fmcdull-C71D23?logo=gitee&logoColor=white)](https://gitee.com/dqcer/mcdull)

[快速开始](#快速开始) · [模块说明](#项目结构) · [亮点功能](#亮点功能) · [文档](#文档) · [问题反馈](https://gitee.com/dqcer/mcdull/issues)

</div>

---

## 关于项目

McDull 是一个基于 **Spring Boot 3 + Spring Cloud** 的企业级微服务开发框架，从真实线上生产环境提炼而来，提供一套开箱即用的标准化脚手架。

**设计目标：**

- 最小依赖原则，杜绝循环重复依赖
- 模块化架构，层次清晰，方便按需引入
- 前沿技术与成熟度的平衡选型
- 有节制地使用第三方组件，最大程度实现自主可控
- 通过 ArchUnit 静态检查在 CI/CD 层面强制架构规范

---

## 技术栈

| 类别 | 技术 | 版本 |
|---|---|---|
| 基础框架 | Spring Boot | 3.2.4 |
| 微服务 | Spring Cloud / Spring Cloud Alibaba | 2023.0.1 / 2023.0.1.0 |
| 服务注册与配置 | Nacos | 2.x |
| API 网关 | Spring Cloud Gateway | — |
| 服务调用 | OpenFeign | — |
| 认证授权 | Sa-Token | 1.42.0 |
| ORM | MyBatis-Plus | 3.5.16 |
| 数据库 | MySQL | 8.x |
| 缓存 | Redis (Redisson) | 3.43.0 |
| 文档数据库 | MongoDB | — |
| 流程引擎 | Warm Flow | — |
| 对象存储 | x-file-storage | 2.2.1 |
| API 文档 | Knife4j | 4.5.0 |
| 监控 | Spring Boot Admin / JavaMelody | — |
| 架构检查 | ArchUnit | 1.0.1 |
| 构建工具 | Maven | 3.6+ |
| CI/CD | Gitee Go 流水线 | — |

---

## 亮点功能

> 按能力层次分为两组：**框架基础能力**（可独立引入的 Starter）和**业务系统能力**（内置于业务模块）。

### ── 框架基础能力 ──


### 🏗️ ArchUnit 架构守护

通过 `mcdull-framework-enforcer` 模块，将架构规范写成单元测试代码，在 CI 阶段强制检查，彻底杜绝规范只靠 Review 落地的问题。

内置约束规则包括：
- 严格的四层调用顺序：Controller → Service → Manager → Repository/Mapper，越层调用直接 CI 失败
- 类命名规范：Mapper 类必须以 `Mapper` 结尾，DTO/VO/DO/Enum 类名必须符合对应后缀约定
- 禁止使用 `System.out/err`、`e.printStackTrace()`
- 禁止直接抛出 `Exception`、`RuntimeException`、`Throwable` 等泛型异常

```java
// 业务模块只需引入 enforcer 依赖，一行代码接入所有规则
@AnalyzeClasses(packages = "com.yourcompany.yourapp")
class ArchTest {
    @ArchTest
    static final List<ArchRule> rules = ArchitectureEnforcer.REQUIRED_RULES;
}
```

### 🔐 多端点认证体系（Sa-Token）

基于 Sa-Token 实现管理端、App 端、普通用户端三套独立的会话体系，通过 `StpKit` 统一管理。每次请求由 `HttpTraceLogFilter` 完成：
- TraceId 注入（支持从上游 Header 透传）
- 用户会话加载到 `UnifySession`（`UserContextHolder` ThreadLocal 存储）
- 数据源路由切换
- 安全响应头自动注入（`X-Content-Type-Options`、`X-Frame-Options`、`X-XSS-Protection`）

### 🔒 声明式分布式锁

基于 Redisson 封装 `@RedisLock` 注解，支持 SpEL 表达式动态生成 key，零侵入加锁。

```java
@RedisLock(key = "'order:submit:' + #userId", timeout = 10)
public void submitOrder(Long userId) { ... }
```

同时提供编程式 `ConcurrentRateLimiter`，支持限流和带超时的分布式锁，并可选是否抛出异常（适用于幂等补偿场景）。

### ⏱️ @CacheExpire — 缓存防雪崩

扩展 Spring `@Cacheable`，支持精确控制每个方法的缓存过期时间，并内置随机浮动范围（`floatRange`），消除大量 key 同时失效导致的缓存雪崩。

```java
@Cacheable(value = "user", key = "#id")
@CacheExpire(value = 300, floatRange = 0.1)  // 300s ± 随机浮动
public UserVO getUser(Long id) { ... }
```

### 🗂️ 本地 + Redis 两级缓存

`CaffeineCache` + `RedissonCache` 双层缓存机制，热点数据优先命中本地 Caffeine（软引用，内存不足自动回收），减少 Redis 网络开销，`AbstractTransformer` 的字段翻译直接使用该机制做数据词典缓存。

### 🏷️ @Transform — 返回值字段自动翻译

`TranslatorAspect` + `@Transform` 注解，在 Controller 返回值切面中自动将枚举 code、字典 ID 翻译为显示文本，无需在每个 Service 方法中手动转换，支持分页列表、集合、嵌套对象的递归翻译。

```java
@Transform(from = "statusCode", dataSource = StatusEnum.class)
private String statusName;
```

### 🔣 统一枚举体系（IEnum）

`IEnum<T>` 接口将枚举涉及的六个横切关注点统一处理，业务枚举只需实现接口并在构造器调用 `init()` 即可获得所有能力：

```java
public enum StatusEnum implements IEnum<Integer> {
    ACTIVE(1, "status.active"),
    INACTIVE(0, "status.inactive");

    StatusEnum(Integer code, String text) {
        this.init(code, text);  // 自动注册到 DictPool
    }
}
```

| 能力 | 说明 |
|---|---|
| **内存字典缓存** | `DictPool` ConcurrentHashMap，`init()` 时自动注册，查询零 DB 开销 |
| **@Deprecated 自动过滤** | 枚举值标记废弃后，`getAll()` 自动从下拉列表排除，旧数据兼容不受影响 |
| **i18n 自动集成** | `getText()` 优先走 `DynamicLocaleMessageSource` 查 i18n key，多语言零额外代码 |
| **入参合法性校验** | `@EnumsIntValid(StatusEnum.class)` 标在 DTO 字段，Bean Validation 自动拦截非法值 |
| **API 文档自动枚举** | `@SchemaEnum(StatusEnum.class)` 标在 DTO 字段，Knife4j 文档自动列出所有枚举值 |
| **`@Transform` 翻译** | `TranslatorAspect` 直接识别 `IEnum` 类型，code → label 翻译零配置 |

### 🌐 国际化 + 多时区 + 自定义日期格式

三个能力组合，满足企业级多地区部署需求：
- `CustomizeLocaleResolver` 根据请求动态解析语言，`DynamicLocaleMessageSource` 支持运行时切换
- `@DynamicDateFormat(enableTimezone = true)` + `DynamicDateSerialize` 按用户会话中的时区和日期格式偏好序列化，无需业务代码感知
- 导出场景提供 `CustomSerializer.serializeDate()` 工具方法，统一日期处理入口


### ⚙️ 自研业务流程编排框架（Process Flow）

`mcdull-framework-starter-flow` 提供轻量级流程编排能力，替代 if-else 堆叠。流程定义与节点实现完全解耦，流程图外置为 JSON，运行时动态装配。

```json
// flow/process_flow_login.json
{
  "id": "process_flow_login",
  "nodeList": ["Validate Captcha", "Validate Login Name Password", "Authority"]
}
```

```java
@Component("Validate Captcha")
@TreeNode(code = "Validate Captcha")
public class ValidateCaptcha implements ProcessHandler<LoginContext> {
    @Override
    public void execute(LoginContext context) { ... }

    @Override
    public void catchException(Exception e, LoginContext context) { ... }

    @Override
    public void finallyException(ProcessHandler handler, Exception e, LoginContext context) { ... }
}
```

关键设计：
- **节点级 try-catch-finally**：每个节点独立定义异常处理，不污染其他节点
- **父节点 try 共享**：`extendParentNodeTryAttr() = true` 时，子节点复用父节点的 catch/finally 逻辑
- **终止节点**：`endNode() = true` 时流程在当前节点后停止
- **Context 透传**：贯穿全流程，节点间通过 `Dict` 传递中间结果，无需方法入参层层传递
- **整体事务**：`ProcessFlow.run()` 带 `@Transactional`，整个流程在一个事务内

### 🗄️ 多层动态数据源路由

`mcdull-framework-starter-mysql` 实现三层叠加的数据源路由体系，支持多租户数据隔离：

| 层次 | 实现类 | 用途 |
|---|---|---|
| 平台层 | `GlobalDataRoutingDataSource` | 全局默认数据源，请求进入时切换 |
| 租户层 | `TenantDataRoutingDatSource` | 按 tenantId 动态路由到对应租户库 |
| 方法层 | `@DynamicDataSource` + `DataSourceAspect` | 注解声明，SpEL 表达式动态计算 key |

`SwitchableDataSource` 接口统一抽象切换行为，业务代码通过 `get(supplier)` 模式在指定数据源上执行，自动 finally 清理上下文。

### 🔗 声明式第三方系统对接（External Client Starter）

`mcdull-framework-starter-external` 提供类似 OpenFeign 的编程模型，专门用于对接外部第三方 HTTP 系统。只需定义接口 + 注解，框架自动生成动态代理，处理认证、序列化、重试和日志，业务代码零侵入。

```yaml
mcdull:
  external:
    clients:
      prodigy:
        base-url: https://prodigy.example.com
        auth:
          type: jwt-account   # none | jwt-account | credentials | api-key
          account-name: ctms
          secret-key: ${SECRET_KEY}
          token-path: /api/auth/token
```

```java
@ExternalApi(name = "prodigy")
public interface ProdigyClient {
    @ExternalPost("/upload/presign")
    PreSignVO presignUpload(@Body PreSignRequest request);

    @ExternalGet("/study/{studyId}/info")
    StudyVO getStudy(@PathVar("studyId") Long studyId);
}
```

四种认证策略：`none` / `jwt-account`（自动缓存续期，token 过期前 60s 预刷新）/ `credentials` / `api-key`。

### 🔌 Feign 跨服务异常透传

`mcdull-framework-starter-feign` 解决微服务间 Feign 调用时异常信息丢失的问题：

- `FeignErrorDecoder`：HTTP 非 2xx 响应时统一日志记录 + 封装为可识别异常
- `ResultApiParse.getInstance()`：区分**系统异常**（`FeignServiceErrorException`）和**业务异常**（`FeignBizException`），调用方可分别捕获处理

### 🔬 Java Agent 零侵入性能探针

`mcdull-framework-agent` 通过 JVM `-javaagent` 参数挂载，**无需修改任何业务代码**，采集内容远超普通 APM 埋点：

| 采集维度 | 说明 |
|---|---|
| 方法耗时 | 精确到毫秒 |
| 入参 / 出参 | 序列化记录，便于问题复现 |
| 异常捕获 | 自动关联到对应方法调用 |
| TraceId | 与日志链路打通 |
| 堆内存快照 | 方法执行**前后**分别记录 init/used/committed/max 及使用率 |
| 非堆内存快照 | 同上，覆盖 Metaspace 等区域 |
| GC 明细 | 方法执行前后 GC 次数和时间，按 GC 类型分组 |

通过前后内存对比，可以精确定位是哪个方法调用引起了内存抖动或 GC 压力。采集结果写入 `service_log` + `service_gc_log` 两张表。

### 📊 JMX 运维管理端点

`ThreadPoolJmxAdapter`、`DatabaseJmxAdapter`、`RedisJmxAdapter` 三个 MBean，通过 JConsole 或 Jolokia（HTTP 协议）实时查看和调整线程池参数、连接池状态、Redis 连接情况，生产问题不需要发版即可临时处置。

### 🛠️ 开箱即用的工程防护

引入 starter 即自动生效，无需业务代码干预：

- **全局 String 自动 Trim**（`TrimStringConfig`）：Jackson 反序列化时对所有 String 字段做 `trim()`，防止首尾空格导致的数据异常
- **ERROR/WARN 日志告警扩展点**（`AlarmRollingFileAppender`）：继承 Logback `RollingFileAppender`，子类 override `title()` 后即可对接钉钉/企微/邮件告警
- **构建版本追踪**（`VersionInfoImplComponent`）：读取 `build-info.properties` 和 `git.properties`，通过 API 可获知线上运行的是哪次构建、哪个 commit
- **HTTP 安全响应头**（`HttpTraceLogFilter`）：每个响应自动添加 `X-Content-Type-Options`、`X-Frame-Options`、`X-XSS-Protection`


### ── 业务系统能力 ──

### 🔍 多维审计体系

mcdull 提供两个层面互相补充的审计能力，覆盖"谁做了什么"和"改了哪些字段"两个维度。

**① 操作日志（请求级）**

`OperationLogsAspect` 对所有 Controller 方法环绕拦截，零侵入自动采集：请求路径、HTTP Method、客户端 IP、User-Agent、请求参数（自动过滤 Stream/MultipartFile）、接口耗时、TraceId、是否成功。日志通过 `LogFeignClient` 异步写入日志中心，`AuditAspect` 同步提取权限码写入会话，实现操作与权限的完整关联。

**② 字段级变更审计（业务级）**

`AuditUtil` + `@AuditDescription` 注解实现字段级 diff，精确描述"改了什么"：

```java
public class UserAudit implements Audit {
    @AuditDescription(label = "账号", sort = 1)
    private String loginName;

    @AuditDescription(label = "邮箱", sort = 3)
    private String email;
}

// 自动对比并生成人类可读描述
String diffText = AuditUtil.compareStr(before, after);
// 输出：用户信息<邮箱:"old@example.com"更新为"new@example.com">
```

支持：仅新增记录初始值 / 变更对比只输出有变化的字段 / 自定义包裹字符和分隔符 / 字段排序 / 日期格式化。

已覆盖：用户、角色、菜单、部门、字典、配置、消息、表单等核心业务实体。

### 📄 动态表单 + 文档导出

**动态表单引擎**

完整的低代码表单运行时，表单结构以 JSON Schema 存储，无需改动数据库和代码即可新增表单：

- 支持嵌套布局（`children` 递归解析提取叶子节点）
- 11 种控件：input、textarea、select、radio、checkbox、switch、date、time、datetime、number、file
- 发布机制：未发布时可反复修改字段，发布后锁定结构保证数据一致性
- 查询时自动将 select/radio/checkbox/switch 的 value 反查 options 还原为 label 文本

**动态 Excel 表单**

`DynamicFieldTemplate` 驱动动态列导出：表头 `*` 自动标红，下拉选项写入隐藏 Sheet 公式约束，首行冻结 + 自动筛选，导入时多 Sheet 批量处理并自动跳过 index 页。

**PDF / Word / HTML 文档生成**

- `HtmlConvertPdf`：HTML 模板 → PDF，内嵌中英文字体，支持左/右页脚分离
- `updatePdfLeftFooter`：对已生成 PDF **增量写入页脚**，不重新渲染整个文档
- `ReplaceWordPlaceholder`：Word 模板 `${key}` 占位符替换，兼容 POI 分 Run 存储问题

### 📁 文件管理 — 菜单感知目录树 + OSS 多后端

上传文件时，系统自动感知当前操作的菜单路径，在目录树中按需创建对应层级并按月自动归档：

```
系统文件/
└── 用户管理/          ← 按当前菜单自动创建
    └── 导出记录/      ← 按当前菜单自动创建
        └── 2024-07/   ← 按月自动归档
            └── export_xxx.xlsx
```

`addIfAbsent(name, parentId)` 幂等创建目录，`FolderEntity` 存储 `idPath` 保证路径可追溯。`batchFileUpload(fileList, bizId, clazz)` 通过 `@TableName` 反射建立文件与任意业务表的通用关联，删除业务对象时级联清理关联文件。`OssService` 接口屏蔽底层存储实现，切换存储后端只需改配置。

### ⏲️ 数据库自动备份

启动时从系统配置表读取 cron 表达式，动态注册定时任务：dump SQL → zip 压缩 → 上传到文件系统"数据备份"目录，自动按日期命名。分布式锁防多实例重复执行，备份策略完全由配置驱动，无需重启服务。

### 🔢 业务序列号生成引擎

可配置的业务单号生成系统，不依赖 Redis 自增，支持持久化历史记录：

- **格式模板**：`{YYYY}{MM}{DD}{nnnnn}`，日期部分运行时动态替换，序号部分自动补零
- **步长随机偏移**（`stepRandomRange`）：每次生成在上次基础上随机步进，防止枚举攻击推测单号规律
- **业务类型隔离**：不同业务模块独立维护序列，互不干扰
- **批量生成**：单次可生成多个连续单号，历史记录持久化到 `sys_serial_number_record`

### 🧩 领域通用工具

**子表 Diff 引擎（`DomainEngine`）**

表单提交包含子表数据时，自动对比 DB 快照与前端提交，归类为三个操作集合：

```java
CompareBean<T, PK> result = DomainEngine.compare(dbList, frontEndList);
result.getInsertList();  // 新增项（前端有、DB 无）
result.getUpdateList();  // 更新项（前端有 ID）
result.getRemoveList();  // 删除项（DB 有、前端无）
```

**协同过滤推荐算法**

内置基于用户（UserCF）和基于物品（ItemCF）的协同过滤推荐算法，泛型设计 `<S, T>` 与具体业务完全解耦：

```java
List<T> items = UserCF.recommend(userId, relateDataList);
List<T> similar = ItemCF.recommend(itemId, relateDataList);
```


---

## 项目结构

```
mcdull/
├── mcdull-framework/          # 框架层 — 基础组件与通用功能
│   ├── mcdull-framework-dependencies   # 统一依赖管理
│   ├── mcdull-framework-base           # 基础组件
│   ├── mcdull-framework-config         # 配置管理
│   ├── mcdull-framework-agent          # Java Agent 性能探针
│   ├── mcdull-framework-enforcer       # 架构约束（ArchUnit）
│   └── mcdull-framework-starters/      # 功能 Starter 集合
│       ├── mcdull-framework-starter-doc        # API 文档（Knife4j）
│       ├── mcdull-framework-starter-external   # 外部系统对接
│       ├── mcdull-framework-starter-feign      # 服务调用
│       ├── mcdull-framework-starter-flow       # 流程编排
│       ├── mcdull-framework-starter-mongodb    # MongoDB 集成
│       ├── mcdull-framework-starter-monitor    # 监控
│       ├── mcdull-framework-starter-mysql      # MySQL 集成
│       ├── mcdull-framework-starter-nacos      # Nacos 集成
│       ├── mcdull-framework-starter-oss        # 对象存储
│       ├── mcdull-framework-starter-redis      # Redis 集成
│       ├── mcdull-framework-starter-security   # 安全认证（Sa-Token）
│       └── mcdull-framework-starter-web        # Web 基础
├── mcdull-bussiness/          # 业务层 — 具体业务实现（注：目录名含历史拼写）
│   ├── mcdull-business-common     # 业务通用组件
│   ├── mcdull-business-system     # 系统管理（用户、角色、权限、代码生成）
│   ├── mcdull-business-blaze      # 核心业务模块
│   ├── mcdull-business-focus      # Focus 业务模块
│   ├── mcdull-business-workflow   # 工作流业务
│   └── mcdull-business-demo       # 示例与最佳实践
├── mcdull-support/            # 支持层 — 运行支撑服务
│   ├── mcdull-gateway         # API 网关
│   ├── mcdull-mdc             # 元数据中心
│   ├── mcdull-monitor         # 监控服务
│   ├── mcdull-tools           # 开发工具
│   └── mcdull-ai              # AI 相关功能
├── doc/                       # 项目文档
└── sql/                       # 数据库初始化脚本
```

每个业务模块采用标准三层拆分：

```
business-module/
├── module-facade/      # 接口定义（API 契约、DTO、枚举）
├── module-impl/        # 业务实现（Controller / Service / Manager / DAO）
└── module-bootstrap/   # 启动入口
```

---

## 快速开始

### 环境要求

| 工具 | 版本要求 |
|---|---|
| JDK | 17+ |
| Maven | 3.6+ |
| MySQL | 5.7+ 或 8.0.14+ |
| Redis | 5.0+ |
| Nacos | 2.0+ |

### 安装与运行

**1. 克隆项目**

```bash
git clone https://gitee.com/dqcer/mcdull.git
cd mcdull
```

**2. 初始化数据库**

按顺序执行 `sql/` 目录下的脚本：

```bash
# 核心系统表结构（必须）
sql/common.sql
sql/3.0.sql
sql/sys_area.sql

# 可选：行政区域扩展数据
sql/0.1.1_sys_area.sql

# 可选：Focus 业务模块
sql/focus.sql

# 可选：工作流
sql/3.0_workflow_1.7.4.sql
sql/warm-flow.sql
```

**3. 配置 Nacos**

启动 Nacos 服务后，导入配置：
- 配置文件：`doc/nacos_config_export_20240421173102.zip`

修改业务服务的 `application-dev.yml`，填入本地数据库和 Redis 连接信息。

**4. 编译项目**

```bash
mvn clean install -DskipTests
```

**5. 启动服务**

按以下顺序启动：
1. Nacos（配置中心 + 服务注册）
2. `mcdull-gateway`（API 网关）
3. `mcdull-business-system`（系统核心服务，其他服务依赖）
4. 其他业务服务（按需启动）

**6. 启动前端**

配套前端工程：[mcdull-vue3](https://gitee.com/dqcer/mcdull-vue3)，具体运行方式参见前端工程的 README。

---

## 文档

详细文档位于 `doc/project/` 目录：

| 文档 | 说明 |
|---|---|
| [deploy.md](doc/project/deploy.md) | 部署教程 |
| [database.md](doc/project/database.md) | 数据库设计规范 |
| [date.md](doc/project/date.md) | 多时区与日期格式使用说明 |
| [archunit.md](doc/project/archunit.md) | ArchUnit 架构约束使用说明 |
| [login.md](doc/project/login.md) | 登录认证接口说明 |

---

## 开发指南

### 代码规范

- 遵循 Java 编码规范，使用 Lombok 简化代码
- 统一异常处理与日志管理
- 严格遵守 Controller → Service → Manager → DAO 四层架构
- 模块间通信通过 facade 接口，禁止跨模块直接依赖 impl 层
- 使用 ArchUnit 单元测试验证架构约束，CI 阶段强制通过

### 开发流程

1. 从 `master` 创建功能分支
2. 完成功能开发和测试（包含 ArchUnit 架构检查）
3. 提交代码，发起 Pull Request
4. 代码审查通过后合并

---

## 部署说明

### 本地开发

- IDE：IntelliJ IDEA（推荐）
- 配置文件：`application-dev.yml`
- 数据库 / Redis：本地实例

### 生产环境

- 部署方式：Docker 容器
- 配置文件：`application-prod.yml`
- 数据库：MySQL 集群
- 缓存：Redis 集群
- 服务注册与配置：Nacos 集群

---

## 贡献

欢迎提交 Issue 和 Pull Request。

1. Fork 本仓库
2. 创建功能分支：`git checkout -b feature/your-feature`
3. 提交改动：`git commit -m 'feat: add some feature'`
4. 推送分支：`git push origin feature/your-feature`
5. 发起 Pull Request

---

## License

本项目基于 [Apache License 2.0](LICENSE) 开源。

---

## 联系

- 作者：dqcer
- 邮箱：dqcer@sina.com
- Gitee：[https://gitee.com/dqcer/mcdull](https://gitee.com/dqcer/mcdull)
