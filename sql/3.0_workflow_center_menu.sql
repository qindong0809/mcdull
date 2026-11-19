-- 工作流中心菜单（配合 mcdull-vue3 的 src/views/system/workflow/center/ 页面）
-- 执行方式：在已有 3.0.sql 初始化后的库中执行本脚本

INSERT INTO `sys_menu` VALUES(400, '工作流中心', 1, 0, 98, NULL, NULL, 1, NULL, NULL, 'PartitionOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), 1, NULL);

INSERT INTO `sys_menu` VALUES(401, '工作流概览', 2, 400, 1, '/system/workflow/center/overview', '/system/workflow/center/overview.vue', 1, NULL, NULL, 'DashboardOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL);
INSERT INTO `sys_menu` VALUES(402, '流程定义', 2, 400, 2, '/system/workflow/center/definition', '/system/workflow/center/definition.vue', 1, 'workflow:center:definition:list', 'workflow:center:definition:list', 'BookOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL);
INSERT INTO `sys_menu` VALUES(403, '流程设计器', 2, 400, 3, '/system/workflow/center/designer', '/system/workflow/center/designer.vue', 1, 'workflow:center:definition:save', 'workflow:center:definition:save', 'FormOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL);
INSERT INTO `sys_menu` VALUES(404, '流程实例', 2, 400, 4, '/system/workflow/center/instance', '/system/workflow/center/instance.vue', 1, 'workflow:center:instance:list', 'workflow:center:instance:list', 'PlayCircleOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL);
INSERT INTO `sys_menu` VALUES(405, '待办任务', 2, 400, 5, '/system/workflow/center/todo', '/system/workflow/center/todo.vue', 1, 'workflow:center:task:handle', 'workflow:center:task:handle', 'CheckCircleOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL);
INSERT INTO `sys_menu` VALUES(406, '历史任务', 2, 400, 6, '/system/workflow/center/history', '/system/workflow/center/history.vue', 1, 'workflow:center:history:list', 'workflow:center:history:list', 'HistoryOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL);
INSERT INTO `sys_menu` VALUES(407, '流程图', 2, 400, 7, '/system/workflow/center/diagram', '/system/workflow/center/diagram.vue', 1, NULL, NULL, 'ShareAltOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL);

-- 为超级管理员角色（role_id = 1）授权，如系统中角色编号不同请按需调整
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 400, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 401, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 402, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 403, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 404, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 405, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 406, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 407, sysdate());
