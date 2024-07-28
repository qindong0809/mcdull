/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : mcdull-cloud-3

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 28/07/2024 17:32:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `menu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `menu_type` int NOT NULL COMMENT '类型 1/目录 2/菜单 3/功能',
  `parent_id` int NOT NULL COMMENT '父菜单id',
  `sort` int NULL DEFAULT NULL COMMENT '显示顺序',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `perms_type` int NULL DEFAULT NULL COMMENT '权限类型',
  `api_perms` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '后端权限字符串',
  `web_perms` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '前端权限字符串',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `context_menu_id` int NULL DEFAULT NULL COMMENT '功能点关联菜单id',
  `frame_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为外链',
  `frame_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '外链地址',
  `cache_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否缓存',
  `visible_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '显示状态',
  `inactive` tinyint NOT NULL DEFAULT 0 COMMENT '状态（true/已失活 false/未失活）',
  `del_flag` tinyint NOT NULL DEFAULT 0 COMMENT '删除标识（true/已删除 false/未删除）',
  `created_by` int NOT NULL COMMENT '创建人',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_by` int NULL DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 258 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (26, '菜单管理', 2, 50, 1, '/menu/list', '/system/menu/menu-list.vue', NULL, NULL, NULL, 'CopyOutlined', NULL, 0, NULL, 1, 1, 0, 0, 2, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (40, '删除', 3, 26, NULL, NULL, NULL, 1, 'system:menu:batchDelete', 'system:menu:batchDelete', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (45, '部门员工', 1, 0, 3, '/organization', NULL, NULL, NULL, NULL, 'UserSwitchOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (46, '部门员工', 2, 45, 1, '/employee/department', '/system/employee/department/index.vue', NULL, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (47, '商品管理', 2, 48, 1, '/erp/goods/list', '/business/erp/goods/goods-list.vue', NULL, NULL, NULL, 'AliwangwangOutlined', NULL, 0, NULL, 1, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:24:10');
INSERT INTO `sys_menu` VALUES (48, '商品管理', 1, 137, 10, '/goods', NULL, NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:24:13');
INSERT INTO `sys_menu` VALUES (50, '系统设置', 1, 0, 6, '/setting', NULL, NULL, NULL, NULL, 'SettingOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (76, '角色管理', 2, 45, 2, '/employee/role', '/system/employee/role/index.vue', NULL, NULL, NULL, 'SlidersOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (78, '商品分类', 2, 48, 2, '/erp/catalog/goods', '/business/erp/catalog/goods-catalog.vue', NULL, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:48');
INSERT INTO `sys_menu` VALUES (79, '自定义分组', 2, 48, 3, '/erp/catalog/custom', '/business/erp/catalog/custom-catalog.vue', NULL, NULL, NULL, 'AppstoreAddOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:34');
INSERT INTO `sys_menu` VALUES (81, '请求监控', 2, 111, 3, '/support/operate-log/operate-log-list', '/support/operate-log/operate-log-list.vue', NULL, NULL, NULL, 'VideoCameraOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (86, '添加部门', 3, 46, 1, NULL, NULL, 1, 'system:department:add', 'system:department:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (87, '修改部门', 3, 46, 2, NULL, NULL, 1, 'system:department:update', 'system:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (88, '删除部门', 3, 46, 3, NULL, NULL, 1, 'system:department:delete', 'system:department:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (91, '添加员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:add', 'system:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (92, '编辑员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:update', 'system:employee:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (93, '禁用启用员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:disabled', 'system:employee:disabled', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (94, '调整员工部门', 3, 46, NULL, NULL, NULL, 1, 'system:employee:department:update', 'system:employee:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (95, '重置密码', 3, 46, NULL, NULL, NULL, 1, 'system:employee:password:reset', 'system:employee:password:reset', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (96, '删除员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:delete', 'system:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (97, '添加角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:add', 'system:role:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (98, '删除角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:delete', 'system:role:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (99, '编辑角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:update', 'system:role:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (100, '更新数据范围', 3, 76, NULL, NULL, NULL, 1, 'system:role:dataScope:update', 'system:role:dataScope:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (101, '批量移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:batch:delete', 'system:role:employee:batch:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (102, '移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:delete', 'system:role:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (103, '添加员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:add', 'system:role:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (104, '修改权限', 3, 76, NULL, NULL, NULL, 1, 'system:role:menu:update', 'system:role:menu:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (105, '添加', 3, 26, NULL, NULL, NULL, 1, 'system:menu:add', 'system:menu:add', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (106, '编辑', 3, 26, NULL, NULL, NULL, 1, 'system:menu:update', 'system:menu:update', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (109, '参数配置', 2, 50, 3, '/config/config-list', '/support/config/config-list.vue', NULL, NULL, NULL, 'AntDesignOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (110, '数据字典', 2, 50, 4, '/setting/dict', '/support/dict/index.vue', NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (111, '监控服务', 1, 0, 100, '/monitor', NULL, NULL, NULL, NULL, 'BarChartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (114, '运维工具', 1, 0, 200, NULL, NULL, NULL, NULL, NULL, 'NodeCollapseOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (117, 'Reload', 2, 50, 12, '/hook', '/support/reload/reload-list.vue', NULL, NULL, NULL, 'ReloadOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (122, '数据库监控', 2, 111, 4, '/support/druid/index', NULL, NULL, NULL, NULL, 'ConsoleSqlOutlined', NULL, 1, 'http://mcdull.io:8090/druid', 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (130, '单号管理', 2, 50, 6, '/support/serial-number/serial-number-list', '/support/serial-number/serial-number-list.vue', NULL, NULL, NULL, 'NumberOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (132, '通知公告', 2, 0, 2, '/oa/notice/notice-list', '/business/oa/notice/notice-list.vue', NULL, NULL, NULL, 'SoundOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:25:18');
INSERT INTO `sys_menu` VALUES (133, '缓存管理', 2, 50, 11, '/support/cache/cache-list', '/support/cache/cache-list.vue', NULL, NULL, NULL, 'BorderInnerOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (137, '进销存系统', 1, 0, 2, NULL, NULL, NULL, NULL, NULL, 'AccountBookOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:24:15');
INSERT INTO `sys_menu` VALUES (138, 'OA系统', 1, 0, 1, NULL, NULL, NULL, NULL, NULL, 'BankOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:25:22');
INSERT INTO `sys_menu` VALUES (142, '公告详情', 2, 132, NULL, '/oa/notice/notice-detail', '/business/oa/notice/notice-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (143, '登录日志', 2, 213, 2, '/support/login-log/login-log-list', '/support/login-log/login-log-list.vue', NULL, NULL, NULL, 'LoginOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (144, '企业信息', 2, 138, 1, '/oa/enterprise/enterprise-list', '/business/oa/enterprise/enterprise-list.vue', NULL, NULL, NULL, 'ShopOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:11');
INSERT INTO `sys_menu` VALUES (145, '企业详情', 2, 138, NULL, '/oa/enterprise/enterprise-detail', '/business/oa/enterprise/enterprise-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:22:56');
INSERT INTO `sys_menu` VALUES (147, '帮助文档', 2, 218, 1, '/help-doc/help-doc-manage-list', '/support/help-doc/management/help-doc-manage-list.vue', NULL, NULL, NULL, 'FolderViewOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (148, '意见反馈', 2, 218, 2, '/feedback/feedback-list', '/support/feedback/feedback-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (149, '我的通知', 2, 132, NULL, '/oa/notice/notice-employee-list', '/business/oa/notice/notice-employee-list.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (150, '我的通知公告详情', 2, 132, NULL, '/oa/notice/notice-employee-detail', '/business/oa/notice/notice-employee-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (151, '代码生成器', 2, 0, 0, '/support/code-generator', '/support/code-generator/code-generator-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:25:56');
INSERT INTO `sys_menu` VALUES (152, '更新日志', 2, 218, 3, '/support/change-log/change-log-list', '/support/change-log/change-log-list.vue', NULL, NULL, NULL, 'HeartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 44, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (153, '清除缓存', 3, 133, 2, NULL, NULL, 1, 'support:cache:delete', 'support:cache:delete', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (154, '获取缓存key', 3, 133, 3, NULL, NULL, 1, 'support:cache:list', 'support:cache:list', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (156, '查看结果', 3, 117, NULL, NULL, NULL, 1, 'support:reload:result', 'support:reload:result', NULL, 117, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (157, '单号生成', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:generate', 'support:serialNumber:generate', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (158, '生成记录', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:record', 'support:serialNumber:record', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (159, '新建', 3, 110, NULL, NULL, NULL, 1, 'support:dict:add', 'support:dict:add', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (160, '编辑', 3, 110, NULL, NULL, NULL, 1, 'support:dict:edit', 'support:dict:edit', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (161, '批量删除', 3, 110, NULL, NULL, NULL, 1, 'support:dict:delete', 'support:dict:delete', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (162, '刷新缓存', 3, 110, NULL, NULL, NULL, 1, 'support:dict:refresh', 'support:dict:refresh', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (163, '新建', 3, 109, NULL, NULL, NULL, 1, 'support:config:add', 'support:config:add', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (164, '编辑', 3, 109, NULL, NULL, NULL, 1, 'support:config:update', 'support:config:update', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (165, '查询', 3, 47, NULL, NULL, NULL, 1, 'goods:query', 'goods:query', NULL, 47, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:24:08');
INSERT INTO `sys_menu` VALUES (166, '新建', 3, 47, NULL, NULL, NULL, 1, 'goods:add', 'goods:add', NULL, 47, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:24:06');
INSERT INTO `sys_menu` VALUES (167, '批量删除', 3, 47, NULL, NULL, NULL, 1, 'goods:batchDelete', 'goods:batchDelete', NULL, 47, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:24:03');
INSERT INTO `sys_menu` VALUES (168, '查询', 3, 147, 11, NULL, NULL, 1, 'support:helpDoc:query', 'support:helpDoc:query', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (169, '新建', 3, 147, 12, NULL, NULL, 1, 'support:helpDoc:add', 'support:helpDoc:add', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (170, '新建目录', 3, 147, 1, NULL, NULL, 1, 'support:helpDocCatalog:addCategory', 'support:helpDocCatalog:addCategory', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (171, '修改目录', 3, 147, 2, NULL, NULL, 1, 'support:helpDocCatalog:update', 'support:helpDocCatalog:update', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (173, '新建', 3, 78, NULL, NULL, NULL, 1, 'category:add', 'category:add', NULL, 78, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:46');
INSERT INTO `sys_menu` VALUES (174, '查询', 3, 78, NULL, NULL, NULL, 1, 'category:tree', 'category:tree', NULL, 78, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:43');
INSERT INTO `sys_menu` VALUES (175, '编辑', 3, 78, NULL, NULL, NULL, 1, 'category:update', 'category:update', NULL, 78, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:41');
INSERT INTO `sys_menu` VALUES (176, '删除', 3, 78, NULL, NULL, NULL, 1, 'category:delete', 'category:delete', NULL, 78, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:38');
INSERT INTO `sys_menu` VALUES (177, '新建', 3, 79, NULL, NULL, NULL, 1, 'category:add', 'custom:category:add', NULL, 78, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:31');
INSERT INTO `sys_menu` VALUES (178, '查询', 3, 79, NULL, NULL, NULL, 1, 'category:tree', 'custom:category:tree', NULL, 78, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:29');
INSERT INTO `sys_menu` VALUES (179, '编辑', 3, 79, NULL, NULL, NULL, 1, 'category:update', 'custom:category:update', NULL, 78, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:26');
INSERT INTO `sys_menu` VALUES (180, '删除', 3, 79, NULL, NULL, NULL, 1, 'category:delete', 'custom:category:delete', NULL, 78, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:24');
INSERT INTO `sys_menu` VALUES (181, '查询', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:query', 'oa:enterprise:query', NULL, 144, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:09');
INSERT INTO `sys_menu` VALUES (182, '新建', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:add', 'oa:enterprise:add', NULL, 144, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:06');
INSERT INTO `sys_menu` VALUES (183, '编辑', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:update', 'oa:enterprise:update', NULL, 144, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:04');
INSERT INTO `sys_menu` VALUES (184, '删除', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:delete', 'oa:enterprise:delete', NULL, 144, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:02');
INSERT INTO `sys_menu` VALUES (185, '查询', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:query', 'oa:notice:query', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (186, '新建', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:add', 'oa:notice:add', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (187, '编辑', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:update', 'oa:notice:update', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (188, '删除', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:delete', 'oa:notice:delete', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (190, '查询', 3, 152, NULL, NULL, NULL, 1, '', 'support:changeLog:query', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (191, '新建', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:add', 'support:changeLog:add', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (192, '批量删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:batchDelete', 'support:changeLog:batchDelete', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (193, '文件管理', 2, 50, 20, '/support/file/file-list', '/support/file/file-list.vue', NULL, NULL, NULL, 'FolderOpenOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (194, '删除', 3, 47, NULL, NULL, NULL, 1, 'goods:delete', 'goods:delete', NULL, 47, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:24:01');
INSERT INTO `sys_menu` VALUES (195, '修改', 3, 47, NULL, NULL, NULL, 1, 'goods:update', 'goods:update', NULL, NULL, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:59');
INSERT INTO `sys_menu` VALUES (196, '查看详情', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:detail', 'oa:enterprise:detail', NULL, NULL, 0, NULL, 0, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:22:53');
INSERT INTO `sys_menu` VALUES (198, '删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:delete', 'support:changeLog:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (199, '查询', 3, 109, NULL, NULL, NULL, 1, 'support:config:query', 'support:config:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (200, '查询', 3, 193, NULL, NULL, NULL, 1, 'support:file:query', 'support:file:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (201, '删除', 3, 147, 14, NULL, NULL, 1, 'support:helpDoc:delete', 'support:helpDoc:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (202, '更新', 3, 147, 13, NULL, NULL, 1, 'support:helpDoc:update', 'support:helpDoc:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (203, '查询', 3, 143, NULL, NULL, NULL, 1, 'support:loginLog:query', 'support:loginLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (204, '查询', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:query', 'support:operateLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (205, '详情', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:detail', 'support:operateLog:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (206, '心跳监控', 2, 111, 1, '/support/heart-beat/heart-beat-list', '/support/heart-beat/heart-beat-list.vue', 1, NULL, NULL, 'FallOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (207, '更新', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:update', 'support:changeLog:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (212, '查询', 3, 117, NULL, NULL, NULL, 1, 'support:reload:query', 'support:reload:query', NULL, NULL, 0, NULL, 1, 1, 1, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (213, '网络安全', 1, 0, 5, NULL, NULL, 1, NULL, NULL, 'SafetyCertificateOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (214, '登录锁定', 2, 213, 1, '/support/login-fail', '/support/login-fail/login-fail-list.vue', 1, NULL, NULL, 'LockOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (216, '导出', 3, 47, NULL, NULL, NULL, 1, 'goods:exportGoods', 'goods:exportGoods', NULL, NULL, 0, NULL, 1, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:56');
INSERT INTO `sys_menu` VALUES (217, '导入', 3, 47, 3, NULL, NULL, 1, 'goods:importGoods', 'goods:importGoods', NULL, NULL, 0, NULL, 1, 1, 0, 1, 1, '2024-06-13 22:44:45', 1, '2024-07-28 09:23:54');
INSERT INTO `sys_menu` VALUES (218, '文档中心', 1, 0, 4, NULL, NULL, 1, NULL, NULL, 'FileSearchOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (221, '会话信息', 2, 111, 1, '/support/session/session-list', '/support/session/session-list.vue', 1, 'system:monitor:session_read', 'system:monitor:session_read', 'DashboardOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (222, '查询', 3, 221, NULL, NULL, NULL, 1, 'system:monitor:session_read', 'system:monitor:session_read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (223, '强退', 3, 221, NULL, NULL, NULL, 1, 'system:monitor:session_kickout', 'system:monitor:session_kickout', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (225, '表单设计器', 1, 0, 1, NULL, NULL, 1, NULL, NULL, 'ApiOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', 1, '2024-06-24 23:07:06');
INSERT INTO `sys_menu` VALUES (226, '设计表单', 2, 225, 2, '/support/designer/index', '/support/designer/index.vue', 1, NULL, NULL, 'AndroidOutlined', NULL, 0, NULL, 0, 0, 0, 0, 1, '2024-06-13 22:44:45', 1, '2024-06-21 21:43:14');
INSERT INTO `sys_menu` VALUES (229, '密码策略', 2, 213, NULL, '/support/password-policy/index', '/support/password-policy/index.vue', 1, NULL, NULL, 'ContactsOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (230, '读操作', 3, 229, 1, NULL, NULL, 1, 'support:password_policy:read', 'support:password_policy:read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (231, '写操作', 3, 229, 2, NULL, NULL, 1, 'support:password_policy:write', 'support:password_policy:write', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (232, '服务监控', 2, 111, 4, '/support/server/index', '/support/server/index.vue', 1, NULL, NULL, 'CloudServerOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (233, '读操作', 3, 232, 1, NULL, NULL, 1, 'system:monitor_server:read', 'system:monitor_server:read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (235, '查看缓存值', 3, 133, 1, NULL, NULL, 1, 'support:cache:value', 'support:cache:value', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (236, '邮箱配置', 2, 50, 1, '/support/email-config/index', '/support/email-config/index.vue', 1, NULL, NULL, 'MailOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (237, '读操作', 3, 236, 1, NULL, NULL, 1, 'support:email_config:read', 'support:email_config:read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (238, '写操作', 3, 236, 2, NULL, NULL, 1, 'support:email_config:write', 'support:email_config:write', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-13 22:44:45', NULL, NULL);
INSERT INTO `sys_menu` VALUES (239, '表单设计列表', 2, 225, 1, '/support/form/form-list', '/support/form/form-list.vue', 1, NULL, NULL, 'FormOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-14 21:52:36', 1, '2024-06-15 17:27:50');
INSERT INTO `sys_menu` VALUES (240, '表单数据', 2, 225, NULL, '1', NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, 1, 1, 0, 1, 1, '2024-06-14 21:53:25', 1, '2024-06-21 21:43:36');
INSERT INTO `sys_menu` VALUES (241, '应用监控', 2, 111, 6, '/supprot/spring-boota-dmin/index', NULL, 1, NULL, NULL, 'AlertOutlined', NULL, 1, 'http://mcdull.io:9000', 1, 1, 0, 0, 1, '2024-06-15 11:47:47', NULL, NULL);
INSERT INTO `sys_menu` VALUES (242, '行政区域', 2, 50, 7, '/support/area/index', '/support/area/index.vue', 1, NULL, NULL, 'BankOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-15 13:43:30', NULL, NULL);
INSERT INTO `sys_menu` VALUES (243, '数据列表', 2, 225, 3, '/support/form/form-data-list', '/support/form/form-data-list.vue', 1, NULL, NULL, 'DiffOutlined', NULL, 0, NULL, 0, 0, 0, 0, 1, '2024-06-15 19:18:04', 1, '2024-06-15 19:19:49');
INSERT INTO `sys_menu` VALUES (244, '查询', 3, 239, NULL, NULL, NULL, 1, 'support:form:page', 'support:form:page', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:48:43', NULL, NULL);
INSERT INTO `sys_menu` VALUES (245, '新增', 3, 239, NULL, NULL, NULL, 1, 'support:form:add', 'support:form:add', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:49:13', NULL, NULL);
INSERT INTO `sys_menu` VALUES (246, '更新', 3, 239, NULL, NULL, NULL, 1, 'support:form:update', 'support:form:update', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:49:27', NULL, NULL);
INSERT INTO `sys_menu` VALUES (247, '删除', 3, 239, NULL, NULL, NULL, 1, 'support:form:delete', 'support:form:delete', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:49:51', NULL, NULL);
INSERT INTO `sys_menu` VALUES (248, '保存设计', 3, 226, NULL, NULL, NULL, 1, 'support:form:designer', 'support:form:designer', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:50:50', 1, '2024-06-21 22:04:57');
INSERT INTO `sys_menu` VALUES (249, '发布', 3, 239, NULL, NULL, NULL, 1, 'support:form:publish', 'support:form:publish', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:51:18', NULL, NULL);
INSERT INTO `sys_menu` VALUES (250, '新增', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:add', 'support:form:record:add', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:52:34', NULL, NULL);
INSERT INTO `sys_menu` VALUES (251, '查询', 3, 243, 2, NULL, NULL, 1, 'support:form:record:list', 'support:form:record:list', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:52:50', 1, '2024-06-21 21:55:02');
INSERT INTO `sys_menu` VALUES (252, '导出', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:export', 'support:form:record:export', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:53:04', NULL, NULL);
INSERT INTO `sys_menu` VALUES (253, '删除', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:delete', 'support:form:record:delete', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:53:33', NULL, NULL);
INSERT INTO `sys_menu` VALUES (254, '更新', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:update', 'support:form:record:update', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-21 21:53:53', NULL, NULL);
INSERT INTO `sys_menu` VALUES (255, '壹把火', 1, 0, 0, NULL, NULL, 1, NULL, NULL, 'DeploymentUnitOutlined', NULL, 0, NULL, 1, 1, 0, 1, 1, '2024-06-24 23:03:54', 1, '2024-07-28 09:22:47');
INSERT INTO `sys_menu` VALUES (256, '企业端', 1, 255, NULL, NULL, NULL, 1, NULL, NULL, 'AppstoreAddOutlined', NULL, 0, NULL, 1, 1, 0, 1, 1, '2024-06-24 23:10:15', 1, '2024-07-28 09:22:45');
INSERT INTO `sys_menu` VALUES (257, '企业管理', 2, 256, 1, '/blaze/customer/customer-info-list', '/blaze/customer/customer-info-list.vue', 1, NULL, NULL, 'AppstoreOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2024-06-24 23:12:50', 1, '2024-07-28 09:22:42');

SET FOREIGN_KEY_CHECKS = 1;
