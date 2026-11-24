# mcdull-framework-enforcer

> 架构与编码规约规则库。规则由本模块提供，**由每个业务模块在自己的 `src/test` 中声明 `@ArchTest` 落地**。

## 快速开始

- 引入依赖（test scope，版本由 BOM 管理）

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>mcdull-framework-enforcer</artifactId>
    <scope>test</scope>
</dependency>
```

- 声明测试类

```java
@AnalyzeClasses(
        packages = "io.gitee.dqcer.mcdull.system",
        importOptions = ImportOption.DoNotIncludeTests.class)
class SystemArchitectureTest {

    @ArchTest
    static final ArchRule LAYER_CALL_RULES = ArchitectureEnforcer.layerChecks();

    @ArchTest
    static final ArchRule DO_NAMING_RULES = ArchitectureEnforcer.doNamingRules();

    @ArchTest
    static final ArchRule FACADE_MUST_NOT_DEPEND_ON_PROVIDER =
            ArchitectureEnforcer.facadeDependencyRules();
}
```

### 三条必须知道的坑

1. **`@ArchTest` 字段类型只能是 `ArchRule`。** 形如 `static final List<ArchRule> rules = ...` 的写法不被
   ArchUnit 引擎支持，会在测试发现阶段抛初始化异常。每条规则单独声明一个字段，违规报告才能按规则定位。
2. **不要使用 `ImportOption.Predefined.DO_NOT_INCLUDE_JARS`。** 本项目的 `xxx-facade` 在测试 classpath 上
   以 jar 形式存在，排除 jar 会让 `..facade..` 内无类可查，`facadeDependencyRules()` 命中 0 条并静默通过。
   正确做法是用 `packages` 限定包根，保留 jar 扫描。
3. **0 命中不等于通过。** 部分命名规则带 `allowEmptyShould(true)`，空集合也会 PASS。看到诊断报告出现
   `PASS 0` 时，必须先确认 `classes.size() > 0` 且包路径与代码一致，否则属于守护静默失效。

## 内置规则

### 分层调用（`layerChecks()`）

| 层 | 包匹配 | 允许的被访问方 |
| --- | --- | --- |
| Controller | `..web.controller..` | 不允许被任何层访问 |
| Entry | `..web.feign..`、`..web.def..`、`..flow..`、`..config..` | 不约束 |
| Service | `..web.service..` | Controller、Entry、Manager |
| Manager | `..web.manager..` | Service、Entry |
| Mapper | `..web.mapper..` | Service、Manager |

Entry 不约束被访问方，是因为 `config` 下的 Jackson 序列化器与常量会被 `model.dto` / `model.vo`
反向引用；把它当作「只能向外」的入口层会产生大量假阳性。

设计取舍说明：

- 未定义 `Repository` 层。MyBatis-Plus 的 `ServiceImpl<M, T>` 使 Service 必然直接依赖 Mapper，
  `Repository` 只存在于 `code-generator-template`，手写链路不经过它。
- `Manager` 允许调用 `Service`。这是 system-impl 的既有形态（实测 46 处），当前按现实约束，
  属于已登记的技术债，收敛时应反向修改代码而不是继续放宽规则。

### 命名与角色归属

命名**按包**判定，角色归属**按标记接口**判定，两套判定不可混用：

| 规则 | 判定依据 | 要求 |
| --- | --- | --- |
| `mapperNamingRules()` | 包 `..mapper..` | 类名以 `Mapper` 结尾 |
| `dtoNamingRules()` | 包 `..dto..` | 类名以 `DTO` 结尾 |
| `voNamingRules()` | 包 `..vo..` | 类名以 `VO` 结尾 |
| `entityNamingRules()` | 包 `..entity..` | 类名以 `Entity` 结尾 |
| `boNamingRules()` | 包 `..bo..` | 类名以 `BO` 结尾 |
| `enumNamingRules()` | `areEnums()` | 类名以 `Enum` 结尾 |
| `dtoPlacementRules()` | `implement(DTO)` | 必须位于 `..dto..` |
| `voPlacementRules()` | `implement(VO)` | 必须位于 `..vo..` |
| `entityPlacementRules()` | `implement(Entity)` | 必须位于 `..entity..` |

为什么必须拆开：`implement(..)` 取的是**传递闭包**。历史上把两者混在一条命名规则里，
`XxxQueryDTO extends PagedDTO` 这类完全合法的继承会被误报，而真正该报的
`MenuVO extends MenuEntity`（视图对象继承持久化对象，把 7 个审计字段带进响应契约）
却只得到一句「你的名字没起对」。拆开后命名规则抓漂移、位置规则抓角色错位，各说各的事。

`BO` 只有命名规则、没有位置规则，是同一个传递闭包特性的主动取舍：
`LogonVO extends UserBO` 让 VO 间接 `implement(BO)`，而视图对象复用业务对象的字段
并不泄漏持久化语义，加 `implement(BO) ⇒ ..bo..` 只会把合法写法报成位置错误。
BO 的错位（如 `MenuInfoBO` 曾放在 `..vo..`）由 `boNamingRules()` 与 `voNamingRules()` 双向夹住。

命名规则排除 `package-info` 与嵌套类：前者是包级元数据，后者的名字由外层类名决定
（Lombok `@Builder` 生成的 `Xxx$XxxBuilder` 属于 static nested class，
注意 `areNotInnerClasses()` 排除不掉它，必须用 `areNotNestedClasses()`）。

### 依赖与编码约束

- `..facade..` 不得依赖 `..provider..`
- 禁止 `System.out` / `System.err` / `e.printStackTrace()`
- 禁止直接抛出 `Exception` / `RuntimeException` / `Throwable`

## 结果只在 `clean` 之后可信

ArchUnit 分析的是 `target/classes` 里的字节码，不是源码。项目里存在过改名后遗留的
`TableQueryForm.class`（源码早已是 `TableQueryFormDTO`）和被修改过但未重编译的
`DatabaseBackupTaskExecutor.class`，两者都产生了源码里根本不存在的违规。

因此跑架构检查前必须 `clean`；CI 门禁同理，否则违规数会随本地残留产物漂移。

## 存量合规怎么办

不要在框架 jar 里放 `archunit_ignore_patterns.txt`。这类全局豁免文件在 classpath 上同名且只生效一份，
理由不可追溯，历史上还出现过豁免目标类早已被删除、规则却仍在被静默削弱的情况。

正确做法是在测试类上用 `FreezingArchRule` 冻结基线，只拦截新增违规：

```java
@ArchTest
static final ArchRule LAYER_CALL_RULES =
        FreezingArchRule.freeze(ArchitectureEnforcer.layerChecks());
```

基线落在 `src/test/resources/archunit.properties` 配置的目录下，首次使用需要显式允许创建，
否则规则会以「store 不存在」直接失败：

```properties
# src/test/resources/archunit.properties
freeze.store.default.path=archunit_store
freeze.store.default.allowStoreCreation=true
```

生成的 `archunit_store/` 目录需要纳入版本库——它就是这份架构债务的账本，评审时看它的增减即可。

## 参考实现

`mcdull-business/mcdull-business-system/system-impl/src/test/java/io/gitee/dqcer/mcdull/system/SystemArchitectureTest.java`

运行（先备依赖，再单独跑目标测试）：

```bash
# 1) 构建依赖模块；只想快速准备依赖时可临时加 -DskipTests，正式构建请保持默认
mvn -B -pl mcdull-business/mcdull-business-system/system-impl -am install

# 2) 单独跑某一个架构测试类
mvn -B -pl mcdull-business/mcdull-business-system/system-impl test -Dtest=SystemArchitectureTest
```

> 全量跑架构检查直接 `mvn test` 即可：各业务模块的 `*ArchitectureTest` 会在自己的测试阶段执行，无需额外配置。
> 注意 `-DskipTests` 会把架构测试一起跳过，因此不要用于正式构建或提交前的自检。
