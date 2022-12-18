DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `created_by` bigint(20) NOT NULL COMMENT '创建人',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
    `status` int(4) NOT NULL COMMENT '状态（1/正常 2/停用）',
    `del_flag` int(1) NOT NULL DEFAULT 1 COMMENT '删除标识（1/正常 2/删除）',
    `nickname` varchar(256) NOT NULL COMMENT '昵称',
    `account` varchar(128) NOT NULL COMMENT '账号',
    `password` varchar(128) NOT NULL COMMENT '密码',
    `salt` varchar(128) NOT NULL COMMENT '密码盐',
    `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(128) DEFAULT NULL COMMENT '手机号',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
    `type` int(1) NOT NULL  COMMENT '类型（1/自定义 2/内置）',
    PRIMARY KEY (`id`),
    KEY `idx_pa_account` (`account`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
INSERT INTO sys_user(id, created_by, created_time, updated_by, updated_time, status, del_flag, nickname, account, password, salt, email, phone, last_login_time, type)
VALUES(1589631293412503554, 1589631293412503554, '2022-10-31 07:20:54', null, null, 1, 1, '麦兜', 'admin', '7a69d7186df1a65ed7af2ba00747488e2415bf1a', 'c7e87439-aef3-48e0-be26-678d0ab99345', 'admin@mcdull.com', '18238352145', null, 2);

DROP TABLE IF EXISTS `sys_user_login`;
CREATE TABLE IF NOT EXISTS `sys_user_login` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `user_id` bigint(20) NOT NULL COMMENT '用户主键',
    `token` varchar(64) NOT NULL COMMENT '所用token',
    `type` int(4) NOT NULL COMMENT '类型（1/登录 2/注销）',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录记录表';

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE IF NOT EXISTS `sys_dict` (
    `code` varchar(32) NOT NULL COMMENT '编码',
    `parent_code` varchar(32) DEFAULT NULL COMMENT '父级',
    `name` varchar(256) NOT NULL COMMENT '名称',
    `name_short` varchar(32) DEFAULT NULL COMMENT '简码',
    `select_type` varchar(32) NOT NULL COMMENT '类别',
    `sort` int(4) NOT NULL COMMENT '排序',
    `defaulted` int(1) NOT NULL COMMENT '当前是否为默认（1/是 2/否）',
    `status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
    `remark` varchar(128) DEFAULT NULL COMMENT '备注',
    `language` varchar(32) DEFAULT NULL,
    `del_flag` int(1) NOT NULL DEFAULT '1' COMMENT '1-未删除,2-删除'
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='公共码表';

INSERT INTO `sys_dict` VALUES ('0', null, '未知的性别', null, 'sex_type', 1, 2, 1, '性别代码',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('1', null, '男', null, 'sex_type', 2, 2, 1, '性别代码',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '女', null, 'sex_type', 3, 2, 1, '性别代码',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('9', null, '未说明的性别', null, 'sex_type', 4, 1, 1, '性别代码',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('1', null, '启用', null, 'status_type', 1, 1, 1, '状态标识',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '停用', null, 'status_type', 2, 2, 1, '状态标识',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('1', null, '正常', null, 'del_flag_type', 1, 1, 1, '删除标识',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '删除', null, 'del_flag_type', 2, 2, 1, '删除标识',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('zh-CN', null, '中文', null, 'language_type', 1, 1, 1, '语言类型',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('en-US', null, '英文', null, 'language_type', 2, 2, 1, '语言类型',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('1', null, '自定义', null, 'data_type', 1, 1, 1, '数据类型',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '内置', null, 'data_type', 2, 2, 1, '数据类型',  'zh-CN',  1);


DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
`id` bigint(20) NOT NULL COMMENT '主键',
`account_id` bigint(20)  NOT NULL COMMENT '操作人的账号主键',
`tenant_id` bigint(20)  DEFAULT NULL COMMENT '租户主键',
`created_time` datetime  NOT NULL COMMENT '创建时间',
`client_ip` varchar(128)  DEFAULT NULL COMMENT '客户端ip',
`user_agent` text  DEFAULT NULL COMMENT '用户代理',
`time` datetime  DEFAULT NULL COMMENT '操作时间',
`method` varchar(128)  DEFAULT NULL COMMENT '请求方法',
`path` varchar(128)   DEFAULT NULL COMMENT '路径',
`time_taken` bigint(20) DEFAULT NULL COMMENT '耗时',
`status` bigint(20) DEFAULT NULL COMMENT 'http状态',
`parameter_map` varchar(1024)  DEFAULT NULL COMMENT '参数map',
`request_body` longtext  DEFAULT NULL COMMENT '请求体',
`headers` text  DEFAULT NULL COMMENT '请求头',
`response_body` longtext  DEFAULT NULL COMMENT '响应头',
PRIMARY KEY (`id`) USING BTREE

) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志记录 ' ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE IF NOT EXISTS `sys_role` (
`id` bigint(20) NOT NULL COMMENT '主键',
`created_by` bigint(20) NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
`name` varchar(512) NOT NULL COMMENT '名称',
`code` varchar(128) DEFAULT NULL COMMENT '编码',
`description` varchar(128) DEFAULT NULL COMMENT '描述',
`del_flag` int(1) NOT NULL COMMENT '删除标识（1/正常 2/已删除）',
`type` int(1) NOT NULL  COMMENT '类型（1/自定义 2/内置）',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

INSERT INTO sys_role
(id, created_by, created_time, updated_by, updated_time, status, name, code, description, del_flag, `type`)
VALUES(1, 1589631293412503554, '2022-10-31 07:20:54', 1589631293412503554, '2022-12-05 22:53:42', 1, '系统管理员', 'sys-admin', '这是系统管理员', 1, 2);
INSERT INTO sys_role
(id, created_by, created_time, updated_by, updated_time, status, name, code, description, del_flag, `type`)
VALUES(2, 1589631293412503554, '2022-10-31 07:20:54', 1589631293412503554, '2022-12-05 22:53:42', 1, '日志管理员', 'log-admin', '这是日志管理员', 1, 2);

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `user_id` bigint(20) NOT NULL COMMENT '角色主键',
    `role_id` bigint(20) NOT NULL COMMENT '菜单主键',
    PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色中间表';

INSERT INTO sys_user_role VALUES(1, '2022-10-31 07:20:54', 1589631293412503554, 1);
INSERT INTO sys_user_role VALUES(2, '2022-10-31 07:20:54', 1589631293412503554, 2);

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE IF NOT EXISTS `sys_menu` (
`id` bigint(20) NOT NULL COMMENT '主键',
`created_by` bigint(20) NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
`parent_id` bigint(20) NOT NULL COMMENT '父级',
`name` varchar(128) NOT NULL COMMENT '名称',
`icon` varchar(128) DEFAULT NULL COMMENT '图标',
`sort` int(16) NOT NULL COMMENT '排序',
`res_code` varchar(128) NOT NULL COMMENT '模块code 如sys:user:list',
`path` varchar(128) DEFAULT NULL COMMENT '路由',
`component` varchar(128) DEFAULT NULL COMMENT '组件',
`type` varchar(8) NOT NULL COMMENT '类型(sys/子系统 menu/菜单、button/按钮)',
`del_flag` int(1) NOT NULL COMMENT '删除标识（1/正常 2/已删除）',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';
BEGIN;
-- 系统级
INSERT INTO sys_menu VALUES(100, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 0, '基础管理系统', NULL, 1, 'sys', NULL, NULL, 'sys', 1);

-- 菜单级
INSERT INTO sys_menu VALUES(10010001, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 100, '用户管理', NULL, 1, 'sys:user', NULL, NULL, 'menu', 1);
INSERT INTO sys_menu VALUES(10010002, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 100, '角色管理', NULL, 2, 'sys:role', NULL, NULL, 'menu', 1);
INSERT INTO sys_menu VALUES(10010009, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 100, '日志管理', NULL, 3, 'sys:log', NULL, NULL, 'menu', 1);

-- 按钮级
INSERT INTO sys_menu VALUES(100100011001, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 10010001, '查看', NULL, 1, 'sys:user:view', NULL, NULL, 'button', 1);
INSERT INTO sys_menu VALUES(100100011002, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 10010001, '添加', NULL, 2, 'sys:user:add', NULL, NULL, 'button', 1);
INSERT INTO sys_menu VALUES(100100011003, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 10010001, '更新', NULL, 3, 'sys:user:update', NULL, NULL, 'button', 1);
INSERT INTO sys_menu VALUES(100100011004, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 10010001, '删除', NULL, 4, 'sys:user:delete', NULL, NULL, 'button', 1);
INSERT INTO sys_menu VALUES(100100011005, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 10010001, '启停', NULL, 5, 'sys:user:status', NULL, NULL, 'button', 1);
INSERT INTO sys_menu VALUES(100100011006, 1589631293412503554, '2022-10-31 07:20:54', NULL, NULL, 1, 10010001, '导出', NULL, 5, 'sys:user:export', NULL, NULL, 'button', 1);
COMMIT;

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
`id` bigint(20) NOT NULL COMMENT '主键',
`created_time` datetime NOT NULL COMMENT '创建时间',
`role_id` bigint(20) NOT NULL COMMENT '角色主键',
`menu_id` bigint(20) NOT NULL COMMENT '菜单主键',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单资源中间表';
BEGIN;
INSERT INTO sys_role_menu VALUES(1001, '2022-10-31 07:20:54', 1, 100);
INSERT INTO sys_role_menu VALUES(1002, '2022-10-31 07:20:54', 1, 10010001);
INSERT INTO sys_role_menu VALUES(1003, '2022-10-31 07:20:54', 1, 100100011001);
INSERT INTO sys_role_menu VALUES(1004, '2022-10-31 07:20:54', 1, 100100011002);
INSERT INTO sys_role_menu VALUES(1005, '2022-10-31 07:20:54', 1, 100100011003);
INSERT INTO sys_role_menu VALUES(1006, '2022-10-31 07:20:54', 1, 100100011004);
INSERT INTO sys_role_menu VALUES(1007, '2022-10-31 07:20:54', 1, 100100011005);
INSERT INTO sys_role_menu VALUES(1008, '2022-10-31 07:20:54', 1, 100100011006);
COMMIT;
