DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` bigint NOT NULL COMMENT '主键',
    `nick_name` varchar(256) NOT NULL COMMENT '昵称',
    `account` varchar(128) NOT NULL COMMENT '账号',
    `password` varchar(128) NOT NULL COMMENT '密码',
    `salt` varchar(128) NOT NULL COMMENT '密码盐',
    `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(128) DEFAULT NULL COMMENT '手机号',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
    `type` int NOT NULL  COMMENT '类型（1/自定义 2/内置）',
    `dept_id` bigint NOT NULL COMMENT '部门id',
    `created_by` bigint NOT NULL COMMENT '创建人',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `updated_by` bigint DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
    `status`  char(1)         default null               comment '状态（1正常 2停用）',
    `del_flag` int NOT NULL COMMENT '删除标识（1/正常 2/删除）',
    `del_by` bigint DEFAULT NULL COMMENT '删除人',
    PRIMARY KEY (`id`)
    );
INSERT INTO sys_user VALUES(1589631293412503554, '麦兜', 'admin', '7a69d7186df1a65ed7af2ba00747488e2415bf1a', 'c7e87439-aef3-48e0-be26-678d0ab99345', 'admin@mcdull.com', '18238352145', null, 2, 100,1589631293412503554, '2022-10-31 07:20:54', null, null, '1', 1, null);


drop table if exists sys_post;
create table sys_post
(
    id            bigint      not null                   comment '岗位ID',
    post_code     varchar(64)     not null                   comment '岗位编码',
    post_name     varchar(50)     not null                   comment '岗位名称',
    post_sort     int          not null                   comment '显示顺序',
    status        char(1)         not null                   comment '状态（1正常 2停用）',
    created_by     bigint      default null               comment '创建者',
    created_time   datetime        not null                   comment '创建时间',
    updated_by     varchar(64)     default null			     comment '更新者',
    updated_time   datetime        default null               comment '更新时间',
    remark        varchar(500)    default null               comment '备注',
    del_flag int NOT NULL COMMENT '删除标识（1/正常 2/删除）',
    del_by bigint DEFAULT NULL COMMENT '删除人',
    primary key (id)
) ;

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post values(1, 'ceo',  '董事长',    1, '1', null, CURRENT_TIMESTAMP(), '', null, '', 1, null);
insert into sys_post values(2, 'se',   '项目经理',  2, '1', null, CURRENT_TIMESTAMP(), '', null, '', 1, null);
insert into sys_post values(3, 'hr',   '人力资源',  3, '1', null, CURRENT_TIMESTAMP(), '', null, '', 1, null);
insert into sys_post values(4, 'user', '普通员工',  4, '1', null, CURRENT_TIMESTAMP(), '', null, '', 1, null);

DROP TABLE IF EXISTS `sys_user_login`;
CREATE TABLE IF NOT EXISTS `sys_user_login` (
    `id` bigint NOT NULL COMMENT '主键',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `account` varchar(64) NOT NULL COMMENT '用户',
    `remark` varchar(64) default '' COMMENT '备注',
    `type` char(1) NOT NULL COMMENT '类型（1/成功 2/失败）',
    `browser` varchar(64)  default ''  COMMENT '浏览器类型',
    `os` varchar(64)  default ''  COMMENT '操作系统',
    `operation_type` char(1) NOT NULL COMMENT '类型（1/登录 2/注销）',
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE  `sys_dict` (
    `code` varchar(32) NOT NULL COMMENT '编码',
    `parent_code` varchar(32) DEFAULT NULL COMMENT '父级',
    `name` varchar(256) NOT NULL COMMENT '名称',
    `name_short` varchar(32) DEFAULT NULL COMMENT '简码',
    `select_type` varchar(32) NOT NULL COMMENT '类别',
    `sort` int NOT NULL COMMENT '排序',
    `defaulted` int NOT NULL COMMENT '当前是否为默认（1/是 2/否）',
    `status`    char(1)         not null               comment '状态（1正常 2停用）',
    `remark` varchar(128) DEFAULT NULL COMMENT '备注',
    `language` varchar(32) DEFAULT NULL,
    `del_flag` int NOT NULL COMMENT '删除标识（1/正常 2/删除）'
    );

INSERT INTO `sys_dict` VALUES ('0', null, '未知的性别', null, 'sex_type', 1, 2, '1', '性别代码',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('1', null, '男', null, 'sex_type', 2, 2, '1', '性别代码',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '女', null, 'sex_type', 3, 2, '1', '性别代码',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('9', null, '未说明的性别', null, 'sex_type', 4, 1, '1', '性别代码',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('1', null, '启用', null, 'status_type', 1, 1, '1', '状态标识',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '停用', null, 'status_type', 2, 2, '1', '状态标识',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('false', null, '正常', null, 'del_flag_type', 1, 1, '1', '删除标识',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('true', null, '删除', null, 'del_flag_type', 2, 2, '1', '删除标识',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('zh-CN', null, '中文', null, 'language_type', 1, 1, '1', '语言类型',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('en-US', null, '英文', null, 'language_type', 2, 2, '1', '语言类型',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('1', null, '自定义', null, 'data_type', 1, 1, '1', '数据类型',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '内置', null, 'data_type', 2, 2, '1', '数据类型',  'zh-CN',  1);


DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
`id` bigint NOT NULL COMMENT '主键',
`account_id` bigint  NOT NULL COMMENT '操作人的账号主键',
`tenant_id` bigint  DEFAULT NULL COMMENT '租户主键',
`created_time` datetime  NOT NULL COMMENT '创建时间',
`client_ip` varchar(128)  NOT NULL COMMENT '客户端ip',
`user_agent` varchar(128)  NOT NULL COMMENT '用户代理',
`method` varchar(128)  NOT NULL COMMENT '请求方法',
`path` varchar(128)   NOT NULL COMMENT '路径',
`trace_id` varchar(128)   NOT NULL COMMENT '日志跟踪id',
`time_taken` bigint NOT NULL COMMENT '耗时',
`parameter_map` varchar(1024)  DEFAULT NULL COMMENT '参数map',
`headers` varchar(1024)  NOT NULL COMMENT '请求头',
`button` varchar(16) NOT NULL COMMENT '所属操作类型',
PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE IF NOT EXISTS `sys_role` (
`id` bigint NOT NULL COMMENT '主键',
`name` varchar(512) NOT NULL COMMENT '名称',
`code` varchar(128) DEFAULT NULL COMMENT '编码',
`description` varchar(128) DEFAULT NULL COMMENT '描述',
`type` int NOT NULL  COMMENT '类型（1/自定义 2/内置）',
`created_by` bigint NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`status` char(1)         not null               comment '状态（1正常 2停用）',
`del_flag` int NOT NULL COMMENT '删除标识（1/正常 2/删除）',
`del_by` bigint DEFAULT NULL COMMENT '删除人',
PRIMARY KEY (`id`)
) ;

INSERT INTO sys_role VALUES(1, '系统管理员', 'admin', '这是系统管理员', 2, 1589631293412503554, '2022-10-31 07:20:54', 1589631293412503554, '2022-12-05 22:53:42', '1', 0, null);
INSERT INTO sys_role VALUES(2, '日志管理员', 'log-admin', '这是日志管理员', 1, 1589631293412503554, '2022-10-31 07:20:54', 1589631293412503554, '2022-12-05 22:53:42', '1', 0, null);

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` bigint NOT NULL COMMENT '主键',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `user_id` bigint NOT NULL COMMENT '用户主键',
    `role_id` bigint NOT NULL COMMENT '角色主键',
    PRIMARY KEY (`id`)
 ) ;

INSERT INTO sys_user_role VALUES(1, '2022-10-31 07:20:54', 1589631293412503554, 1);
INSERT INTO sys_user_role VALUES(2, '2022-10-31 07:20:54', 1589631293412503554, 2);

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` bigint NOT NULL COMMENT '主键',
    `name` varchar(128) NOT NULL COMMENT '名称',
    `parent_id` varchar(128) NOT NULL COMMENT '父菜单ID',
    `order_num` int NOT NULL COMMENT '显示顺序',
    `path` varchar(128) DEFAULT NULL COMMENT '路由地址',
    `component` varchar(256) DEFAULT NULL COMMENT '组件路径',
    `query` varchar(256) DEFAULT NULL COMMENT '路由参数',
    `is_frame` int NOT NULL COMMENT '是否为外链（0是 1否）',
    `is_cache` int NOT NULL COMMENT '是否缓存（0缓存 1不缓存）',
    `menu_type` varchar(8) NOT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `visible` char(1) NOT NULL COMMENT '菜单状态（0显示 1隐藏）',
    `status` char(1)         not null               comment '状态（1正常 2停用）',
    `perms` varchar(128) NOT NULL COMMENT '权限标识',
    `icon` varchar(128) DEFAULT NULL COMMENT '菜单图标',
    `created_by` bigint NULL COMMENT '创建人',
    `created_time` datetime NULL COMMENT '创建时间',
    `updated_by` bigint NULL COMMENT '更新人',
    `updated_time` datetime NULL COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL comment '备注',
    PRIMARY KEY (`id`)
    ) ;

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu values('1', '系统管理', '0', '1', 'system',           null, '', 1, 0, 'M', '0', '1', '', 'system',   null, CURRENT_TIMESTAMP(), null, null, '系统管理目录');
insert into sys_menu values('2', '系统监控', '0', '2', 'monitor',          null, '', 1, 0, 'M', '0', '1', '', 'monitor',  null, CURRENT_TIMESTAMP(), null, null, '系统监控目录');
insert into sys_menu values('3', '系统工具', '0', '3', 'tool',             null, '', 1, 0, 'M', '0', '1', '', 'tool',     null, CURRENT_TIMESTAMP(), null, null, '系统工具目录');
insert into sys_menu values('4', '若依官网', '0', '4', 'http://ruoyi.vip', null, '', 0, 0, 'M', '0', '1', '', 'guide',    null, CURRENT_TIMESTAMP(), null, null, '若依官网地址');
-- 二级菜单
insert into sys_menu values('100',  '用户管理', '1',   '1', 'user',       'system/user/index',        '', 1, 0, 'C', '0', '1', 'system:user:list',        'user',          null, CURRENT_TIMESTAMP(), null, null, '用户管理菜单');
insert into sys_menu values('101',  '角色管理', '1',   '2', 'role',       'system/role/index',        '', 1, 0, 'C', '0', '1', 'system:role:list',        'peoples',       null, CURRENT_TIMESTAMP(), null, null, '角色管理菜单');
insert into sys_menu values('102',  '菜单管理', '1',   '3', 'menu',       'system/menu/index',        '', 1, 0, 'C', '0', '1', 'system:menu:list',        'tree-table',    null, CURRENT_TIMESTAMP(), null, null, '菜单管理菜单');
insert into sys_menu values('103',  '部门管理', '1',   '4', 'dept',       'system/dept/index',        '', 1, 0, 'C', '0', '1', 'system:dept:list',        'tree',          null, CURRENT_TIMESTAMP(), null, null, '部门管理菜单');
insert into sys_menu values('104',  '岗位管理', '1',   '5', 'post',       'system/post/index',        '', 1, 0, 'C', '0', '1', 'system:post:list',        'post',          null, CURRENT_TIMESTAMP(), null, null, '岗位管理菜单');
insert into sys_menu values('105',  '字典管理', '1',   '6', 'dict',       'system/dict/index',        '', 1, 0, 'C', '0', '1', 'system:dict:list',        'dict',          null, CURRENT_TIMESTAMP(), null, null, '字典管理菜单');
insert into sys_menu values('106',  '参数设置', '1',   '7', 'config',     'system/config/index',      '', 1, 0, 'C', '0', '1', 'system:config:list',      'edit',          null, CURRENT_TIMESTAMP(), null, null, '参数设置菜单');
insert into sys_menu values('107',  '通知公告', '1',   '8', 'notice',     'system/notice/index',      '', 1, 0, 'C', '0', '1', 'system:notice:list',      'message',       null, CURRENT_TIMESTAMP(), null, null, '通知公告菜单');
insert into sys_menu values('108',  '日志管理', '1',   '9', 'log',        '',                         '', 1, 0, 'M', '0', '1', '',                        'log',           null, CURRENT_TIMESTAMP(), null, null, '日志管理菜单');
insert into sys_menu values('109',  '在线用户', '2',   '1', 'online',     'monitor/online/index',     '', 1, 0, 'C', '0', '1', 'monitor:online:list',     'online',        null, CURRENT_TIMESTAMP(), null, null, '在线用户菜单');
insert into sys_menu values('110',  '定时任务', '2',   '2', 'job',        'monitor/job/index',        '', 1, 0, 'C', '0', '1', 'monitor:job:list',        'job',           null, CURRENT_TIMESTAMP(), null, null, '定时任务菜单');
insert into sys_menu values('111',  '数据监控', '2',   '3', 'druid',      'monitor/druid/index',      '', 1, 0, 'C', '0', '1', 'monitor:druid:list',      'druid',         null, CURRENT_TIMESTAMP(), null, null, '数据监控菜单');
insert into sys_menu values('112',  '服务监控', '2',   '4', 'server',     'monitor/server/index',     '', 1, 0, 'C', '0', '1', 'monitor:server:list',     'server',        null, CURRENT_TIMESTAMP(), null, null, '服务监控菜单');
insert into sys_menu values('113',  '缓存监控', '2',   '5', 'cache',      'monitor/cache/index',      '', 1, 0, 'C', '0', '1', 'monitor:cache:list',      'redis',         null, CURRENT_TIMESTAMP(), null, null, '缓存监控菜单');
insert into sys_menu values('114',  '缓存列表', '2',   '6', 'cacheList',  'monitor/cache/list',       '', 1, 0, 'C', '0', '1', 'monitor:cache:list',      'redis-list',    null, CURRENT_TIMESTAMP(), null, null, '缓存列表菜单');
insert into sys_menu values('115',  '表单构建', '3',   '1', 'build',      'tool/build/index',         '', 1, 0, 'C', '0', '1', 'tool:build:list',         'build',         null, CURRENT_TIMESTAMP(), null, null, '表单构建菜单');
insert into sys_menu values('116',  '代码生成', '3',   '2', 'gen',        'tool/gen/index',           '', 1, 0, 'C', '0', '1', 'tool:gen:list',           'code',          null, CURRENT_TIMESTAMP(), null, null, '代码生成菜单');
insert into sys_menu values('117',  '系统接口', '3',   '3', 'swagger',    'tool/swagger/index',       '', 1, 0, 'C', '0', '1', 'tool:swagger:list',       'swagger',       null, CURRENT_TIMESTAMP(), null, null, '系统接口菜单');
-- 三级菜单
insert into sys_menu values('500',  '操作日志', '108', '1', 'operlog',    'monitor/operlog/index',    '', 1, 0, 'C', '0', '1', 'monitor:operlog:list',    'form',          null, CURRENT_TIMESTAMP(), null, null, '操作日志菜单');
insert into sys_menu values('501',  '登录日志', '108', '2', 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '1', 'monitor:logininfor:list', 'logininfor',    null, CURRENT_TIMESTAMP(), null, null, '登录日志菜单');
-- 用户管理按钮
insert into sys_menu values('1000', '用户查询', '100', '1',  '', '', '', 1, 0, 'F', '0', '1', 'system:user:query',          '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1001', '用户新增', '100', '2',  '', '', '', 1, 0, 'F', '0', '1', 'system:user:add',            '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1002', '用户修改', '100', '3',  '', '', '', 1, 0, 'F', '0', '1', 'system:user:edit',           '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1003', '用户删除', '100', '4',  '', '', '', 1, 0, 'F', '0', '1', 'system:user:remove',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1004', '用户导出', '100', '5',  '', '', '', 1, 0, 'F', '0', '1', 'system:user:export',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1005', '用户导入', '100', '6',  '', '', '', 1, 0, 'F', '0', '1', 'system:user:import',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1006', '重置密码', '100', '7',  '', '', '', 1, 0, 'F', '0', '1', 'system:user:resetPwd',       '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 角色管理按钮
insert into sys_menu values('1007', '角色查询', '101', '1',  '', '', '', 1, 0, 'F', '0', '1', 'system:role:query',          '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1008', '角色新增', '101', '2',  '', '', '', 1, 0, 'F', '0', '1', 'system:role:add',            '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1009', '角色修改', '101', '3',  '', '', '', 1, 0, 'F', '0', '1', 'system:role:edit',           '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1010', '角色删除', '101', '4',  '', '', '', 1, 0, 'F', '0', '1', 'system:role:remove',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1011', '角色导出', '101', '5',  '', '', '', 1, 0, 'F', '0', '1', 'system:role:export',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 菜单管理按钮
insert into sys_menu values('1012', '菜单查询', '102', '1',  '', '', '', 1, 0, 'F', '0', '1', 'system:menu:query',          '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1013', '菜单新增', '102', '2',  '', '', '', 1, 0, 'F', '0', '1', 'system:menu:add',            '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1014', '菜单修改', '102', '3',  '', '', '', 1, 0, 'F', '0', '1', 'system:menu:edit',           '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1015', '菜单删除', '102', '4',  '', '', '', 1, 0, 'F', '0', '1', 'system:menu:remove',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 部门管理按钮
insert into sys_menu values('1016', '部门查询', '103', '1',  '', '', '', 1, 0, 'F', '0', '1', 'system:dept:query',          '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1017', '部门新增', '103', '2',  '', '', '', 1, 0, 'F', '0', '1', 'system:dept:add',            '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1018', '部门修改', '103', '3',  '', '', '', 1, 0, 'F', '0', '1', 'system:dept:edit',           '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1019', '部门删除', '103', '4',  '', '', '', 1, 0, 'F', '0', '1', 'system:dept:remove',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 岗位管理按钮
insert into sys_menu values('1020', '岗位查询', '104', '1',  '', '', '', 1, 0, 'F', '0', '1', 'system:post:query',          '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1021', '岗位新增', '104', '2',  '', '', '', 1, 0, 'F', '0', '1', 'system:post:add',            '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1022', '岗位修改', '104', '3',  '', '', '', 1, 0, 'F', '0', '1', 'system:post:edit',           '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1023', '岗位删除', '104', '4',  '', '', '', 1, 0, 'F', '0', '1', 'system:post:remove',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1024', '岗位导出', '104', '5',  '', '', '', 1, 0, 'F', '0', '1', 'system:post:export',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 字典管理按钮
insert into sys_menu values('1025', '字典查询', '105', '1', '#', '', '', 1, 0, 'F', '0', '1', 'system:dict:query',          '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1026', '字典新增', '105', '2', '#', '', '', 1, 0, 'F', '0', '1', 'system:dict:add',            '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1027', '字典修改', '105', '3', '#', '', '', 1, 0, 'F', '0', '1', 'system:dict:edit',           '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1028', '字典删除', '105', '4', '#', '', '', 1, 0, 'F', '0', '1', 'system:dict:remove',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1029', '字典导出', '105', '5', '#', '', '', 1, 0, 'F', '0', '1', 'system:dict:export',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 参数设置按钮
insert into sys_menu values('1030', '参数查询', '106', '1', '#', '', '', 1, 0, 'F', '0', '1', 'system:config:query',        '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1031', '参数新增', '106', '2', '#', '', '', 1, 0, 'F', '0', '1', 'system:config:add',          '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1032', '参数修改', '106', '3', '#', '', '', 1, 0, 'F', '0', '1', 'system:config:edit',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1033', '参数删除', '106', '4', '#', '', '', 1, 0, 'F', '0', '1', 'system:config:remove',       '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1034', '参数导出', '106', '5', '#', '', '', 1, 0, 'F', '0', '1', 'system:config:export',       '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 通知公告按钮
insert into sys_menu values('1035', '公告查询', '107', '1', '#', '', '', 1, 0, 'F', '0', '1', 'system:notice:query',        '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1036', '公告新增', '107', '2', '#', '', '', 1, 0, 'F', '0', '1', 'system:notice:add',          '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1037', '公告修改', '107', '3', '#', '', '', 1, 0, 'F', '0', '1', 'system:notice:edit',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1038', '公告删除', '107', '4', '#', '', '', 1, 0, 'F', '0', '1', 'system:notice:remove',       '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 操作日志按钮
insert into sys_menu values('1039', '操作查询', '500', '1', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:operlog:query',      '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1040', '操作删除', '500', '2', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:operlog:remove',     '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1041', '日志导出', '500', '3', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:operlog:export',     '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 登录日志按钮
insert into sys_menu values('1042', '登录查询', '501', '1', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:logininfor:query',   '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1043', '登录删除', '501', '2', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:logininfor:remove',  '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1044', '日志导出', '501', '3', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:logininfor:export',  '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1045', '账户解锁', '501', '4', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:logininfor:unlock',  '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 在线用户按钮
insert into sys_menu values('1046', '在线查询', '109', '1', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:online:query',       '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1047', '批量强退', '109', '2', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:online:batchLogout', '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1048', '单条强退', '109', '3', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:online:forceLogout', '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 定时任务按钮
insert into sys_menu values('1049', '任务查询', '110', '1', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:job:query',          '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1050', '任务新增', '110', '2', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:job:add',            '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1051', '任务修改', '110', '3', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:job:edit',           '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1052', '任务删除', '110', '4', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:job:remove',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1053', '状态修改', '110', '5', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:job:changeStatus',   '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1054', '任务导出', '110', '6', '#', '', '', 1, 0, 'F', '0', '1', 'monitor:job:export',         '#', null, CURRENT_TIMESTAMP(), null, null, '');
-- 代码生成按钮
insert into sys_menu values('1055', '生成查询', '116', '1', '#', '', '', 1, 0, 'F', '0', '1', 'tool:gen:query',             '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1056', '生成修改', '116', '2', '#', '', '', 1, 0, 'F', '0', '1', 'tool:gen:edit',              '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1057', '生成删除', '116', '3', '#', '', '', 1, 0, 'F', '0', '1', 'tool:gen:remove',            '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1058', '导入代码', '116', '4', '#', '', '', 1, 0, 'F', '0', '1', 'tool:gen:import',            '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1059', '预览代码', '116', '5', '#', '', '', 1, 0, 'F', '0', '1', 'tool:gen:preview',           '#', null, CURRENT_TIMESTAMP(), null, null, '');
insert into sys_menu values('1060', '生成代码', '116', '6', '#', '', '', 1, 0, 'F', '0', '1', 'tool:gen:code',              '#', null, CURRENT_TIMESTAMP(), null, null, '');



DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
`id` bigint NOT NULL COMMENT '主键',
`created_time` datetime NOT NULL COMMENT '创建时间',
`role_id` bigint NOT NULL COMMENT '角色主键',
`menu_id` bigint NOT NULL COMMENT '菜单id',
PRIMARY KEY (`id`)
) ;

insert into sys_role_menu values (1001,   CURRENT_TIMESTAMP(),  '2', '1');
insert into sys_role_menu values (1002,   CURRENT_TIMESTAMP(),  '2', '2');
insert into sys_role_menu values (1003,   CURRENT_TIMESTAMP(),  '2', '3');
insert into sys_role_menu values (1004,   CURRENT_TIMESTAMP(),  '2', '4');

insert into sys_role_menu values (101001, CURRENT_TIMESTAMP(), '2', '100');
insert into sys_role_menu values (101002, CURRENT_TIMESTAMP(), '2', '101');
insert into sys_role_menu values (101003, CURRENT_TIMESTAMP(), '2', '102');
insert into sys_role_menu values (101004, CURRENT_TIMESTAMP(), '2', '103');
insert into sys_role_menu values (101005, CURRENT_TIMESTAMP(), '2', '104');
insert into sys_role_menu values (101006, CURRENT_TIMESTAMP(), '2', '105');
insert into sys_role_menu values (101007, CURRENT_TIMESTAMP(), '2', '106');
insert into sys_role_menu values (101008, CURRENT_TIMESTAMP(), '2', '107');
insert into sys_role_menu values (101009, CURRENT_TIMESTAMP(), '2', '108');
insert into sys_role_menu values (101010, CURRENT_TIMESTAMP(), '2', '109');
insert into sys_role_menu values (101011, CURRENT_TIMESTAMP(), '2', '110');
insert into sys_role_menu values (101012, CURRENT_TIMESTAMP(), '2', '111');
insert into sys_role_menu values (101013, CURRENT_TIMESTAMP(), '2', '112');
insert into sys_role_menu values (101014, CURRENT_TIMESTAMP(), '2', '113');
insert into sys_role_menu values (101015, CURRENT_TIMESTAMP(), '2', '114');
insert into sys_role_menu values (101016, CURRENT_TIMESTAMP(), '2', '115');
insert into sys_role_menu values (101017, CURRENT_TIMESTAMP(), '2', '116');
insert into sys_role_menu values (101018, CURRENT_TIMESTAMP(), '2', '117');
insert into sys_role_menu values (101019, CURRENT_TIMESTAMP(), '2', '500');
insert into sys_role_menu values (101020, CURRENT_TIMESTAMP(), '2', '501');

insert into sys_role_menu values (102001, CURRENT_TIMESTAMP(), '2', '1000');
insert into sys_role_menu values (102002, CURRENT_TIMESTAMP(), '2', '1001');
insert into sys_role_menu values (102003, CURRENT_TIMESTAMP(), '2', '1002');
insert into sys_role_menu values (102004, CURRENT_TIMESTAMP(), '2', '1003');
insert into sys_role_menu values (102005, CURRENT_TIMESTAMP(), '2', '1004');
insert into sys_role_menu values (102006, CURRENT_TIMESTAMP(), '2', '1005');
insert into sys_role_menu values (102007, CURRENT_TIMESTAMP(), '2', '1006');
insert into sys_role_menu values (102008, CURRENT_TIMESTAMP(), '2', '1007');
insert into sys_role_menu values (102009, CURRENT_TIMESTAMP(), '2', '1008');
insert into sys_role_menu values (102010, CURRENT_TIMESTAMP(), '2', '1009');
insert into sys_role_menu values (102011, CURRENT_TIMESTAMP(), '2', '1010');
insert into sys_role_menu values (102012, CURRENT_TIMESTAMP(), '2', '1011');
insert into sys_role_menu values (102013, CURRENT_TIMESTAMP(), '2', '1012');
insert into sys_role_menu values (102014, CURRENT_TIMESTAMP(), '2', '1013');
insert into sys_role_menu values (102015, CURRENT_TIMESTAMP(), '2', '1014');
insert into sys_role_menu values (102016, CURRENT_TIMESTAMP(), '2', '1015');
insert into sys_role_menu values (102017, CURRENT_TIMESTAMP(), '2', '1016');
insert into sys_role_menu values (102018, CURRENT_TIMESTAMP(), '2', '1017');
insert into sys_role_menu values (102019, CURRENT_TIMESTAMP(), '2', '1018');
insert into sys_role_menu values (102020, CURRENT_TIMESTAMP(), '2', '1019');
insert into sys_role_menu values (102021, CURRENT_TIMESTAMP(), '2', '1020');
insert into sys_role_menu values (102022, CURRENT_TIMESTAMP(), '2', '1021');
insert into sys_role_menu values (102023, CURRENT_TIMESTAMP(), '2', '1022');
insert into sys_role_menu values (102024, CURRENT_TIMESTAMP(), '2', '1023');
insert into sys_role_menu values (102025, CURRENT_TIMESTAMP(), '2', '1024');
insert into sys_role_menu values (102026, CURRENT_TIMESTAMP(), '2', '1025');
insert into sys_role_menu values (102027, CURRENT_TIMESTAMP(), '2', '1026');
insert into sys_role_menu values (102028, CURRENT_TIMESTAMP(), '2', '1027');
insert into sys_role_menu values (102029, CURRENT_TIMESTAMP(), '2', '1028');
insert into sys_role_menu values (102030, CURRENT_TIMESTAMP(), '2', '1029');
insert into sys_role_menu values (102031, CURRENT_TIMESTAMP(), '2', '1030');
insert into sys_role_menu values (102032, CURRENT_TIMESTAMP(), '2', '1031');
insert into sys_role_menu values (102033, CURRENT_TIMESTAMP(), '2', '1032');
insert into sys_role_menu values (102034, CURRENT_TIMESTAMP(), '2', '1033');
insert into sys_role_menu values (102035, CURRENT_TIMESTAMP(), '2', '1034');
insert into sys_role_menu values (102036, CURRENT_TIMESTAMP(), '2', '1035');
insert into sys_role_menu values (102037, CURRENT_TIMESTAMP(), '2', '1036');
insert into sys_role_menu values (102038, CURRENT_TIMESTAMP(), '2', '1037');
insert into sys_role_menu values (102039, CURRENT_TIMESTAMP(), '2', '1038');
insert into sys_role_menu values (102040, CURRENT_TIMESTAMP(), '2', '1039');
insert into sys_role_menu values (102041, CURRENT_TIMESTAMP(), '2', '1040');
insert into sys_role_menu values (102042, CURRENT_TIMESTAMP(), '2', '1041');
insert into sys_role_menu values (102043, CURRENT_TIMESTAMP(), '2', '1042');
insert into sys_role_menu values (102044, CURRENT_TIMESTAMP(), '2', '1043');
insert into sys_role_menu values (102045, CURRENT_TIMESTAMP(), '2', '1044');
insert into sys_role_menu values (102046, CURRENT_TIMESTAMP(), '2', '1045');
insert into sys_role_menu values (102047, CURRENT_TIMESTAMP(), '2', '1046');
insert into sys_role_menu values (102048, CURRENT_TIMESTAMP(), '2', '1047');
insert into sys_role_menu values (102049, CURRENT_TIMESTAMP(), '2', '1048');
insert into sys_role_menu values (102050, CURRENT_TIMESTAMP(), '2', '1049');
insert into sys_role_menu values (102051, CURRENT_TIMESTAMP(), '2', '1050');
insert into sys_role_menu values (102052, CURRENT_TIMESTAMP(), '2', '1051');
insert into sys_role_menu values (102053, CURRENT_TIMESTAMP(), '2', '1052');
insert into sys_role_menu values (102054, CURRENT_TIMESTAMP(), '2', '1053');
insert into sys_role_menu values (102055, CURRENT_TIMESTAMP(), '2', '1054');
insert into sys_role_menu values (102056, CURRENT_TIMESTAMP(), '2', '1055');
insert into sys_role_menu values (102057, CURRENT_TIMESTAMP(), '2', '1056');
insert into sys_role_menu values (102058, CURRENT_TIMESTAMP(), '2', '1057');
insert into sys_role_menu values (102059, CURRENT_TIMESTAMP(), '2', '1058');
insert into sys_role_menu values (102060, CURRENT_TIMESTAMP(), '2', '1059');
insert into sys_role_menu values (102061, CURRENT_TIMESTAMP(), '2', '1060');




DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
`id` bigint NOT NULL COMMENT '主键',
`notice_title` varchar(512) NOT NULL COMMENT '公告标题',
`notice_content` longblob NOT NULL COMMENT '公告内容',
`notice_type` tinyint NOT NULL COMMENT '公告类型（1通知 2公告）',
`remark` varchar(255)    default null comment '备注',
`status` char(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
`created_by` bigint DEFAULT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`del_flag` int NOT NULL COMMENT '删除标识（1/正常 2/删除）',
`del_by` bigint DEFAULT NULL COMMENT '删除人',
PRIMARY KEY (`id`)
);
insert into sys_notice values(1, '维护通知：2023-04-05 系统凌晨维护','维护内容',  '1',  '备注', '1', null, CURRENT_TIMESTAMP(), null, null, 1, null);



drop table if exists `sys_config`;
create table `sys_config` (
`id` int  not null auto_increment    comment '参数主键',
`name`       varchar(100)    default '' comment '参数名称',
`config_key`varchar(100)   default '' comment '参数键名',
`config_value`  varchar(500)  default '' comment '参数键值',
`config_type`  char(1) NOT NULL comment '系统内置（Y是 N否）',
`remark`  varchar(500)    default null        comment '备注',
`created_by` bigint NULL COMMENT '创建人',
`created_time` datetime NULL COMMENT '创建时间',
`updated_by` bigint NULL COMMENT '更新人',
`updated_time` datetime NULL COMMENT '更新时间',
`del_flag` int NOT NULL COMMENT '删除标识（1/正常 2/删除）',
`del_by` bigint DEFAULT NULL COMMENT '删除人',
primary key (id)
) ;

insert into sys_config values(4, '账号自助-验证码开关','sys.account.captchaEnabled','true', 'Y','是否开启验证码功能（true开启，false关闭）', null,  CURRENT_TIMESTAMP(), null, null, 1, null);


drop table if exists sys_dept;
create table sys_dept (
  id                bigint      not null                   comment '部门id',
  parent_id         bigint      default null               comment '父部门id',
  ancestors         varchar(1024)   default ''                 comment '祖级列表',
  name              varchar(512)     not null                   comment '部门名称',
  order_num         int          not null                   comment '显示顺序',
  leader_id         varchar(20)     default null               comment '负责人',
  status            char(1)         not null                   comment '状态（1正常 2停用）',
  created_by        bigint      not null                   comment '创建人',
  created_time      datetime        not null                   comment '创建时间',
  updated_by        bigint      default null               comment '更新人',
  updated_time      datetime        default null               comment '更新时间',
  del_flag          int          not null                   comment '删除标识（1/正常 2/删除）',
  del_by            bigint      default null               comment '删除人',
primary key (id)
) ;

insert into sys_dept values(100,  0,   '0',          'McDull科技',   0,  null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);
insert into sys_dept values(101,  100, '0,100',      '成都总公司',    1,  null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);
insert into sys_dept values(102,  100, '0,100',      '武汉分公司',    2,  null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);
insert into sys_dept values(103,  101, '0,100,101',  '研发部门',   1, null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);
insert into sys_dept values(104,  101, '0,100,101',  '市场部门',   2, null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);
insert into sys_dept values(105,  101, '0,100,101',  '测试部门',   3, null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);
insert into sys_dept values(106,  101, '0,100,101',  '财务部门',   4, null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);
insert into sys_dept values(107,  101, '0,100,101',  '运维部门',   5, null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);
insert into sys_dept values(108,  102, '0,100,102',  '市场部门',   1, null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);
insert into sys_dept values(109,  102, '0,100,102',  '财务部门',   2, null,  '1', 0, CURRENT_TIMESTAMP(), null, null, 1, null);

drop table if exists sys_dict_type;
create table sys_dict_type
(
    id                bigint      not null auto_increment    comment '字典主键',
    dict_name         varchar(100)    default ''                 comment '字典名称',
    dict_type         varchar(100)    default ''                 comment '字典类型',
    status            char(1)         default '0'                comment '状态（1正常 2停用）',
    created_by        varchar(64)     default null               comment '创建者',
    created_time      datetime        not null                   comment '创建时间',
    updated_by        varchar(64)     default ''                 comment '更新者',
    updated_time      datetime        default null               comment '更新时间',
    remark           varchar(500)     default null               comment '备注',
    del_flag          int          not null                   comment '删除标识（1/正常 2/删除）',
    del_by            bigint      default null               comment '删除人',
    primary key (id),
    unique (dict_type)
) ;

insert into sys_dict_type values(1,  '用户性别', 'sys_user_sex',        '1', null, CURRENT_TIMESTAMP(), '', null, '用户性别列表', 1, null);
insert into sys_dict_type values(2,  '菜单状态', 'sys_show_hide',       '1', null, CURRENT_TIMESTAMP(), '', null, '菜单状态列表', 1, null);
insert into sys_dict_type values(3,  '系统开关', 'sys_normal_disable',  '1', null, CURRENT_TIMESTAMP(), '', null, '系统开关列表', 1, null);
insert into sys_dict_type values(4,  '任务状态', 'sys_job_status',      '1', null, CURRENT_TIMESTAMP(), '', null, '任务状态列表', 1, null);
insert into sys_dict_type values(5,  '任务分组', 'sys_job_group',       '1', null, CURRENT_TIMESTAMP(), '', null, '任务分组列表', 1, null);
insert into sys_dict_type values(6,  '系统是否', 'sys_yes_no',          '1', null, CURRENT_TIMESTAMP(), '', null, '系统是否列表', 1, null);
insert into sys_dict_type values(7,  '通知类型', 'sys_notice_type',     '1', null, CURRENT_TIMESTAMP(), '', null, '通知类型列表', 1, null);
insert into sys_dict_type values(8,  '通知状态', 'sys_notice_status',   '1', null, CURRENT_TIMESTAMP(), '', null, '通知状态列表', 1, null);
insert into sys_dict_type values(9,  '操作类型', 'sys_oper_type',       '1', null, CURRENT_TIMESTAMP(), '', null, '操作类型列表', 1, null);
insert into sys_dict_type values(10, '系统状态', 'sys_common_status',   '1', null, CURRENT_TIMESTAMP(), '', null, '登录状态列表', 1, null);


drop table if exists sys_dict_data;
create table sys_dict_data
(
    id        bigint      not null auto_increment    comment '字典编码',
    dict_sort        int          default 0                  comment '字典排序',
    dict_label       varchar(100)    default ''                 comment '字典标签',
    dict_value       varchar(100)    default ''                 comment '字典键值',
    dict_type        varchar(100)    default ''                 comment '字典类型',
    css_class        varchar(100)    default null               comment '样式属性（其他样式扩展）',
    list_class       varchar(100)    default null               comment '表格回显样式',
    is_default       char(1)         default 'N'                comment '是否默认（Y是 N否）',
    status           char(1)         default null               comment '状态（1正常 2停用）',
    created_by        bigint    default null               comment '创建者',
    created_time      datetime                                  comment '创建时间',
    updated_by        bigint    default null               comment '更新者',
    updated_time      datetime                                  comment '更新时间',
    remark           varchar(500)    default null               comment '备注',
    del_flag          int          not null                   comment '删除标识（1/正常 2/删除）',
    del_by            bigint      default null               comment '删除人',
    primary key (id)
) ;

insert into sys_dict_data values(1,  1,  '男',       '0',       'sys_user_sex',        '',   '',        'Y', '1', null, CURRENT_TIMESTAMP(), null, null, '性别男',    1, null);
insert into sys_dict_data values(2,  2,  '女',       '1',       'sys_user_sex',        '',   '',        'N', '1', null, CURRENT_TIMESTAMP(), null, null, '性别女',    1, null);
insert into sys_dict_data values(3,  3,  '未知',     '2',       'sys_user_sex',        '',   '',        'N', '1', null, CURRENT_TIMESTAMP(), null, null, '性别未知',   1, null);
insert into sys_dict_data values(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '1', null, CURRENT_TIMESTAMP(), null, null, '显示菜单',   1, null);
insert into sys_dict_data values(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '1', null, CURRENT_TIMESTAMP(), null, null, '隐藏菜单',   1, null);
insert into sys_dict_data values(6,  1,  '正常',     '1',       'sys_normal_disable',  '',   'primary', 'Y', '1', null, CURRENT_TIMESTAMP(), null, null, '正常状态',   1, null);
insert into sys_dict_data values(7,  2,  '停用',     '2',       'sys_normal_disable',  '',   'danger',  'N', '1', null, CURRENT_TIMESTAMP(), null, null, '停用状态',   1, null);
insert into sys_dict_data values(8,  1,  '正常',     '0',       'sys_job_status',      '',   'primary', 'Y', '1', null, CURRENT_TIMESTAMP(), null, null, '正常状态',   1, null);
insert into sys_dict_data values(9,  2,  '暂停',     '1',       'sys_job_status',      '',   'danger',  'N', '1', null, CURRENT_TIMESTAMP(), null, null, '停用状态',   1, null);
insert into sys_dict_data values(10, 1,  '默认',     'DEFAULT', 'sys_job_group',       '',   '',        'Y', '1', null, CURRENT_TIMESTAMP(), null, null, '默认分组',   1, null);
insert into sys_dict_data values(11, 2,  '系统',     'SYSTEM',  'sys_job_group',       '',   '',        'N', '1', null, CURRENT_TIMESTAMP(), null, null, '系统分组',   1, null);
insert into sys_dict_data values(12, 1,  '是',       'Y',       'sys_yes_no',          '',   'primary', 'Y', '1', null, CURRENT_TIMESTAMP(), null, null, '系统默认是', 1, null);
insert into sys_dict_data values(13, 2,  '否',       'N',       'sys_yes_no',          '',   'danger',  'N', '1', null, CURRENT_TIMESTAMP(), null, null, '系统默认否', 1, null);
insert into sys_dict_data values(14, 1,  '通知',     '1',       'sys_notice_type',     '',   'warning', 'Y', '1', null, CURRENT_TIMESTAMP(), null, null, '通知',      1, null);
insert into sys_dict_data values(15, 2,  '公告',     '2',       'sys_notice_type',     '',   'success', 'N', '1', null, CURRENT_TIMESTAMP(), null, null, '公告',      1, null);
insert into sys_dict_data values(16, 1,  '正常',     '0',       'sys_notice_status',   '',   'primary', 'Y', '1', null, CURRENT_TIMESTAMP(), null, null, '正常状态',   1, null);
insert into sys_dict_data values(17, 2,  '关闭',     '1',       'sys_notice_status',   '',   'danger',  'N', '1', null, CURRENT_TIMESTAMP(), null, null, '关闭状态',   1, null);
insert into sys_dict_data values(18, 99, '其他',     '0',       'sys_oper_type',       '',   'info',    'N', '1', null, CURRENT_TIMESTAMP(), null, null, '其他操作',   1, null);
insert into sys_dict_data values(19, 1,  '新增',     '1',       'sys_oper_type',       '',   'info',    'N', '1', null, CURRENT_TIMESTAMP(), null, null, '新增操作',   1, null);
insert into sys_dict_data values(20, 2,  '修改',     '2',       'sys_oper_type',       '',   'info',    'N', '1', null, CURRENT_TIMESTAMP(), null, null, '修改操作',   1, null);
insert into sys_dict_data values(21, 3,  '删除',     '3',       'sys_oper_type',       '',   'danger',  'N', '1', null, CURRENT_TIMESTAMP(), null, null, '删除操作',   1, null);
insert into sys_dict_data values(22, 4,  '授权',     '4',       'sys_oper_type',       '',   'primary', 'N', '1', null, CURRENT_TIMESTAMP(), null, null, '授权操作',   1, null);
insert into sys_dict_data values(23, 5,  '导出',     '5',       'sys_oper_type',       '',   'warning', 'N', '1', null, CURRENT_TIMESTAMP(), null, null, '导出操作',   1, null);
insert into sys_dict_data values(24, 6,  '导入',     '6',       'sys_oper_type',       '',   'warning', 'N', '1', null, CURRENT_TIMESTAMP(), null, null, '导入操作',   1, null);
insert into sys_dict_data values(25, 7,  '强退',     '7',       'sys_oper_type',       '',   'danger',  'N', '1', null, CURRENT_TIMESTAMP(), null, null, '强退操作',   1, null);
insert into sys_dict_data values(26, 8,  '生成代码', '8',       'sys_oper_type',       '',   'warning', 'N', '1', null, CURRENT_TIMESTAMP(), null, null, '生成操作',    1, null);
insert into sys_dict_data values(27, 9,  '清空数据', '9',       'sys_oper_type',       '',   'danger',  'N', '1', null, CURRENT_TIMESTAMP(), null, null, '清空操作',    1, null);
insert into sys_dict_data values(28, 1,  '成功',     '1',       'sys_common_status',   '',   'primary', 'N', '1', null, CURRENT_TIMESTAMP(), null, null, '正常状态',   1, null);
insert into sys_dict_data values(29, 2,  '失败',     '2',       'sys_common_status',   '',   'danger',  'N', '1', null, CURRENT_TIMESTAMP(), null, null, '停用状态',   1, null);
