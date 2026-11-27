-- ============================================================
-- 5.1 租户管理 · 菜单与按钮权限脚本
--
-- 依赖：3.0.sql 已创建 sys_menu / sys_role_menu；5.1_tenant.sql 已新增 sys_menu.application_code。
-- 本脚本自带 application_code 幂等加列保护，可重复执行。
--
-- 菜单归属：全部为 system 应用（平台系统管理域），不按租户开通的 OA/HR/CRM 配置。
-- 权限串：system:tenant:* / system:datasource:*。
-- 父菜单：系统设置 sys_menu.id = 50（来自 3.0.sql）。
-- 菜单 ID 使用 500-513 段，避开 3.0.sql 与 3.0_workflow_center_menu.sql 已用 ID。
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
-- 1. 页面菜单
-- ------------------------------------------------------------
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(500, '租户管理', 2, 50, 7, '/system/tenant', '/system/tenant/tenant-list.vue', 1,
 'system:tenant:query', 'system:tenant:query', 'ClusterOutlined', NULL, 0, NULL,
 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');

-- 隐藏页面：租户配置工作台，由租户列表操作列“配置”按钮进入，不在侧边菜单展示。
-- visible_flag=0 仅隐藏菜单；path/component 存在，buildRoutes 仍会生成动态路由。
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(507, '租户配置工作台', 2, 500, 8, '/system/tenant/setting', '/system/tenant/components/tenant-setting/index.vue', 1,
 'system:tenant:query', 'system:tenant:query', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(510, '数据源配置', 2, 50, 8, '/system/datasource', '/system/datasource/datasource-list.vue', 1,
 'system:datasource:query', 'system:datasource:query', 'DatabaseOutlined', NULL, 0, NULL,
 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');

-- ------------------------------------------------------------
-- 2. 租户管理按钮权限（context_menu_id = 500）
-- ------------------------------------------------------------
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(501, '查询', 3, 500, 1, NULL, NULL, 1,
 'system:tenant:query', 'system:tenant:query', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(502, '写操作', 3, 500, 2, NULL, NULL, 1,
 'system:tenant:write', 'system:tenant:write', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(503, '删除租户', 3, 500, 3, NULL, NULL, 1,
 'system:tenant:delete', 'system:tenant:delete', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(504, '初始化', 3, 500, 4, NULL, NULL, 1,
 'system:tenant:init', 'system:tenant:init', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(505, '配置租户用户', 3, 500, 5, NULL, NULL, 1,
 'system:tenant:write', 'system:tenant:write', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(506, '配置菜单白名单', 3, 500, 6, NULL, NULL, 1,
 'system:tenant:write', 'system:tenant:write', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

-- ------------------------------------------------------------
-- 3. 数据源配置按钮权限（context_menu_id = 510）
-- ------------------------------------------------------------
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(511, '查询', 3, 510, 1, NULL, NULL, 1,
 'system:datasource:query', 'system:datasource:query', NULL, 510, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(512, '写操作', 3, 510, 2, NULL, NULL, 1,
 'system:datasource:write', 'system:datasource:write', NULL, 510, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(513, '删除数据源', 3, 510, 3, NULL, NULL, 1,
 'system:datasource:delete', 'system:datasource:delete', NULL, 510, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(514, '测试连接', 3, 510, 4, NULL, NULL, 1,
 'system:datasource:test', 'system:datasource:test', NULL, 510, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');

-- ------------------------------------------------------------
-- 4. 为超级管理员角色（role_id = 1）授权，可重放
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
