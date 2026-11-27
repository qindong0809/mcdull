-- ============================================================
-- v2.0.0 增量脚本 - workflow 系统
-- 用途：从上一版本升级到 v2.0.0；在已执行过旧版本的库上执行
-- 注意：全量建库请使用 v2.0.0_workflow.full.sql，不要执行本文件
-- ============================================================

-- 在此填写本次版本的增量 DDL / DML，要求可重复执行：
--   CREATE TABLE IF NOT EXISTS ...
--   ALTER TABLE ... ADD COLUMN IF NOT EXISTS ...（MySQL 5.7 需用 information_schema 守卫）
--   INSERT IGNORE INTO ... / INSERT ... ON DUPLICATE KEY UPDATE ...

-- 示例（取消注释后按实际改写）：
-- ALTER TABLE `flow_definition` ADD COLUMN `new_col` varchar(64) default null comment '新字段';

INSERT IGNORE INTO sys_sql_log(system, scope, version, script_name, applied_time)
VALUES ('workflow', 'master', 'v2.0.0', 'v2.0.0_workflow_upgrade', NOW());
