-- ============================================================
-- 账号唯一性 + 字符集统一 增量迁移脚本（可重放）
--
-- 背景：
--   1. sys_user / app_user  historically 只有主键，账号重复靠应用层全表扫描校验，
--      并发下存在竞态；本脚本用「生成列 + 条件唯一索引」在数据库层兜底。
--   2. 大小写视为同一个账号；允许删除后重建同名账号。
--      生成列 login_name_key 仅在 del_flag=0 时取小写账号，删除后置 NULL。
--      MySQL 唯一索引允许多个 NULL，因此已删除记录互不冲突、同名可重建。
--   3. 统一 utf8mb4 / utf8mb4_unicode_ci，避免 4 字节字符（emoji 等）写入失败。
--
-- 目标环境：MySQL 5.7+ / 8.0+（生成列 STORED 自 5.7 起支持）
-- 执行方式：在已有库上执行；全新安装仍按 README 顺序执行 3.0.sql 等基础脚本即可。
-- ============================================================

-- ------------------------------------------------------------
-- 0. 前置检查：若下列查询返回行，必须先人工清理重复账号再继续
--    注意：大小写视为相同，故用 LOWER() 分组
-- ------------------------------------------------------------
-- SELECT LOWER(login_name) AS k, COUNT(*) AS c
-- FROM sys_user WHERE del_flag = 0 GROUP BY k HAVING c > 1;
-- SELECT LOWER(login_name) AS k, COUNT(*) AS c
-- FROM app_user WHERE del_flag = 0 GROUP BY k HAVING c > 1;

-- ------------------------------------------------------------
-- 1. sys_user：字符集 -> 生成列 -> 唯一索引
--    可重放：先删索引与生成列（若存在），再重建。
-- ------------------------------------------------------------
ALTER TABLE `sys_user`
    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- MySQL 5.7/8.0 均不支持 DROP COLUMN IF EXISTS，用 information_schema 条件判断保证可重放。
-- 删除列会自动移除其上的索引，无需单独 DROP INDEX。
SET @ddl = (
    SELECT IF(COUNT(*) > 0,
              'ALTER TABLE `sys_user` DROP COLUMN `login_name_key`',
              'DO 0')
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_user'
      AND COLUMN_NAME = 'login_name_key'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

ALTER TABLE `sys_user`
    ADD COLUMN `login_name_key` varchar(64)
        GENERATED ALWAYS AS (CASE WHEN `del_flag` = 0 THEN LOWER(`login_name`) ELSE NULL END) STORED
        COMMENT '活跃账号唯一键：删除后置 NULL 允许同名重建，小写化统一大小写',
    ADD UNIQUE KEY `uk_login_name_active` (`login_name_key`);

-- ------------------------------------------------------------
-- 2. app_user：先收窄 login_name（512 -> 64），再建生成列与唯一索引
--    若现有数据存在超过 64 字符的账号，需先人工处理。
-- ------------------------------------------------------------
ALTER TABLE `app_user`
    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

SET @ddl = (
    SELECT IF(COUNT(*) > 0,
              'ALTER TABLE `app_user` DROP COLUMN `login_name_key`',
              'DO 0')
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'app_user'
      AND COLUMN_NAME = 'login_name_key'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

ALTER TABLE `app_user`
    MODIFY COLUMN `login_name` varchar(64) NOT NULL COMMENT '登录账号',
    ADD COLUMN `login_name_key` varchar(64)
        GENERATED ALWAYS AS (CASE WHEN `del_flag` = 0 THEN LOWER(`login_name`) ELSE NULL END) STORED
        COMMENT '活跃账号唯一键：删除后置 NULL 允许同名重建，小写化统一大小写',
    ADD UNIQUE KEY `uk_login_name_active` (`login_name_key`);

-- ------------------------------------------------------------
-- 3. 全库字符集统一：把仍非 utf8mb4 的表转为 utf8mb4
--    由 information_schema 动态生成语句；MySQL 不支持在一条 ALTER 中
--    直接遍历所有表，故此处生成 SQL 供人工/脚本复核后执行。
-- ------------------------------------------------------------
SELECT CONCAT(
    'ALTER TABLE `', TABLE_SCHEMA, '`.`', TABLE_NAME,
    '` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;'
) AS ddl
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_TYPE = 'BASE TABLE'
  AND TABLE_COLLATION NOT LIKE 'utf8mb4%'
ORDER BY TABLE_NAME;

-- ------------------------------------------------------------
-- 4. 验证
-- ------------------------------------------------------------
-- SHOW FULL COLUMNS FROM `sys_user` WHERE Field IN ('login_name','login_name_key','del_flag');
-- SHOW INDEX FROM `sys_user` WHERE Key_name = 'uk_login_name_active';
-- SHOW INDEX FROM `app_user` WHERE Key_name = 'uk_login_name_active';
