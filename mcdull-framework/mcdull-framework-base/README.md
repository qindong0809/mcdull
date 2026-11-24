# mcdull-framework-base

基础契约层：69 个 Java / 15 包。只定义稳定“形状”，不承载业务语义。

非 BOM、非 Starter、无自动装配；易变值集合禁止下沉。

## 接入

```xml
<dependency>
  <groupId>io.gitee.dqcer</groupId>
  <artifactId>mcdull-framework-base</artifactId>
  <version>${revision}</version>
</dependency>
```

## 注意

- `InnerResult`：`unwrap()` 失败还原异常且 null 抛错；`unwrapOrNull()` 允许 null；`checkOk()` 只校验状态。
- `@UnAuthorize` 当前无人消费；放行走安全模块白名单或 `@SaIgnore`。
- `@EnumsIntValid(required=false)` 直接放行，不做枚举反查。
- `@Transform` 由 `starter-web`、`@SchemaEnum` 由 `starter-doc` 消费；仅引入 base 不生效。
- 新增/删除公开类时同步更新上表。

