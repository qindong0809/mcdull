# 发布包（release）

本目录按版本组织 SQL 发布物。

## 目录职责

```text
release/
├─ sql/                  # 源脚本（历史增量，冻结归档，不再改动）
│   ├─ 00_bootstrap.sql  # 建台账表 sys_sql_log
│   └─ *.sql             # 历史版本增量脚本
├─ v2.0.0/               # v2.0.0 版本发布包
│   ├─ system/
│   │   ├─ v2.0.0_system.full.sql       # 全量：当前 schema 快照（手工维护）
│   │   └─ v2.0.0_system_upgrade.sql    # 增量：本次版本变更（手工维护）
│   └─ workflow/
│       ├─ v2.0.0_workflow.full.sql     # 全量：当前 schema 快照（手工维护）
│       └─ v2.0.0_workflow_upgrade.sql  # 增量：本次版本变更（手工维护）
├─ ORDER.txt             # 执行顺序
└─ CHECKSUMS.txt         # 产物校验和
```

## 全量与增量的维护规则（重要）

**全量不是历史增量脚本的拼接**，而是当前 schema 的干净快照：

- 每张表只有**一条最终 `CREATE TABLE`**，包含所有历史 ALTER 的结果（如 `sys_menu.application_code`、`sys_user.login_name_key`、workflow 的 `revision` 字段等）。
- 后续变更表结构时，**直接修改全量脚本中该表的 `CREATE TABLE` 定义**（原位修改，不在末尾追加）。
- 同时在对应 `*_upgrade.sql` 中写本次版本的 `ALTER`/数据变更，供老库升级。
- 两份文件必须结果一致；CI 应对拍验证：执行全量后的 schema = 顺序执行历史增量后的 schema。

禁止做法：
- 禁止把增量脚本原文追加到全量文件末尾；
- 禁止在全量里保留 `DROP TABLE`、`ALTER TABLE`、`SET @ddl`、`PREPARE` 等过程性语句；
- 禁止手工编辑 `sql/` 下已发布的历史增量。

## 使用方式

- **全新库**：先执行 `sql/00_bootstrap.sql`，再按 `ORDER.txt` 执行对应系统的 `*.full.sql`。
- **已有库升级**：按 `ORDER.txt` 执行对应系统的 `*_upgrade.sql`。
- 模块变更完成后同步更新 `CHECKSUMS.txt`。

## 台账

所有脚本末尾都写 `sys_sql_log`，重复执行不会产生重复记录。查询已执行版本：

```sql
SELECT system, scope, version, script_name, applied_time
FROM sys_sql_log
ORDER BY system, version;
```

## 约定

- 历史版本全量按 Git Tag 归档，不在此目录逐版本堆积。
- `seed/dev_test` 类数据不进生产发布包。
