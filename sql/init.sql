DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `nickname` varchar(256) NOT NULL COMMENT '昵称',
    `account` varchar(128) NOT NULL COMMENT '账号',
    `password` varchar(128) NOT NULL COMMENT '密码',
    `salt` varchar(128) NOT NULL COMMENT '密码盐',
    `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(128) DEFAULT NULL COMMENT '手机号',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
    `type` int(1) NOT NULL  COMMENT '类型（1/自定义 2/内置）',
    `created_by` bigint(20) NOT NULL COMMENT '创建人',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
    `status` int(4) NOT NULL COMMENT '状态（1/正常 2/停用）',
    `del_flag` bit(1) NOT NULL COMMENT '删除标识（0/正常 1/删除）',
    `del_by` bigint(20) DEFAULT NULL COMMENT '删除人',
    PRIMARY KEY (`id`),
    KEY `idx_pa_account` (`account`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
INSERT INTO sys_user VALUES(1589631293412503554, '麦兜', 'admin', '7a69d7186df1a65ed7af2ba00747488e2415bf1a', 'c7e87439-aef3-48e0-be26-678d0ab99345', 'admin@mcdull.com', '18238352145', null, 2, 1589631293412503554, '2022-10-31 07:20:54', null, null, 1, 0, null);

DROP TABLE IF EXISTS `sys_user_login`;
CREATE TABLE IF NOT EXISTS `sys_user_login` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `user_id` bigint(20) NOT NULL COMMENT '用户主键',
    `token` varchar(64) NOT NULL COMMENT '所用token',
    `type` int(4) NOT NULL COMMENT '类型（1/登录 2/注销）',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录/注销';

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
    `del_flag` bit(1) NOT NULL COMMENT '删除标识（0/正常 1/删除）'
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='公共码表';

INSERT INTO `sys_dict` VALUES ('0', null, '未知的性别', null, 'sex_type', 1, 2, 1, '性别代码',  'zh-CN',  0);
INSERT INTO `sys_dict` VALUES ('1', null, '男', null, 'sex_type', 2, 2, 1, '性别代码',  'zh-CN',  0);
INSERT INTO `sys_dict` VALUES ('2', null, '女', null, 'sex_type', 3, 2, 1, '性别代码',  'zh-CN',  0);
INSERT INTO `sys_dict` VALUES ('9', null, '未说明的性别', null, 'sex_type', 4, 1, 1, '性别代码',  'zh-CN',  0);

INSERT INTO `sys_dict` VALUES ('1', null, '启用', null, 'status_type', 1, 1, 1, '状态标识',  'zh-CN',  0);
INSERT INTO `sys_dict` VALUES ('2', null, '停用', null, 'status_type', 2, 2, 1, '状态标识',  'zh-CN',  0);

INSERT INTO `sys_dict` VALUES ('false', null, '正常', null, 'del_flag_type', 1, 1, 1, '删除标识',  'zh-CN',  0);
INSERT INTO `sys_dict` VALUES ('true', null, '删除', null, 'del_flag_type', 2, 2, 1, '删除标识',  'zh-CN',  0);

INSERT INTO `sys_dict` VALUES ('zh-CN', null, '中文', null, 'language_type', 1, 1, 1, '语言类型',  'zh-CN',  0);
INSERT INTO `sys_dict` VALUES ('en-US', null, '英文', null, 'language_type', 2, 2, 1, '语言类型',  'zh-CN',  0);

INSERT INTO `sys_dict` VALUES ('1', null, '自定义', null, 'data_type', 1, 1, 1, '数据类型',  'zh-CN',  0);
INSERT INTO `sys_dict` VALUES ('2', null, '内置', null, 'data_type', 2, 2, 1, '数据类型',  'zh-CN',  0);


DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
`id` bigint(20) NOT NULL COMMENT '主键',
`account_id` bigint(20)  NOT NULL COMMENT '操作人的账号主键',
`tenant_id` bigint(20)  DEFAULT NULL COMMENT '租户主键',
`created_time` datetime  NOT NULL COMMENT '创建时间',
`client_ip` varchar(128)  NOT NULL COMMENT '客户端ip',
`user_agent` text  NOT NULL COMMENT '用户代理',
`method` varchar(128)  NOT NULL COMMENT '请求方法',
`path` varchar(128)   NOT NULL COMMENT '路径',
`trace_id` varchar(128)   NOT NULL COMMENT '日志跟踪id',
`time_taken` bigint(20) NOT NULL COMMENT '耗时',
`parameter_map` varchar(1024)  DEFAULT NULL COMMENT '参数map',
`headers` text  NOT NULL COMMENT '请求头',
`model` varchar(128) NOT NULL COMMENT '所属系统/模块',
`menu` varchar(256) NOT NULL COMMENT '所属菜单',
`type` varchar(16) NOT NULL COMMENT '所属操作类型',
PRIMARY KEY (`id`) USING BTREE

) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户操作日志';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE IF NOT EXISTS `sys_role` (
`id` bigint(20) NOT NULL COMMENT '主键',
`name` varchar(512) NOT NULL COMMENT '名称',
`code` varchar(128) DEFAULT NULL COMMENT '编码',
`description` varchar(128) DEFAULT NULL COMMENT '描述',
`type` int(1) NOT NULL  COMMENT '类型（1/自定义 2/内置）',
`created_by` bigint(20) NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
`del_flag` bit(1) NOT NULL COMMENT '删除标识（0/正常 1/删除）',
`del_by` bigint(20) DEFAULT NULL COMMENT '删除人',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

INSERT INTO sys_role VALUES(0, '系统管理员', 'sys-admin', '这是系统管理员', 2, 1589631293412503554, '2022-10-31 07:20:54', 1589631293412503554, '2022-12-05 22:53:42', 1, 0, null);
INSERT INTO sys_role VALUES(1, '日志管理员', 'log-admin', '这是日志管理员', 1, 1589631293412503554, '2022-10-31 07:20:54', 1589631293412503554, '2022-12-05 22:53:42', 1, 0, null);

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `user_id` bigint(20) NOT NULL COMMENT '角色主键',
    `role_id` bigint(20) NOT NULL COMMENT '菜单主键',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`) USING BTREE,
    KEY `idx_role_id` (`role_id`) USING BTREE
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色中间表';

INSERT INTO sys_user_role VALUES(1, '2022-10-31 07:20:54', 1589631293412503554, 1);
INSERT INTO sys_user_role VALUES(2, '2022-10-31 07:20:54', 1589631293412503554, 2);

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE IF NOT EXISTS `sys_menu` (
`code` varchar(128) NOT NULL COMMENT 'code',
`parent_code` varchar(128) NOT NULL COMMENT '父级code',
`name` varchar(128) NOT NULL COMMENT '名称',
`sort` int(16) NOT NULL COMMENT '排序',
`path` varchar(128) DEFAULT NULL COMMENT '路由',
`type` varchar(8) NOT NULL COMMENT '类型(sys/子系统 menu/菜单、button/按钮)',
`status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';
BEGIN;
-- 系统级
INSERT INTO sys_menu VALUES('sys', 'root', '基础管理系统', 1, NULL,   'sys', 1);
-- 菜单级
INSERT INTO sys_menu VALUES('sys:user', 'sys', '用户管理', 1, NULL, 'menu', 1);
INSERT INTO sys_menu VALUES('sys:role', 'sys', '角色管理', 2, NULL, 'menu', 1);
INSERT INTO sys_menu VALUES('sys:log',  'sys', '日志管理', 3, NULL, 'menu', 1);
-- 按钮级
INSERT INTO sys_menu VALUES('sys:user:view',   'sys:user', '查看', 1, NULL, 'button', 1);
INSERT INTO sys_menu VALUES('sys:user:save',   'sys:user', '添加', 2, NULL, 'button', 1);
INSERT INTO sys_menu VALUES('sys:user:update', 'sys:user', '更新', 3, NULL, 'button', 1);
INSERT INTO sys_menu VALUES('sys:user:delete', 'sys:user', '删除', 4, NULL, 'button', 1);
INSERT INTO sys_menu VALUES('sys:user:status', 'sys:user', '启停', 5, NULL, 'button', 1);
INSERT INTO sys_menu VALUES('sys:user:export', 'sys:user', '导出', 5, NULL, 'button', 1);
COMMIT;

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
`id` bigint(20) NOT NULL COMMENT '主键',
`created_time` datetime NOT NULL COMMENT '创建时间',
`role_id` bigint(20) NOT NULL COMMENT '角色主键',
`menu_code` varchar(128) NOT NULL COMMENT '菜单code',
KEY `idx_role_id` (`role_id`) USING BTREE,
KEY `idx_menu_code` (`menu_code`) USING BTREE,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色单资源中间表';
BEGIN;
INSERT INTO sys_role_menu VALUES(1001, '2022-10-31 07:20:54', 1, 'sys');
INSERT INTO sys_role_menu VALUES(1002, '2022-10-31 07:20:54', 1, 'sys:user');
INSERT INTO sys_role_menu VALUES(1003, '2022-10-31 07:20:54', 1, 'sys:user:view');
INSERT INTO sys_role_menu VALUES(1004, '2022-10-31 07:20:54', 1, 'sys:user:save');
INSERT INTO sys_role_menu VALUES(1005, '2022-10-31 07:20:54', 1, 'sys:user:update');
INSERT INTO sys_role_menu VALUES(1006, '2022-10-31 07:20:54', 1, 'sys:user:delete');
INSERT INTO sys_role_menu VALUES(1007, '2022-10-31 07:20:54', 1, 'sys:user:status');
INSERT INTO sys_role_menu VALUES(1008, '2022-10-31 07:20:54', 1, 'sys:user:export');
COMMIT;


DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
`id` bigint(20) NOT NULL COMMENT '主键',
`title` varchar(512) NOT NULL COMMENT '公告标题',
`content` longtext NOT NULL COMMENT '公告内容',
`type` tinyint NOT NULL COMMENT '公告类型（1通知 2公告）',
`status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
`created_by` bigint(20) NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`del_flag` bit(1) NOT NULL COMMENT '删除标识（0/正常 1/删除）',
`del_by` bigint(20) DEFAULT NULL COMMENT '删除人',
PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';