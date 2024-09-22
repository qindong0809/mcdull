create table if not exists `sys_user` (
`id` int not null auto_increment comment '主键',
`login_name` varchar(30)  not null comment '登录帐号',
`login_pwd` varchar(50)  not null comment '登录密码',
`actual_name` varchar(30)  not null comment '员工名称',
`email` varchar(300)  not null comment 'email',
`gender` tinyint(1) not null default 0 comment '性别',
`phone` varchar(15)  null default null comment '手机号码',
`department_id` int(0) not null comment '部门id',
`administrator_flag` tinyint(0) not null default 0 comment '是否为超级管理员: 0 不是，1是',
`last_login_time` datetime default null comment '最近一次登录时间',
`last_password_modified_date` datetime default null comment '最近一次修改密码时间',
`used_password` varchar(5000) null default null comment '密码历史记录',
`remark` varchar(200)  null default null comment '备注',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)  comment='用户';

insert into `sys_user` values(1, 'admin', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', 'Terry',  '1@sina.com', 0, '13800000000', 1, 1, null, null, null, '超级管理员', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_user` values(2, 'dev',  'a29c57c6894dee6e8251510d58c07078ee3f49bf', '王大锤',    '1@sina.com',  0, '13800000000', 1, 0, null, null, null, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_user` values(3, 'demo', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', '演示账号（只读）',   '1@sina.com',   0, '13800000000', 1, 0, null, null, null, '只读用户', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_user` values(4, 'zhaoming', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', '赵敏',   '1@sina.com',   0, '13800000000', 1, 0, null, null, null, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_user` values(5, 'qiangang', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', '钱刚',   '1@sina.com',   0, '13800000000', 1, 0, null, null, null, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_user` values(6, 'sunli', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', '孙丽',    '1@sina.com',  0, '13800000000', 1, 0, null, null, null, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);

create table `sys_role`  (
`id` int not null auto_increment comment '主键',
`role_name` varchar(20)  not null comment '角色名称',
`role_code` varchar(500)  null default null comment '角色编码',
`remark` varchar(200)  null default null comment '角色描述',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '角色表';

insert into `sys_role` values(1, '系统管理员', 'sys_admin',    '管理员',   0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_role` values(3, '运维管理员', 'devops_admin', '管理员',   0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_role` values(4, 'HR',        'hr',          '人事',     0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_role` values(5, '访客',      'visitor',      '只读',     0, sysdate(), 0, sysdate(), 0, 0);

create table `sys_department`  (
`id` int not null auto_increment comment '部门主键id',
`name` varchar(50) not null comment '部门名称',
`manager_id` int null default null comment '部门负责人id',
`parent_id` int not null default 0 comment '部门的父级id',
`sort` int(0) not null comment '部门排序',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree,
index `parent_id`(`parent_id`) using btree
) comment = '部门';

insert into `sys_department` values(1, 'xx集团', 1, 0, 1, 0, sysdate(), 0, sysdate(), 0, 0);

insert into `sys_department` values(2, '研发部', 1, 1, 1, 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_department` values(3, '总经办',    1, 1, 2, 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_department` values(4, '财务部',    1, 1, 3, 0, sysdate(), 0, sysdate(), 0, 0);

insert into `sys_department` values(5, '物资部',    2, 3, 2, 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_department` values(6, '营销中心',    3, 3, 3, 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_department` values(7, '行政部',    4, 3, 4, 0, sysdate(), 0, sysdate(), 0, 0);

create table `sys_menu`  (
`id` int not null auto_increment comment '菜单id',
`menu_name` varchar(200)  not null comment '菜单名称',
`menu_type` int(0) not null comment '类型 1/目录 2/菜单 3/功能',
`parent_id` int not null comment '父菜单id',
`sort` int(0) null default null comment '显示顺序',
`path` varchar(255)  null default null comment '路由地址',
`component` varchar(255)  null default null comment '组件路径',
`perms_type` int(0) null default null comment '权限类型',
`api_perms` text  null comment '后端权限字符串',
`web_perms` text  null comment '前端权限字符串',
`icon` varchar(100)  null default null comment '菜单图标',
`context_menu_id` int null default null comment '功能点关联菜单id',
`frame_flag` tinyint(1) not null default 0 comment '是否为外链',
`frame_url` text  null comment '外链地址',
`cache_flag` tinyint(1) not null default 0 comment '是否缓存',
`visible_flag` tinyint(1) not null default 1 comment '显示状态',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)comment = '菜单表';

INSERT INTO `sys_menu` VALUES (26, '菜单管理', 2, 50, 1, '/menu/list', '/system/menu/menu-list.vue', NULL, NULL, NULL, 'CopyOutlined', NULL, 0, NULL, 1, 1, 0, 0, 2, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (40, '删除', 3, 26, NULL, NULL, NULL, 1, 'system:menu:batchDelete', 'system:menu:batchDelete', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (45, '组织架构', 1, 0, 3, '/organization', NULL, NULL, NULL, NULL, 'UserSwitchOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (258, '部门管理', 2, 45, 1, '/organization/department', '/system/department/department-list.vue', 1, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (46, '部门员工', 2, 45, 1, '/employee/department', '/system/employee/department/index.vue', NULL, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (47, '商品管理', 2, 48, 1, '/erp/goods/list', '/business/erp/goods/goods-list.vue', NULL, NULL, NULL, 'AliwangwangOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (48, '商品管理', 1, 137, 10, '/goods', NULL, NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (50, '系统设置', 1, 0, 6, '/setting', NULL, NULL, NULL, NULL, 'SettingOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (76, '角色管理', 2, 45, 2, '/employee/role', '/system/employee/role/index.vue', NULL, NULL, NULL, 'SlidersOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (78, '商品分类', 2, 48, 2, '/erp/catalog/goods', '/business/erp/catalog/goods-catalog.vue', NULL, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (79, '自定义分组', 2, 48, 3, '/erp/catalog/custom', '/business/erp/catalog/custom-catalog.vue', NULL, NULL, NULL, 'AppstoreAddOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (81, '请求监控', 2, 111, 3, '/support/operate-log/operate-log-list', '/support/operate-log/operate-log-list.vue', NULL, NULL, NULL, 'VideoCameraOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (86, '添加部门', 3, 46, 1, NULL, NULL, 1, 'system:department:add', 'system:department:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (87, '修改部门', 3, 46, 2, NULL, NULL, 1, 'system:department:update', 'system:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (88, '删除部门', 3, 46, 3, NULL, NULL, 1, 'system:department:delete', 'system:department:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (91, '添加员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:add', 'system:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (92, '编辑员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:update', 'system:employee:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (93, '禁用启用员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:disabled', 'system:employee:disabled', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (94, '调整员工部门', 3, 46, NULL, NULL, NULL, 1, 'system:employee:department:update', 'system:employee:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (95, '重置密码', 3, 46, NULL, NULL, NULL, 1, 'system:employee:password:reset', 'system:employee:password:reset', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (96, '删除员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:delete', 'system:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (97, '添加角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:add', 'system:role:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (98, '删除角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:delete', 'system:role:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (99, '编辑角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:update', 'system:role:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (100, '更新数据范围', 3, 76, NULL, NULL, NULL, 1, 'system:role:dataScope:update', 'system:role:dataScope:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (101, '批量移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:batch:delete', 'system:role:employee:batch:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (102, '移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:delete', 'system:role:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (103, '添加员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:add', 'system:role:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (104, '修改权限', 3, 76, NULL, NULL, NULL, 1, 'system:role:menu:update', 'system:role:menu:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (105, '添加', 3, 26, NULL, NULL, NULL, 1, 'system:menu:add', 'system:menu:add', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (106, '编辑', 3, 26, NULL, NULL, NULL, 1, 'system:menu:update', 'system:menu:update', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (109, '参数配置', 2, 50, 3, '/config/config-list', '/support/config/config-list.vue', NULL, NULL, NULL, 'AntDesignOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (110, '数据字典', 2, 50, 4, '/setting/dict', '/support/dict/index.vue', NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (111, '监控服务', 1, 0, 100, '/monitor', NULL, NULL, NULL, NULL, 'BarChartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (114, '运维工具', 1, 0, 200, NULL, NULL, NULL, NULL, NULL, 'NodeCollapseOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (117, 'Reload', 2, 50, 12, '/hook', '/support/reload/reload-list.vue', NULL, NULL, NULL, 'ReloadOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (122, '数据库监控', 2, 111, 4, '/support/druid/index', NULL, NULL, NULL, NULL, 'ConsoleSqlOutlined', NULL, 1, 'http://mcdull.io:8090/druid', 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (130, '单号管理', 2, 50, 6, '/support/serial-number/serial-number-list', '/support/serial-number/serial-number-list.vue', NULL, NULL, NULL, 'NumberOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (132, '通知公告', 2, 138, 2, '/oa/notice/notice-list', '/business/oa/notice/notice-list.vue', NULL, NULL, NULL, 'SoundOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (133, '缓存管理', 2, 50, 11, '/support/cache/cache-list', '/support/cache/cache-list.vue', NULL, NULL, NULL, 'BorderInnerOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (137, '进销存系统', 1, 0, 2, NULL, NULL, NULL, NULL, NULL, 'AccountBookOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (138, 'OA系统', 1, 0, 1, NULL, NULL, NULL, NULL, NULL, 'BankOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (142, '公告详情', 2, 132, NULL, '/oa/notice/notice-detail', '/business/oa/notice/notice-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (143, '登录日志', 2, 213, 2, '/support/login-log/login-log-list', '/support/login-log/login-log-list.vue', NULL, NULL, NULL, 'LoginOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (144, '企业信息', 2, 138, 1, '/oa/enterprise/enterprise-list', '/business/oa/enterprise/enterprise-list.vue', NULL, NULL, NULL, 'ShopOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (145, '企业详情', 2, 138, NULL, '/oa/enterprise/enterprise-detail', '/business/oa/enterprise/enterprise-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (147, '帮助文档', 2, 218, 1, '/help-doc/help-doc-manage-list', '/support/help-doc/management/help-doc-manage-list.vue', NULL, NULL, NULL, 'FolderViewOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (148, '意见反馈', 2, 218, 2, '/feedback/feedback-list', '/support/feedback/feedback-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (149, '我的通知', 2, 132, NULL, '/oa/notice/notice-employee-list', '/business/oa/notice/notice-employee-list.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (150, '我的通知公告详情', 2, 132, NULL, '/oa/notice/notice-employee-detail', '/business/oa/notice/notice-employee-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (151, '代码生成', 2, 0, 600, '/support/code-generator', '/support/code-generator/code-generator-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (152, '更新日志', 2, 218, 3, '/support/change-log/change-log-list', '/support/change-log/change-log-list.vue', NULL, NULL, NULL, 'HeartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 44, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (156, '查看结果', 3, 117, NULL, NULL, NULL, 1, 'support:reload:result', 'support:reload:result', NULL, 117, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (157, '单号生成', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:generate', 'support:serialNumber:generate', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (158, '生成记录', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:record', 'support:serialNumber:record', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (159, '新建', 3, 110, NULL, NULL, NULL, 1, 'support:dict:add', 'support:dict:add', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (160, '编辑', 3, 110, NULL, NULL, NULL, 1, 'support:dict:edit', 'support:dict:edit', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (161, '批量删除', 3, 110, NULL, NULL, NULL, 1, 'support:dict:delete', 'support:dict:delete', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (162, '刷新缓存', 3, 110, NULL, NULL, NULL, 1, 'support:dict:refresh', 'support:dict:refresh', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (163, '新建', 3, 109, NULL, NULL, NULL, 1, 'support:config:add', 'support:config:add', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (164, '编辑', 3, 109, NULL, NULL, NULL, 1, 'support:config:update', 'support:config:update', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (165, '查询', 3, 47, NULL, NULL, NULL, 1, 'goods:query', 'goods:query', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (166, '新建', 3, 47, NULL, NULL, NULL, 1, 'goods:add', 'goods:add', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (167, '批量删除', 3, 47, NULL, NULL, NULL, 1, 'goods:batchDelete', 'goods:batchDelete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (168, '查询', 3, 147, 11, NULL, NULL, 1, 'support:helpDoc:query', 'support:helpDoc:query', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (169, '新建', 3, 147, 12, NULL, NULL, 1, 'support:helpDoc:add', 'support:helpDoc:add', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (170, '新建目录', 3, 147, 1, NULL, NULL, 1, 'support:helpDocCatalog:addCategory', 'support:helpDocCatalog:addCategory', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (171, '修改目录', 3, 147, 2, NULL, NULL, 1, 'support:helpDocCatalog:update', 'support:helpDocCatalog:update', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (173, '新建', 3, 78, NULL, NULL, NULL, 1, 'category:add', 'category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (174, '查询', 3, 78, NULL, NULL, NULL, 1, 'category:tree', 'category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (175, '编辑', 3, 78, NULL, NULL, NULL, 1, 'category:update', 'category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (176, '删除', 3, 78, NULL, NULL, NULL, 1, 'category:delete', 'category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (177, '新建', 3, 79, NULL, NULL, NULL, 1, 'category:add', 'custom:category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (178, '查询', 3, 79, NULL, NULL, NULL, 1, 'category:tree', 'custom:category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (179, '编辑', 3, 79, NULL, NULL, NULL, 1, 'category:update', 'custom:category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (180, '删除', 3, 79, NULL, NULL, NULL, 1, 'category:delete', 'custom:category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (181, '查询', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:query', 'oa:enterprise:query', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (182, '新建', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:add', 'oa:enterprise:add', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (183, '编辑', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:update', 'oa:enterprise:update', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (184, '删除', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:delete', 'oa:enterprise:delete', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (185, '查询', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:query', 'oa:notice:query', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (186, '新建', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:add', 'oa:notice:add', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (187, '编辑', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:update', 'oa:notice:update', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (188, '删除', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:delete', 'oa:notice:delete', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (190, '查询', 3, 152, NULL, NULL, NULL, 1, '', 'support:changeLog:query', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (191, '新建', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:add', 'support:changeLog:add', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (192, '批量删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:batchDelete', 'support:changeLog:batchDelete', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (193, '文件管理', 2, 50, 20, '/support/file/file-list', '/support/file/file-list.vue', NULL, NULL, NULL, 'FolderOpenOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (194, '删除', 3, 47, NULL, NULL, NULL, 1, 'goods:delete', 'goods:delete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (195, '修改', 3, 47, NULL, NULL, NULL, 1, 'goods:update', 'goods:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (196, '查看详情', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:detail', 'oa:enterprise:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (198, '删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:delete', 'support:changeLog:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (199, '查询', 3, 109, NULL, NULL, NULL, 1, 'support:config:query', 'support:config:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (200, '查询', 3, 193, NULL, NULL, NULL, 1, 'support:file:query', 'support:file:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (201, '删除', 3, 147, 14, NULL, NULL, 1, 'support:helpDoc:delete', 'support:helpDoc:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (202, '更新', 3, 147, 13, NULL, NULL, 1, 'support:helpDoc:update', 'support:helpDoc:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (203, '查询', 3, 143, NULL, NULL, NULL, 1, 'support:loginLog:query', 'support:loginLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (204, '查询', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:query', 'support:operateLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (205, '详情', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:detail', 'support:operateLog:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (206, '心跳监控', 2, 111, 1, '/support/heart-beat/heart-beat-list', '/support/heart-beat/heart-beat-list.vue', 1, NULL, NULL, 'FallOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (207, '更新', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:update', 'support:changeLog:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (212, '查询', 3, 117, NULL, NULL, NULL, 1, 'support:reload:query', 'support:reload:query', NULL, NULL, 0, NULL, 1, 1, 1, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (213, '网络安全', 1, 0, 5, NULL, NULL, 1, NULL, NULL, 'SafetyCertificateOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (214, '登录锁定', 2, 213, 1, '/support/login-fail', '/support/login-fail/login-fail-list.vue', 1, NULL, NULL, 'LockOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (216, '导出', 3, 47, NULL, NULL, NULL, 1, 'goods:exportGoods', 'goods:exportGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (217, '导入', 3, 47, 3, NULL, NULL, 1, 'goods:importGoods', 'goods:importGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (218, '文档中心', 1, 0, 4, NULL, NULL, 1, NULL, NULL, 'FileSearchOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (221, '会话信息', 2, 111, 1, '/support/session/session-list', '/support/session/session-list.vue', 1, 'system:monitor:session_read', 'system:monitor:session_read', 'DashboardOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (223, '强退', 3, 221, NULL, NULL, NULL, 1, 'system:monitor:session_kickout', 'system:monitor:session_kickout', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (222, '查询', 3, 221, NULL, NULL, NULL, 1, 'system:monitor:session_read', 'system:monitor:session_read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);

INSERT INTO `sys_menu` VALUES (225, '表单设计器', 1, 0, 0, NULL, NULL, 1, NULL, NULL, 'ApiOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (226, '设计表单', 2, 225, NULL, '/support/designer/index', '/support/designer/index.vue', 1, NULL, NULL, 'AndroidOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (229, '密码策略', 2, 213, NULL, '/support/password-policy/index', '/support/password-policy/index.vue', 1, NULL, NULL, 'ContactsOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (230, '读操作', 3, 229, 1, NULL, NULL, 1, 'support:password_policy:read', 'support:password_policy:read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (231, '写操作', 3, 229, 2, NULL, NULL, 1, 'support:password_policy:write', 'support:password_policy:write', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (232, '服务监控', 2, 111, 4, '/support/server/index', '/support/server/index.vue', 1, NULL, NULL, 'CloudServerOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (233, '读操作', 3, 232, 1, NULL, NULL, 1, 'system:monitor_server:read', 'system:monitor_server:read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (235, '查看缓存值', 3, 133, 1, NULL, NULL, 1, 'support:cache:value', 'support:cache:value', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (153, '清除缓存', 3, 133, 2, NULL, NULL, 1, 'support:cache:delete', 'support:cache:delete', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (154, '获取缓存key', 3, 133, 3, NULL, NULL, 1, 'support:cache:list', 'support:cache:list', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (236, '邮箱配置', 2, 50, 1, '/support/email-config/index', '/support/email-config/index.vue', 1, NULL, NULL, 'MailOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (237, '读操作', 3, 236, 1, NULL, NULL, 1, 'support:email_config:read', 'support:email_config:read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (238, '写操作', 3, 236, 2, NULL, NULL, 1, 'support:email_config:write', 'support:email_config:write', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (239, '表单设计列表', 2, 225, 1, '/support/form/form-list', '/support/form/form-list.vue', 1, NULL, NULL, 'FormOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (240, '表单数据', 2, 225, NULL, '1', NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, 1, 1, 0, 1, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (241, '应用监控', 2, 111, 6, '/supprot/spring-boota-dmin/index', NULL, 1, NULL, NULL, 'AlertOutlined', NULL, 1, 'http://mcdull.io:9000', 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (242, '行政区域', 2, 50, 7, '/support/area/index', '/support/area/index.vue', 1, NULL, NULL, 'BankOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (243, '数据列表', 2, 225, 3, '/support/form/form-data-list', '/support/form/form-data-list.vue', 1, NULL, NULL, 'DiffOutlined', NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (244, '查询', 3, 239, NULL, NULL, NULL, 1, 'support:form:page', 'support:form:page', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (245, '新增', 3, 239, NULL, NULL, NULL, 1, 'support:form:add', 'support:form:add', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (246, '更新', 3, 239, NULL, NULL, NULL, 1, 'support:form:update', 'support:form:update', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (247, '删除', 3, 239, NULL, NULL, NULL, 1, 'support:form:delete', 'support:form:delete', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (248, '保存设计', 3, 226, NULL, NULL, NULL, 1, 'support:form:designer', 'support:form:designer', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (249, '发布', 3, 239, NULL, NULL, NULL, 1, 'support:form:publish', 'support:form:publish', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (250, '新增', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:add', 'support:form:record:add', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (251, '查询', 3, 243, 2, NULL, NULL, 1, 'support:form:record:list', 'support:form:record:list', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (252, '导出', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:export', 'support:form:record:export', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (253, '删除', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:delete', 'support:form:record:delete', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (254, '更新', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:update', 'support:form:record:update', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (259, 'knife4j文档', 2, 218, 4, '/knife4j', NULL, 1, NULL, NULL, 'FileWordOutlined', NULL, 1, 'http://localhost:8090/doc.html', 1, 1, 0, 0, 1, sysdate(), NULL, NULL);


create table `sys_role_menu`  (
`id` int not null auto_increment comment '主键id',
`role_id` int not null comment '角色id',
`menu_id` int not null comment '菜单id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
)comment = '角色-菜单';

create table `sys_role_user`  (
`id` int not null auto_increment,
`role_id` int not null comment '角色id',
`user_id` int not null comment '员工id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '角色用户';

create table `sys_role_data_scope`  (
`id` int not null auto_increment,
`data_scope_type` int(0) not null comment '数据范围id',
`view_type` int(0) not null comment '数据范围类型',
`role_id` int not null comment '角色id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '角色的数据范围';

create table `sys_dict_key`  (
`id` int not null auto_increment,
`key_code` varchar(50) not null comment '编码',
`key_name` varchar(50) not null comment '名称',
`remark` varchar(500)  null default null comment '备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '字段key';
INSERT INTO `sys_dict_key` VALUES (1, 'GODOS_PLACE', '商品产地', '商品产地的字典', 0, sysdate(), sysdate());

create table `sys_dict_value`  (
`id` int not null auto_increment,
`dict_key_id` int not null,
`value_code` varchar(50)  not null comment '编码',
`value_name` varchar(50)  not null comment '名称',
`remark` varchar(500)  null default null comment '备注',
`sort` int(0) not null default 0 comment '排序',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)comment = '字典的值';


insert into `sys_dict_value` values (1, 1, 'luo_yang',  '洛阳', '', 1, 0, sysdate(), sysdate());
insert into `sys_dict_value` values (2, 1, 'zheng_zhou', '郑州', '', 2, 0, sysdate(), sysdate());
insert into `sys_dict_value` values (3, 1, 'bei_jing', '北京', '', 3, 0, sysdate(), sysdate());

create table `sys_config`  (
`id` int not null auto_increment comment '主键',
`config_name` varchar(255)  not null comment '参数名字',
`config_key` varchar(255)  not null comment '参数key',
`config_value` text  not null comment '参数值',
`remark` varchar(255)  null default null comment '备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '系统配置';

insert into `sys_config` values (1, '系统名称', 'system-name', 'xxx系统', '', 0, sysdate(), sysdate());
insert into `sys_config` values (2, '域名名称', 'domain-name', 'http://mcdull.io:8081', '', 0, sysdate(), sysdate());
insert into `sys_config` values (3, '重置密码邮件标题', 'forget-password-email-title', '密码重置请求', '', 0, sysdate(), sysdate());
insert into `sys_config` values (4, '重置密码链接有效期（分钟）', 'forget-password-timeout', '5', '', 0, sysdate(), sysdate());

create table `sys_file`  (
`id` int not null auto_increment comment '主键id',
`folder_type` tinyint unsigned not null comment '文件夹类型',
`file_name` varchar(100) null default null comment '文件名称',
`file_size` int(0) null default null comment '文件大小',
`file_key` varchar(200)  not null comment '文件key，用于文件下载',
`file_type` varchar(50)  not null comment '文件类型',
`created_by` int not null comment '创建人',
`updated_by` int default null comment '更新人',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree,
unique index `uk_file_key`(`file_key`) using btree,
index `module_id_module_type`(`folder_type`) using btree,
index `module_type`(`folder_type`) using btree
) comment = '文件';

create table `sys_table_column`  (
`id` int not null auto_increment,
`user_id` int not null comment '用户id',
`table_id` int(0) not null comment '表格id',
`columns` text null comment '具体的表格列，存入的json',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree,
unique index `uni_employee_table`(`user_id`, `table_id`) using btree
)  comment = '表格的自定义列存储';

create table `sys_help_doc_catalog`  (
`id` int not null auto_increment comment '帮助文档目录',
`name` varchar(1000) not null comment '名称',
`sort` int(0) not null default 0 comment '排序字段',
`parent_id` int not null comment '父级id',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '帮助文档-目录';
insert into `sys_help_doc_catalog` values (6, '企业信息', 0, 0, 0, sysdate(), sysdate());
insert into `sys_help_doc_catalog` values (9, '企业信用', 0, 6, 0, sysdate(), sysdate());
insert into `sys_help_doc_catalog` values (10, '采购文档', 0, 11, 0, sysdate(), sysdate());
insert into `sys_help_doc_catalog` values (11, '进销存', 0, 0, 0, sysdate(), sysdate());

create table `sys_help_doc`  (
`id` int not null auto_increment,
`help_doc_catalog_id` int not null comment '类型1公告 2动态',
`title` varchar(200)  not null comment '标题',
`content_text` text  not null comment '文本内容',
`content_html` text  not null comment 'html内容',
`attachment` varchar(1000)  null default null comment '附件',
`sort` int(0) not null default 0 comment '排序',
`page_view_count` int(0) not null default 0 comment '页面浏览量pv',
`user_view_count` int(0) not null default 0 comment '用户浏览量uv',
`author` varchar(1000)  null default null comment '作者',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '帮助文档';

insert into `sys_help_doc` values (32, 6, '企业名称该写什么？', '、行、备注等，数据变动记录；', '<ul><li style=\"text-align: start;\">需求1：管理公司基本信息，包含：企业名称、logo、地区、营业执照、联系人 等等，可以 增删拆改</li><li style=\"text-align: start;\">需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改</li><li style=\"text-align: start;\">需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改</li><li style=\"text-align: start;\">需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；</li></ul>', '', 0, 49, 1, '卓大', 0, sysdate(), sysdate());
insert into `sys_help_doc` values (33, 6, '谁有权限查看企业信息', '纳税号、银行账 任何的修改，都有记录 数据变动记录；', '<ul><li style=\"text-align: start;\">需求1：管理公司基本信息，包含：企业名称、logo、地区、营业执照、联系人 等等，可以 增删拆改</li><li style=\"text-align: start;\">需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改</li><li style=\"text-align: start;\">需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改</li><li style=\"text-align: start;\">需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；</li></ul>', '', 0, 12, 1, '卓大', 0, sysdate(), sysdate());

create table `sys_help_doc_relation`  (
`id` int not null auto_increment,
`relation_id` int not null comment '关联id',
`relation_name` varchar(255) null default null comment '关联名称',
`help_doc_id` int not null comment '文档id',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`, `relation_id`, `help_doc_id`) using btree,
unique index `uni_menu_help_doc`(`relation_id`, `help_doc_id`) using btree
) comment = '帮助文档-关联表';

insert into `sys_help_doc_relation` values (1, 0, '首页', 32,  0, sysdate(), sysdate());
insert into `sys_help_doc_relation` values (2, 0, '首页', 33,  0, sysdate(), sysdate());

create table `sys_help_doc_view_record`  (
`id` int not null auto_increment,
`help_doc_id` int not null comment 'help doc id',
`user_id` int not null comment '用户id',
`page_view_count` int(0) null default 0 comment '查看次数',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`, `help_doc_id`, `user_id`) using btree,
unique index `uk_notice_employee`(`help_doc_id`, `user_id`) using btree comment '资讯员工'
) comment = '帮助文档-查看记录';

insert into `sys_help_doc_view_record` values (1, 31, 1,  3,  0, sysdate(), sysdate());
insert into `sys_help_doc_view_record` values (2, 32, 1,  49, 0, sysdate(), sysdate());
insert into `sys_help_doc_view_record` values (3, 33, 1,  12, 0, sysdate(), sysdate());
insert into `sys_help_doc_view_record` values (4, 34, 1,  5,  0, sysdate(), sysdate());

create table `sys_change_log`  (
`id` int not null auto_increment comment '更新日志id',
`version` varchar(255) not null comment '版本',
`type` int(0) not null comment '更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]',
`publish_author` varchar(255) not null comment '发布人',
`public_date` date not null comment '发布日期',
`content` text not null comment '更新内容',
`link` text null comment '跳转链接',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree,
unique index `version_unique`(`version`) using btree
) comment = '系统更新日志';

insert into `sys_change_log` values (1, 'v1.0.0', 1, '王大锤', sysdate(), 'v1.0.0 版本正式上线，内容如下：\n\n1.【新增】增加员工姓名查询\n\n2.【新增】增加文件预览组件\n\n3.【新增】新增四级菜单\n', '', 0, sysdate(), sysdate());
insert into `sys_change_log` values (2, 'v1.0.1', 2, '王大锤', sysdate(), 'v1.0.1 版本正式更新上线，更新内容如下：\n\n1.【新增】增加员工姓名查询\n\n2.【新增】增加文件预览组件\n\n3.【新增】新增四级菜单\n', '', 0, sysdate(), sysdate());
insert into `sys_change_log` values (3, 'v1.0.2', 3, '王大锤', sysdate(), 'v1.0.2 版本bug修复，更新内容如下：\n\n1.【新增】增加员工姓名查询\n\n2.【新增】增加文件预览组件\n\n3.【新增】新增四级菜单\n', '', 0, sysdate(), sysdate());

create table `sys_feedback`  (
`id` int not null auto_increment comment '主键',
`code` varchar(512)  not null comment '编号',
`feedback_content` text  null comment '反馈内容',
`feedback_attachment` varchar(500)  null default null comment '反馈图片',
`user_id` int not null comment '创建人id',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '意见反馈';

create table `sys_code_generator_config`  (
`id` int not null auto_increment comment '主键',
`table_name` varchar(255) not null comment '表名',
`basic` text null comment '基础命名信息',
`fields` text null comment '字段列表',
`insert_and_update` text null comment '新建、修改',
`delete_info` text null comment '删除',
`query_fields` text null comment '查询',
`table_fields` text null comment '列表',
`detail` text null comment '详情',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`, `table_name`) using btree,
unique index `table_unique`(`table_name`) using btree
) comment = '代码生成器的每个表的配置' ;

create table `sys_login_log`  (
`id` int(0) not null auto_increment comment '主键',
`login_name` varchar(512) not null comment '用户id',
`login_ip` varchar(1000)  null default null comment '用户ip',
`login_ip_region` varchar(1000)  null default null comment '用户ip地区',
`user_agent` text  null comment 'user-agent信息',
`login_result` int(0) not null comment '登录结果：0成功 1失败 2 退出',
`remark` varchar(2000)  null default null comment '备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree,
index `customer_id`(`login_name`) using btree
) comment = '用户登录日志';

create table `sys_operate_log`  (
`id` int(0) not null auto_increment comment '主键',
`user_id` int not null comment '用户id',
`trace_id` varchar(50) null default null comment 'Trace ID',
`time_taken` int(0) null default null comment '耗时(ms)',
`module` varchar(50) null default null comment '操作模块',
`content` varchar(500) null default null comment '操作内容',
`url` varchar(100) null default null comment '请求路径',
`method` varchar(100) null default null comment '请求方法',
`param` varchar(4096) null comment '请求参数',
`ip` varchar(255) null default null comment '请求ip',
`ip_region` varchar(1000) null default null comment '请求ip地区',
`user_agent` text null comment '请求user-agent',
`success_flag` tinyint(0) null default null comment '请求结果 0失败 1成功',
`fail_reason` longtext null comment '失败原因',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '操作记录';

create table `sys_notice`  (
`id` int(0) not null auto_increment comment '主键',
`notice_type_id` int(0) not null comment '类型1公告 2动态',
`title` varchar(200) not null comment '标题',
`all_visible_flag` tinyint(1) not null comment '是否全部可见',
`scheduled_publish_flag` tinyint(1) not null comment '是否定时发布',
`publish_time` datetime(0) not null comment '发布时间',
`content_text` text not null comment '文本内容',
`content_html` text not null comment 'html内容',
`attachment` varchar(1000) null default null comment '附件',
`page_view_count` int(0) not null default 0 comment '页面浏览量pv',
`user_view_count` int(0) not null default 0 comment '用户浏览量uv',
`source` varchar(1000) null default null comment '来源',
`author` varchar(1000) null default null comment '作者',
`document_number` varchar(1000) null default null comment '文号',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '通知';

create table `sys_notice_type`  (
`id` int(0) not null auto_increment comment '通知类型',
`notice_type_name` varchar(1000) not null comment '类型名称',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '通知类型';

create table `sys_notice_visible_range`  (
`id` int(0) not null auto_increment comment '主键',
`notice_id` int not null comment '通知id',
`data_type` tinyint(0) not null comment '数据类型1员工 2部门',
`data_id` int not null comment '员工or部门id',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '通知可见范围';

insert into `sys_notice_type` values (1, '公告', 0, sysdate(), sysdate());
insert into `sys_notice_type` values (2, '通知', 0, sysdate(), sysdate());

create table `sys_notice_view_record`  (
`id` int not null auto_increment comment '主键',
`notice_id` int not null comment '通知公告id',
`user_id` int not null comment '员工id',
`page_view_count` int(0) null default 0 comment '查看次数',
`first_ip` varchar(255)  null default null comment '首次ip',
`first_user_agent` varchar(1000) null default null comment '首次用户设备等标识',
`last_ip` varchar(255) null default null comment '最后一次ip',
`last_user_agent` varchar(1000) null default null comment '最后一次用户设备等标识',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '通知查看记录';

create table `sys_oa_enterprise`  (
`id` int not null auto_increment comment '企业id',
`enterprise_name` varchar(255) not null comment '企业名称',
`enterprise_logo` varchar(255) null default null comment '企业logo',
`type` int(0) not null default 1 comment '类型（1:有限公司;2:合伙公司）',
`unified_social_credit_code` varchar(255) not null comment '统一社会信用代码',
`contact` varchar(100) not null comment '联系人',
`contact_phone` varchar(100) not null comment '联系人电话',
`email` varchar(100) null default null comment '邮箱',
`province` varchar(100) null default null comment '省份',
`province_name` varchar(100) null default null comment '省份名称',
`city` varchar(100) null default null comment '市',
`city_name` varchar(100) null default null comment '城市名称',
`district` varchar(100) null default null comment '区县',
`district_name` varchar(100) null default null comment '区县名称',
`address` varchar(255) null default null comment '详细地址',
`business_license` varchar(255) null default null comment '营业执照',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = 'oa企业模块' ;

insert into `sys_oa_enterprise` values (1, '区块链实验室', '', 1, 'block', 'kk', '18637922222', null, '410000', '河南省', '410300', '洛阳市', '410311', '洛龙区', '区块链大楼', null, 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_oa_enterprise` values (2, '创新实验室', '', 2, '创新', 'xx', '18637921111', 'xxx@163.com', '410000', '河南省', '410300', '洛阳市', '410311', '洛龙区', '1024大楼', null, 0, sysdate(), 0, sysdate(), 0, 0);

create table `sys_serial_number`  (
`id` int not null auto_increment comment '主键',
`business_type` int not null comment '业务类型1/意见反馈 2/',
`format` varchar(50) null default null comment '格式[yyyy]表示年,[mm]标识月,[dd]表示日,[nnn]表示三位数字',
`step_random_range` int not null comment '步长随机数',
`remark` varchar(255) null default null comment '备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)comment = '单号生成器定义表';

insert into `sys_serial_number` values (1, 1, 'FDBK.{yyyy}{mm}{dd}{nnnnn}', 1, 'FDBK.20201101321',  0, sysdate(), sysdate());

create table `sys_serial_number_record`  (
`id` int not null auto_increment comment '主键',
`serial_number_id` int(0) not null,
`record_date` datetime not null comment '记录日期',
`last_number` int not null default 0 comment '最后更新值',
`last_time` datetime(0) not null comment '最后更新时间',
`count` int(0) not null default 0 comment '更新次数',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = 'serial_number记录表';

create table `sys_password_policy`  (
`id` int not null auto_increment comment '主键',
`repeatable_password_number` int not null comment '与旧密码重复次数',
`failed_login_maximum_number` int not null comment '登录失败最大次数',
`failed_login_maximum_time` int not null comment '登录失败的时间（minute）',
`password_expired_period` int not null comment '密码过期时间（day）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '密码配置';
insert into `sys_password_policy` values (1, 3, 5, 10, 30, 0, sysdate(), sysdate());

create table `sys_login_locked`  (
`id` bigint(0) not null auto_increment comment '自增id',
`login_name` varchar(1000) null default null comment '登录名',
`login_fail_count` int(0) null default null comment '连续登录失败次数',
`lock_flag` tinyint(0) null default 0 comment '锁定状态:1锁定，0未锁定',
`login_lock_begin_time` datetime(0) null default null comment '连续登录失败锁定开始时间',
`login_lock_end_time` datetime(0) null default null comment '连续登录失败锁定结束时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '登录失败次数记录表';

create table `sys_email_send_history`  (
`id` int(0) not null auto_increment comment '主键',
`sent_to` varchar(1000) not null comment '发送对象',
`cc` varchar(1000) default null comment '抄送对象',
`title` varchar(500) not null comment '标题',
`content` varchar(4056) not null comment '内容',
`file_id_array` varchar(1000) default null comment '附件',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '通知类型';

create table if not exists `sys_info` (
`id` int(0) not null auto_increment comment '主键',
`email_host` varchar(128) not null comment '主机ip',
`email_port` varchar(128) not null comment '主机端口',
`email_username` varchar(128) not null comment '账户',
`email_password` varchar(128) not null comment '密码',
`email_from` varchar(128) not null comment '设置发送方,如：xx<postmaster@xxx.com>',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)  comment='系统信息';
insert into `sys_info` values (1, 'smtp.163.com', '25', 'postmaster@163.com', 'postmaster', 'postmaster', 0, sysdate(), sysdate());

drop table if exists `sys_message`;
create table `sys_message`  (
`id` int(0) not null auto_increment comment '主键',
`message_type` smallint(0) not null comment '消息类型',
`receiver_user_type` int(0) not null comment '接收者用户类型',
`receiver_user_id` bigint(0) not null comment '接收者用户id',
`data_id` varchar(500) null default '' comment '相关数据id',
`title` varchar(1000) not null comment '标题',
`content` text not null comment '内容',
`read_flag` tinyint(1) not null default 0 comment '是否已读',
`read_time` datetime(0) null default null comment '已读时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree,
index `idx_msg`(`message_type`, `receiver_user_type`, `receiver_user_id`) using btree
)comment = '通知消息';


create table if not exists `sys_form` (
`id` int auto_increment comment '主键',
`name` varchar(255) not null comment '表单名称',
`json_text` varchar(4056) default null comment '表单内容',
`publish` tinyint(0) not null default 0 comment '发布（true/false）',
`remark` varchar(512)  null default null comment '备注',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
) comment='表单信息';

create table `sys_form_item` (
`id` int auto_increment comment '主键',
`form_id` int not null comment '表单id',
`control_type` varchar(45) not null comment '控件类型 (input、textarea、select、radio、checkbox、date、time、datetime、number、file)',
`label` varchar(128) not null comment '显示名称',
`label_code` varchar(64) not null comment '显示名称code(业务唯一)',
`options` varchar(512) default null  comment '选项数组 select/radio/checkbox',
`required` tinyint(0) not null default 0 comment '必填标识（true、false）',
`order_number` int not null,
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment='表单输入项信息';

create table `sys_form_record` (
`id` int auto_increment comment '主键',
`form_id` int not null comment '表单id',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment='表单业务数据信息';

create table `sys_form_record_item` (
`id` int auto_increment comment '主键',
`form_id` int not null comment '表单id',
`form_item_id` int not null comment '表单项id',
`form_record_id` int not null comment '表单业务id',
`current_value` varchar(512) default null comment '当前值',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment='表单业务数据输入项信息';