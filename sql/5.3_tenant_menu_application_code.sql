-- ============================================================
-- 5.3 租户管理 · 菜单应用归属增量修正脚本
--
-- 目的：按前端实际的应用上下文，修正 sys_menu.application_code。
--   - 租户管理 / 数据源配置仍属于 system 平台应用
--   - 工作流中心相关菜单归入 workflow
--   - AI / Agent 治理 / 表单设计器按权限码或路径前缀识别
--   - 未命中规则的存量菜单保持 system
--
-- 依赖：5.1_tenant.sql 已新增 sys_menu.application_code；
--       本脚本自带幂等加列保护，可重复执行。
-- ============================================================

-- ------------------------------------------------------------
-- 0. 确保 application_code 字段存在（可重放）
-- ------------------------------------------------------------
SET @ddl = (
    SELECT IF(COUNT(*) > 0, 'DO 0',
              'ALTER TABLE `sys_menu` ADD COLUMN `application_code` varchar(64) NOT NULL DEFAULT ''system'' COMMENT ''应用code（默认 system）''')
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_menu'
      AND COLUMN_NAME = 'application_code'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ------------------------------------------------------------
-- 1. 存量空值兜底：默认归 system
-- ------------------------------------------------------------
UPDATE `sys_menu`
SET `application_code` = 'system',
    `updated_time` = sysdate(),
    `updated_by` = 1
WHERE `application_code` IS NULL
   OR `application_code` = '';

-- ------------------------------------------------------------
-- 2. 租户管理 / 数据源配置：强制归 system（前端属于平台系统管理域）
-- ------------------------------------------------------------
UPDATE `sys_menu`
SET `application_code` = 'system',
    `updated_time` = sysdate(),
    `updated_by` = 1
WHERE `id` BETWEEN 500 AND 514;

-- ------------------------------------------------------------
-- 3. 工作流中心：按前端实际菜单 ID 与路径/权限码归 workflow
--    297/298：流程管理 / 流程定义
--    400-407：工作流中心页面与按钮
-- ------------------------------------------------------------
UPDATE `sys_menu`
SET `application_code` = 'workflow',
    `updated_time` = sysdate(),
    `updated_by` = 1
WHERE `del_flag` = 0
  AND (
      `id` IN (297, 298, 400, 401, 402, 403, 404, 405, 406, 407)
      OR `api_perms` LIKE 'workflow:%'
      OR `web_perms` LIKE 'workflow:%'
      OR `path` LIKE '/system/workflow/%'
      OR `path` LIKE '/workflow/%'
  );

-- ------------------------------------------------------------
-- 4. 其他应用：按前端 portal 注册的应用编码识别
-- ------------------------------------------------------------
UPDATE `sys_menu`
SET `application_code` = 'ai',
    `updated_time` = sysdate(),
    `updated_by` = 1
WHERE `del_flag` = 0
  AND (
      `api_perms` LIKE 'ai:%'
      OR `web_perms` LIKE 'ai:%'
      OR `path` LIKE '/ai/%'
  );

UPDATE `sys_menu`
SET `application_code` = 'gov',
    `updated_time` = sysdate(),
    `updated_by` = 1
WHERE `del_flag` = 0
  AND (
      `api_perms` LIKE 'agent:%'
      OR `web_perms` LIKE 'agent:%'
      OR `path` LIKE '/agent/%'
  );

UPDATE `sys_menu`
SET `application_code` = 'form',
    `updated_time` = sysdate(),
    `updated_by` = 1
WHERE `del_flag` = 0
  AND (
      `api_perms` LIKE 'form:%'
      OR `web_perms` LIKE 'form:%'
      OR `path` LIKE '/form/%'
  );

-- ------------------------------------------------------------
-- 5. 超级管理员角色（role_id=1）补授权，可重放
-- ------------------------------------------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`)
SELECT 1, m.id, sysdate()
FROM `sys_menu` m
WHERE m.id BETWEEN 500 AND 514
  AND m.del_flag = 0
  AND NOT EXISTS (
      SELECT 1 FROM `sys_role_menu` rm
      WHERE rm.role_id = 1 AND rm.menu_id = m.id AND rm.del_flag = 0
  );

-- ------------------------------------------------------------
-- 6. 校验（可选）
-- ------------------------------------------------------------
-- SELECT id, menu_name, path, api_perms, application_code
-- FROM sys_menu
-- WHERE del_flag = 0
-- ORDER BY application_code, parent_id, sort, id;
