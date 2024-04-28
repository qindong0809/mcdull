drop table if exists `sys_user`;
create table if not exists `sys_user` (
`id` bigint(20) not null auto_increment comment '主键',
`login_name` varchar(30)  not null comment '登录帐号',
`login_pwd` varchar(50)  not null comment '登录密码',
`actual_name` varchar(30)  not null comment '员工名称',
`gender` tinyint(1) not null default 0 comment '性别',
`phone` varchar(15)  null default null comment '手机号码',
`department_id` int(0) not null comment '部门id',
`administrator_flag` tinyint(0) not null default 0 comment '是否为超级管理员: 0 不是，1是',
`remark` varchar(200)  null default null comment '备注',
`created_by` bigint(20) not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` bigint(20) default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)  comment='用户';

insert into `sys_user` values(1, 'admin', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'Terry', 0, '13800000000', 1, 1, '超级管理员', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_user` values(2, 'user', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'mcdull', 0, '13800000000', 1, 0, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_user` values(3, 'user1', '7c4a8d09ca3762af61e59520943dc26494f8941b', 'mcdull1', 0, '13800000000', 1, 0, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);

drop table if exists `sys_role`;
create table `sys_role`  (
`id` bigint(0) not null auto_increment comment '主键',
`role_name` varchar(20)  not null comment '角色名称',
`role_code` varchar(500)  null default null comment '角色编码',
`remark` varchar(200)  null default null comment '角色描述',
`created_by` bigint(20) not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` bigint(20) default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '角色表';

insert into `sys_role` values(1, '超管', 'super_admin', '只读角色', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_role` values(2, '管理员', 'admin', '管理员', 0, sysdate(), 0, sysdate(), 0, 0);

drop table if exists `sys_department`;
create table `sys_department`  (
`id` bigint(0) not null auto_increment comment '部门主键id',
`name` varchar(50) not null comment '部门名称',
`manager_id` bigint(0) null default null comment '部门负责人id',
`parent_id` bigint(0) not null default 0 comment '部门的父级id',
`sort` int(0) not null comment '部门排序',
`created_by` bigint(20) not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` bigint(20) default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree,
index `parent_id`(`parent_id`) using btree
) comment = '部门';

insert into `sys_department` values(1, 'R&D', 1, 0, 1, 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_department` values(2, '测试部', 2, 1, 2, 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_department` values(3, '财务部', 3, 1, 3, 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_department` values(4, '销售部', 4, 1, 4, 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_department` values(5, '研发部', 5, 1, 5, 0, sysdate(), 0, sysdate(), 0, 0);

drop table if exists `sys_menu`;
create table `sys_menu`  (
`id` bigint(0) not null auto_increment comment '菜单id',
`menu_name` varchar(200)  not null comment '菜单名称',
`menu_type` int(0) not null comment '类型 1/目录 2/菜单 3/功能',
`parent_id` bigint(0) not null comment '父菜单id',
`sort` int(0) null default null comment '显示顺序',
`path` varchar(255)  null default null comment '路由地址',
`component` varchar(255)  null default null comment '组件路径',
`perms_type` int(0) null default null comment '权限类型',
`api_perms` text  null comment '后端权限字符串',
`web_perms` text  null comment '前端权限字符串',
`icon` varchar(100)  null default null comment '菜单图标',
`context_menu_id` bigint(0) null default null comment '功能点关联菜单id',
`frame_flag` tinyint(1) not null default 0 comment '是否为外链',
`frame_url` text  null comment '外链地址',
`cache_flag` tinyint(1) not null default 0 comment '是否缓存',
`visible_flag` tinyint(1) not null default 1 comment '显示状态',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_by` bigint(20) not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` bigint(20) default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)comment = '菜单表';

INSERT INTO `sys_menu` VALUES (26, '菜单管理', 2, 50, 1, '/menu/list', '/system/menu/menu-list.vue', NULL, NULL, NULL, 'CopyOutlined', NULL, 0, NULL, 1, 1, 0, 0, 2, '2021-08-09 15:04:35', 1, '2023-12-01 19:39:03');
INSERT INTO `sys_menu` VALUES (40, '删除', 3, 26, NULL, NULL, NULL, 1, 'system:menu:batchDelete', 'system:menu:batchDelete', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 09:45:56', 1, '2023-10-07 18:15:50');
INSERT INTO `sys_menu` VALUES (45, '部门员工', 1, 0, 3, '/organization', NULL, NULL, NULL, NULL, 'UserSwitchOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 16:13:27', 1, '2023-11-28 17:42:25');
INSERT INTO `sys_menu` VALUES (46, '部门员工', 2, 45, 1, '/employee/department', '/system/employee/department/index.vue', NULL, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 16:21:50', 1, '2022-06-23 16:19:54');
INSERT INTO `sys_menu` VALUES (47, '商品管理', 2, 48, 1, '/erp/goods/list', '/business/erp/goods/goods-list.vue', NULL, NULL, NULL, 'AliwangwangOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2021-08-12 17:58:39', 1, '2023-12-01 19:33:08');
INSERT INTO `sys_menu` VALUES (48, '商品管理', 1, 137, 10, '/goods', NULL, NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 18:02:59', 1, '2022-06-24 20:07:35');
INSERT INTO `sys_menu` VALUES (50, '系统设置', 1, 0, 6, '/setting', NULL, NULL, NULL, NULL, 'SettingOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-13 16:41:33', 1, '2023-12-01 19:38:03');
INSERT INTO `sys_menu` VALUES (76, '角色管理', 2, 45, 2, '/employee/role', '/system/employee/role/index.vue', NULL, NULL, NULL, 'SlidersOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-26 10:31:00', 1, '2022-06-23 16:21:06');
INSERT INTO `sys_menu` VALUES (78, '商品分类', 2, 48, 2, '/erp/catalog/goods', '/business/erp/catalog/goods-catalog.vue', NULL, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2022-05-18 23:34:14', 1, '2023-12-01 19:33:13');
INSERT INTO `sys_menu` VALUES (79, '自定义分组', 2, 48, 3, '/erp/catalog/custom', '/business/erp/catalog/custom-catalog.vue', NULL, NULL, NULL, 'AppstoreAddOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-18 23:37:53', 1, '2023-12-01 19:33:16');
INSERT INTO `sys_menu` VALUES (81, '请求监控', 2, 111, 3, '/support/operate-log/operate-log-list', '/support/operate-log/operate-log-list.vue', NULL, NULL, NULL, 'VideoCameraOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-20 12:37:24', 1, '2022-10-22 18:33:10');
INSERT INTO `sys_menu` VALUES (85, '组件演示', 2, 84, NULL, '/demonstration/index', '/support/demonstration/index.vue', NULL, NULL, NULL, 'ClearOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-20 23:16:46', NULL, '2022-05-20 23:16:46');
INSERT INTO `sys_menu` VALUES (86, '添加部门', 3, 46, 1, NULL, NULL, 1, 'system:department:add', 'system:department:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-26 23:33:37', 1, '2023-10-07 18:26:35');
INSERT INTO `sys_menu` VALUES (87, '修改部门', 3, 46, 2, NULL, NULL, 1, 'system:department:update', 'system:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-26 23:34:11', 1, '2023-10-07 18:26:44');
INSERT INTO `sys_menu` VALUES (88, '删除部门', 3, 46, 3, NULL, NULL, 1, 'system:department:delete', 'system:department:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-26 23:34:49', 1, '2023-10-07 18:26:49');
INSERT INTO `sys_menu` VALUES (91, '添加员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:add', 'system:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:11:38', 1, '2023-10-07 18:27:46');
INSERT INTO `sys_menu` VALUES (92, '编辑员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:update', 'system:employee:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:12:10', 1, '2023-10-07 18:27:49');
INSERT INTO `sys_menu` VALUES (93, '禁用启用员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:disabled', 'system:employee:disabled', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:12:37', 1, '2023-10-07 18:27:53');
INSERT INTO `sys_menu` VALUES (94, '调整员工部门', 3, 46, NULL, NULL, NULL, 1, 'system:employee:department:update', 'system:employee:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:12:59', 1, '2023-10-07 18:27:34');
INSERT INTO `sys_menu` VALUES (95, '重置密码', 3, 46, NULL, NULL, NULL, 1, 'system:employee:password:reset', 'system:employee:password:reset', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:13:30', 1, '2023-10-07 18:27:57');
INSERT INTO `sys_menu` VALUES (96, '删除员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:delete', 'system:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:14:08', 1, '2023-10-07 18:28:01');
INSERT INTO `sys_menu` VALUES (97, '添加角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:add', 'system:role:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:34:00', 1, '2023-10-07 18:42:31');
INSERT INTO `sys_menu` VALUES (98, '删除角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:delete', 'system:role:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:34:19', 1, '2023-10-07 18:42:35');
INSERT INTO `sys_menu` VALUES (99, '编辑角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:update', 'system:role:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:34:55', 1, '2023-10-07 18:42:44');
INSERT INTO `sys_menu` VALUES (100, '更新数据范围', 3, 76, NULL, NULL, NULL, 1, 'system:role:dataScope:update', 'system:role:dataScope:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:37:03', 1, '2023-10-07 18:41:49');
INSERT INTO `sys_menu` VALUES (101, '批量移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:batch:delete', 'system:role:employee:batch:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:39:05', 1, '2023-10-07 18:43:32');
INSERT INTO `sys_menu` VALUES (102, '移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:delete', 'system:role:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:39:21', 1, '2023-10-07 18:43:37');
INSERT INTO `sys_menu` VALUES (103, '添加员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:add', 'system:role:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:39:38', 1, '2023-10-07 18:44:05');
INSERT INTO `sys_menu` VALUES (104, '修改权限', 3, 76, NULL, NULL, NULL, 1, 'system:role:menu:update', 'system:role:menu:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:41:55', 1, '2023-10-07 18:44:11');
INSERT INTO `sys_menu` VALUES (105, '添加', 3, 26, NULL, NULL, NULL, 1, 'system:menu:add', 'system:menu:add', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:44:37', 1, '2023-10-07 17:35:35');
INSERT INTO `sys_menu` VALUES (106, '编辑', 3, 26, NULL, NULL, NULL, 1, 'system:menu:update', 'system:menu:update', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:44:59', 1, '2023-10-07 17:35:48');
INSERT INTO `sys_menu` VALUES (109, '参数配置', 2, 50, 3, '/config/config-list', '/support/config/config-list.vue', NULL, NULL, NULL, 'AntDesignOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 13:34:41', 1, '2022-06-23 16:24:16');
INSERT INTO `sys_menu` VALUES (110, '数据字典', 2, 50, 4, '/setting/dict', '/support/dict/index.vue', NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 17:53:00', 1, '2022-05-27 18:09:14');
INSERT INTO `sys_menu` VALUES (111, '监控服务', 1, 0, 100, '/monitor', NULL, NULL, NULL, NULL, 'BarChartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-17 11:13:23', 1, '2023-11-28 17:43:56');
INSERT INTO `sys_menu` VALUES (113, '查询', 3, 112, NULL, NULL, NULL, NULL, NULL, 'ad', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-17 11:31:36', NULL, '2022-06-17 11:31:36');
INSERT INTO `sys_menu` VALUES (114, '运维工具', 1, 0, 200, NULL, NULL, NULL, NULL, NULL, 'NodeCollapseOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2022-06-20 10:09:16', 1, '2023-12-01 19:36:18');
INSERT INTO `sys_menu` VALUES (117, 'Reload', 2, 50, 12, '/hook', '/support/reload/reload-list.vue', NULL, NULL, NULL, 'ReloadOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-20 10:16:49', 1, '2023-12-01 19:39:17');
INSERT INTO `sys_menu` VALUES (122, '数据库监控', 2, 111, 4, '/support/druid/index', NULL, NULL, NULL, NULL, 'ConsoleSqlOutlined', NULL, 1, 'http://mcdull.io:8090/druid', 1, 1, 0, 0, 1, '2022-06-20 14:49:33', 1, '2023-02-16 19:15:58');
INSERT INTO `sys_menu` VALUES (130, '单号管理', 2, 50, 6, '/support/serial-number/serial-number-list', '/support/serial-number/serial-number-list.vue', NULL, NULL, NULL, 'NumberOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-24 14:45:22', 1, '2022-06-28 16:23:41');
INSERT INTO `sys_menu` VALUES (132, '通知公告', 2, 138, 2, '/oa/notice/notice-list', '/business/oa/notice/notice-list.vue', NULL, NULL, NULL, 'SoundOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2022-06-24 18:23:09', 1, '2023-12-04 13:42:23');
INSERT INTO `sys_menu` VALUES (133, '缓存管理', 2, 50, 11, '/support/cache/cache-list', '/support/cache/cache-list.vue', NULL, NULL, NULL, 'BorderInnerOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-24 18:52:25', 1, '2023-12-01 19:39:13');
INSERT INTO `sys_menu` VALUES (137, '进销存系统', 1, 0, 2, NULL, NULL, NULL, NULL, NULL, 'AccountBookOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-24 20:07:20', 1, '2022-10-22 18:30:19');
INSERT INTO `sys_menu` VALUES (138, 'OA系统', 1, 0, 1, NULL, NULL, NULL, NULL, NULL, 'BankOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-24 20:09:18', 1, '2022-10-22 18:30:15');
INSERT INTO `sys_menu` VALUES (142, '公告详情', 2, 132, NULL, '/oa/notice/notice-detail', '/business/oa/notice/notice-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-06-25 16:38:47', 1, '2022-09-14 19:46:17');
INSERT INTO `sys_menu` VALUES (143, '登录日志', 2, 213, 2, '/support/login-log/login-log-list', '/support/login-log/login-log-list.vue', NULL, NULL, NULL, 'LoginOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-28 15:01:38', 1, '2023-10-17 19:04:34');
INSERT INTO `sys_menu` VALUES (144, '企业信息', 2, 138, 1, '/oa/enterprise/enterprise-list', '/business/oa/enterprise/enterprise-list.vue', NULL, NULL, NULL, 'ShopOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-14 17:00:07', 1, '2023-12-04 13:42:19');
INSERT INTO `sys_menu` VALUES (145, '企业详情', 2, 138, NULL, '/oa/enterprise/enterprise-detail', '/business/oa/enterprise/enterprise-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-09-14 18:52:52', 1, '2022-11-22 10:39:07');
INSERT INTO `sys_menu` VALUES (147, '帮助文档', 2, 218, 1, '/help-doc/help-doc-manage-list', '/support/help-doc/management/help-doc-manage-list.vue', NULL, NULL, NULL, 'FolderViewOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-14 19:59:01', 1, '2023-12-01 19:38:23');
INSERT INTO `sys_menu` VALUES (148, '意见反馈', 2, 218, 2, '/feedback/feedback-list', '/support/feedback/feedback-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-14 19:59:52', 1, '2023-12-01 19:38:40');
INSERT INTO `sys_menu` VALUES (149, '我的通知', 2, 132, NULL, '/oa/notice/notice-employee-list', '/business/oa/notice/notice-employee-list.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-09-14 20:29:41', 1, '2022-09-14 20:31:23');
INSERT INTO `sys_menu` VALUES (150, '我的通知公告详情', 2, 132, NULL, '/oa/notice/notice-employee-detail', '/business/oa/notice/notice-employee-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-09-14 20:30:25', 1, '2022-09-14 20:31:38');
INSERT INTO `sys_menu` VALUES (151, '代码生成', 2, 0, 600, '/support/code-generator', '/support/code-generator/code-generator-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-21 18:25:05', 1, '2022-10-22 11:27:58');
INSERT INTO `sys_menu` VALUES (152, '更新日志', 2, 218, 3, '/support/change-log/change-log-list', '/support/change-log/change-log-list.vue', NULL, NULL, NULL, 'HeartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 44, '2022-10-10 10:31:20', 1, '2023-12-01 19:38:51');
INSERT INTO `sys_menu` VALUES (153, '清除缓存', 3, 133, NULL, NULL, NULL, 1, 'support:cache:delete', 'support:cache:delete', NULL, 133, 0, NULL, 0, 1, 1, 0, 1, '2022-10-15 22:45:13', 1, '2023-10-07 16:22:29');
INSERT INTO `sys_menu` VALUES (154, '获取缓存key', 3, 133, NULL, NULL, NULL, 1, 'support:cache:keys', 'support:cache:keys', NULL, 133, 0, NULL, 0, 1, 1, 0, 1, '2022-10-15 22:45:48', 1, '2023-10-07 16:22:35');
INSERT INTO `sys_menu` VALUES (156, '查看结果', 3, 117, NULL, NULL, NULL, 1, 'support:reload:result', 'support:reload:result', NULL, 117, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:17:23', 1, '2023-10-07 14:31:47');
INSERT INTO `sys_menu` VALUES (157, '单号生成', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:generate', 'support:serialNumber:generate', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:21:06', 1, '2023-10-07 18:22:46');
INSERT INTO `sys_menu` VALUES (158, '生成记录', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:record', 'support:serialNumber:record', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:21:34', 1, '2023-10-07 18:22:55');
INSERT INTO `sys_menu` VALUES (159, '新建', 3, 110, NULL, NULL, NULL, 1, 'support:dict:add', 'support:dict:add', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:23:51', 1, '2023-10-07 18:18:24');
INSERT INTO `sys_menu` VALUES (160, '编辑', 3, 110, NULL, NULL, NULL, 1, 'support:dict:edit', 'support:dict:edit', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:24:05', 1, '2023-10-07 18:19:17');
INSERT INTO `sys_menu` VALUES (161, '批量删除', 3, 110, NULL, NULL, NULL, 1, 'support:dict:delete', 'support:dict:delete', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:24:34', 1, '2023-10-07 18:19:39');
INSERT INTO `sys_menu` VALUES (162, '刷新缓存', 3, 110, NULL, NULL, NULL, 1, 'support:dict:refresh', 'support:dict:refresh', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:24:55', 1, '2023-10-07 18:18:37');
INSERT INTO `sys_menu` VALUES (163, '新建', 3, 109, NULL, NULL, NULL, 1, 'support:config:add', 'support:config:add', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:26:56', 1, '2023-10-07 18:16:17');
INSERT INTO `sys_menu` VALUES (164, '编辑', 3, 109, NULL, NULL, NULL, 1, 'support:config:update', 'support:config:update', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:27:07', 1, '2023-10-07 18:16:24');
INSERT INTO `sys_menu` VALUES (165, '查询', 3, 47, NULL, NULL, NULL, 1, 'goods:query', 'goods:query', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 19:55:39', 1, '2023-10-07 13:58:28');
INSERT INTO `sys_menu` VALUES (166, '新建', 3, 47, NULL, NULL, NULL, 1, 'goods:add', 'goods:add', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 19:56:00', 1, '2023-10-07 13:58:32');
INSERT INTO `sys_menu` VALUES (167, '批量删除', 3, 47, NULL, NULL, NULL, 1, 'goods:batchDelete', 'goods:batchDelete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 19:56:15', 1, '2023-10-07 13:58:35');
INSERT INTO `sys_menu` VALUES (168, '查询', 3, 147, 11, NULL, NULL, 1, 'support:helpDoc:query', 'support:helpDoc:query', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:12:13', 1, '2023-10-07 14:05:49');
INSERT INTO `sys_menu` VALUES (169, '新建', 3, 147, 12, NULL, NULL, 1, 'support:helpDoc:add', 'support:helpDoc:add', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:12:37', 1, '2023-10-07 14:05:56');
INSERT INTO `sys_menu` VALUES (170, '新建目录', 3, 147, 1, NULL, NULL, 1, 'support:helpDocCatalog:addCategory', 'support:helpDocCatalog:addCategory', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:12:57', 1, '2023-10-07 14:06:38');
INSERT INTO `sys_menu` VALUES (171, '修改目录', 3, 147, 2, NULL, NULL, 1, 'support:helpDocCatalog:update', 'support:helpDocCatalog:update', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:13:46', 1, '2023-10-07 14:06:49');
INSERT INTO `sys_menu` VALUES (173, '新建', 3, 78, NULL, NULL, NULL, 1, 'category:add', 'category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:02', 1, '2023-10-07 13:54:01');
INSERT INTO `sys_menu` VALUES (174, '查询', 3, 78, NULL, NULL, NULL, 1, 'category:tree', 'category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:22', 1, '2023-10-07 13:54:33');
INSERT INTO `sys_menu` VALUES (175, '编辑', 3, 78, NULL, NULL, NULL, 1, 'category:update', 'category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:38', 1, '2023-10-07 13:54:18');
INSERT INTO `sys_menu` VALUES (176, '删除', 3, 78, NULL, NULL, NULL, 1, 'category:delete', 'category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:50', 1, '2023-10-07 13:54:27');
INSERT INTO `sys_menu` VALUES (177, '新建', 3, 79, NULL, NULL, NULL, 1, 'category:add', 'custom:category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:02', 1, '2023-10-07 13:57:32');
INSERT INTO `sys_menu` VALUES (178, '查询', 3, 79, NULL, NULL, NULL, 1, 'category:tree', 'custom:category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:22', 1, '2023-10-07 13:57:50');
INSERT INTO `sys_menu` VALUES (179, '编辑', 3, 79, NULL, NULL, NULL, 1, 'category:update', 'custom:category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:38', 1, '2023-10-07 13:58:02');
INSERT INTO `sys_menu` VALUES (180, '删除', 3, 79, NULL, NULL, NULL, 1, 'category:delete', 'custom:category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:50', 1, '2023-10-07 13:58:12');
INSERT INTO `sys_menu` VALUES (181, '查询', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:query', 'oa:enterprise:query', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:14', 1, '2023-10-07 12:00:09');
INSERT INTO `sys_menu` VALUES (182, '新建', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:add', 'oa:enterprise:add', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:25', 1, '2023-10-07 12:00:17');
INSERT INTO `sys_menu` VALUES (183, '编辑', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:update', 'oa:enterprise:update', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:36', 1, '2023-10-07 12:00:38');
INSERT INTO `sys_menu` VALUES (184, '删除', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:delete', 'oa:enterprise:delete', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:53', 1, '2023-10-07 12:00:46');
INSERT INTO `sys_menu` VALUES (185, '查询', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:query', 'oa:notice:query', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:26:38', 1, '2023-10-07 11:43:01');
INSERT INTO `sys_menu` VALUES (186, '新建', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:add', 'oa:notice:add', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:27:04', 1, '2023-10-07 11:43:07');
INSERT INTO `sys_menu` VALUES (187, '编辑', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:update', 'oa:notice:update', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:27:15', 1, '2023-10-07 11:43:12');
INSERT INTO `sys_menu` VALUES (188, '删除', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:delete', 'oa:notice:delete', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:27:23', 1, '2023-10-07 11:43:18');
INSERT INTO `sys_menu` VALUES (190, '查询', 3, 152, NULL, NULL, NULL, 1, '', 'support:changeLog:query', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:28:33', 1, '2023-10-07 14:25:05');
INSERT INTO `sys_menu` VALUES (191, '新建', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:add', 'support:changeLog:add', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:28:46', 1, '2023-10-07 14:24:15');
INSERT INTO `sys_menu` VALUES (192, '批量删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:batchDelete', 'support:changeLog:batchDelete', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:29:10', 1, '2023-10-07 14:24:22');
INSERT INTO `sys_menu` VALUES (193, '文件管理', 2, 50, 20, '/support/file/file-list', '/support/file/file-list.vue', NULL, NULL, NULL, 'FolderOpenOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 11:26:11', 1, '2022-10-22 11:29:22');
INSERT INTO `sys_menu` VALUES (194, '删除', 3, 47, NULL, NULL, NULL, 1, 'goods:delete', 'goods:delete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:00:12', 1, '2023-10-07 13:58:39');
INSERT INTO `sys_menu` VALUES (195, '修改', 3, 47, NULL, NULL, NULL, 1, 'goods:update', 'goods:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:05:23', 1, '2023-10-07 13:58:42');
INSERT INTO `sys_menu` VALUES (196, '查看详情', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:detail', 'oa:enterprise:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:16:47', 1, '2023-10-07 11:48:59');
INSERT INTO `sys_menu` VALUES (198, '删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:delete', 'support:changeLog:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:42:34', 1, '2023-10-07 14:24:32');
INSERT INTO `sys_menu` VALUES (199, '查询', 3, 109, NULL, NULL, NULL, 1, 'support:config:query', 'support:config:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:45:14', 1, '2023-10-07 18:16:27');
INSERT INTO `sys_menu` VALUES (200, '查询', 3, 193, NULL, NULL, NULL, 1, 'support:file:query', 'support:file:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:47:23', 1, '2023-10-07 18:24:43');
INSERT INTO `sys_menu` VALUES (201, '删除', 3, 147, 14, NULL, NULL, 1, 'support:helpDoc:delete', 'support:helpDoc:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 21:03:20', 1, '2023-10-07 14:07:02');
INSERT INTO `sys_menu` VALUES (202, '更新', 3, 147, 13, NULL, NULL, 1, 'support:helpDoc:update', 'support:helpDoc:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 21:03:32', 1, '2023-10-07 14:06:56');
INSERT INTO `sys_menu` VALUES (203, '查询', 3, 143, NULL, NULL, NULL, 1, 'support:loginLog:query', 'support:loginLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 21:05:11', 1, '2023-10-07 14:27:23');
INSERT INTO `sys_menu` VALUES (204, '查询', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:query', 'support:operateLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 10:33:31', 1, '2023-10-07 14:27:56');
INSERT INTO `sys_menu` VALUES (205, '详情', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:detail', 'support:operateLog:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 10:33:49', 1, '2023-10-07 14:28:04');
INSERT INTO `sys_menu` VALUES (206, '心跳监控', 2, 111, 1, '/support/heart-beat/heart-beat-list', '/support/heart-beat/heart-beat-list.vue', 1, NULL, NULL, 'FallOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 10:47:03', 1, '2022-10-22 18:32:52');
INSERT INTO `sys_menu` VALUES (207, '更新', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:update', 'support:changeLog:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 11:51:32', 1, '2023-10-07 14:24:39');
INSERT INTO `sys_menu` VALUES (212, '查询', 3, 117, NULL, NULL, NULL, 1, 'support:reload:query', 'support:reload:query', NULL, NULL, 0, NULL, 1, 1, 1, 0, 1, '2023-10-07 14:31:36', NULL, '2023-10-07 14:31:36');
INSERT INTO `sys_menu` VALUES (213, '网络安全', 1, 0, 5, NULL, NULL, 1, NULL, NULL, 'SafetyCertificateOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-10-17 19:03:08', 1, '2023-12-01 19:38:00');
INSERT INTO `sys_menu` VALUES (214, '登录锁定', 2, 213, 1, '/support/login-fail', '/support/login-fail/login-fail-list.vue', 1, NULL, NULL, 'LockOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-10-17 19:04:24', NULL, '2023-10-17 19:04:24');
INSERT INTO `sys_menu` VALUES (215, '接口加解密', 2, 213, 3, '/support/api-encrypt', '/support/api-encrypt/api-encrypt-index.vue', 1, NULL, NULL, 'CodepenCircleOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-10-24 11:49:28', 1, '2023-10-25 16:28:18');
INSERT INTO `sys_menu` VALUES (216, '导出', 3, 47, NULL, NULL, NULL, 1, 'goods:exportGoods', 'goods:exportGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-12-01 19:34:03', NULL, '2023-12-01 19:34:03');
INSERT INTO `sys_menu` VALUES (217, '导入', 3, 47, 3, NULL, NULL, 1, 'goods:importGoods', 'goods:importGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-12-01 19:34:22', NULL, '2023-12-01 19:34:22');
INSERT INTO `sys_menu` VALUES (218, '文档中心', 1, 0, 4, NULL, NULL, 1, NULL, NULL, 'FileSearchOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-12-01 19:37:28', 1, '2023-12-01 19:37:51');


drop table if exists `sys_role_menu`;
create table `sys_role_menu`  (
`id` bigint(0) not null auto_increment comment '主键id',
`role_id` bigint(0) not null comment '角色id',
`menu_id` bigint(0) not null comment '菜单id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
)comment = '角色-菜单';

drop table if exists `sys_role_user`;
create table `sys_role_user`  (
`id` bigint(0) not null auto_increment,
`role_id` bigint(0) not null comment '角色id',
`user_id` bigint(0) not null comment '员工id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree,
unique index `uk_role_employee`(`role_id`, `user_id`) using btree
) comment = '角色用户';

drop table if exists `sys_dict_key`;
create table `sys_dict_key`  (
`id` bigint(0) not null auto_increment,
`key_code` varchar(50) not null comment '编码',
`key_name` varchar(50) not null comment '名称',
`remark` varchar(500)  null default null comment '备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '字段key';
INSERT INTO `sys_dict_key` VALUES (1, 'GODOS_PLACE', '商品产地', '商品产地的字典', 0, sysdate(), sysdate());

drop table if exists `sys_dict_value`;
create table `sys_dict_value`  (
`id` bigint(0) not null auto_increment,
`dict_key_id` bigint(0) not null,
`value_code` varchar(50)  not null comment '编码',
`value_name` varchar(50)  not null comment '名称',
`remark` varchar(500)  null default null comment '备注',
`sort` int(0) not null default 0 comment '排序',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)comment = '字典的值';


insert into `sys_dict_value` values (1, 1, 'luo_yang', '洛阳', '', 1, 0, sysdate(), sysdate());
insert into `sys_dict_value` values (2, 1, 'zheng_zhou', '郑州', '', 2, 0, sysdate(), sysdate());
insert into `sys_dict_value` values (3, 1, 'bei_jing', '北京', '', 3, 0, sysdate(), sysdate());

drop table if exists `sys_config`;
create table `sys_config`  (
`id` bigint(0) not null auto_increment comment '主键',
`config_name` varchar(255)  not null comment '参数名字',
`config_key` varchar(255)  not null comment '参数key',
`config_value` text  not null,
`remark` varchar(255)  null default null,
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '系统配置';

insert into `sys_config` values (1, '万能密码', 'super_password', '1024lab', '建议定期修改', 0, sysdate(), sysdate());