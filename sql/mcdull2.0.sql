drop table if exists `sys_user`;
create table if not exists `sys_user` (
    `id` int not null auto_increment comment '主键',
    `nick_name` varchar(256) not null comment '昵称',
    `username` varchar(128) not null comment '账号',
    `password` varchar(128) not null comment '密码',
    `salt` varchar(128) not null comment '密码盐',
    `email` varchar(128) default null comment '邮箱',
    `phone` varchar(128) default null comment '手机号',
    `sex` int(1) not null  comment '性别（0/男 1/女）',
    `remark` varchar(512) default null comment '备注',
    `last_login_time` datetime default null comment '最后一次登录时间',
    `type` int(1) not null  comment '类型（1/自定义 2/内置）',
    `dept_id` int not null comment '部门id',
    `created_by` int not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by` int default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
    `del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
    primary key (`id`),
    key `idx_username` (`username`)
    ) comment='用户表';
insert into sys_user values(1, '麦兜', 'admin', '7a69d7186df1a65ed7af2ba00747488e2415bf1a', 'c7e87439-aef3-48e0-be26-678d0ab99345', 'admin@mcdull.com', '18238352145', 0,  '备注',  null, 2, 100, 0, '2022-10-31 07:20:54', null, null, b'0', b'0');

drop table if exists `sys_role`;
create table if not exists `sys_role` (
    `id` int not null auto_increment comment '主键',
    `name` varchar(512) not null comment '名称',
    `code` varchar(128) default null comment '编码',
    `description` varchar(128) default null comment '描述',
    `type` int(1) not null  comment '类型（1/自定义 2/内置）',
    `created_by` int not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by` int default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
    `del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
    primary key (`id`)
) comment='角色表';

insert into sys_role values(1, '系统管理员', 'admin', '这是系统管理员', 2, 1, sysdate(), null, null, b'0', b'0');
insert into sys_role values(2, '日志管理员', 'log-admin', '这是日志管理员', 1, 1, sysdate(), null, null, b'0', b'0');

drop table if exists `sys_user_role`;
create table if not exists `sys_user_role` (
    `id` int not null auto_increment comment '主键',
    `created_time` datetime not null comment '创建时间',
    `user_id` int not null comment '用户主键',
    `role_id` int not null comment '角色主键',
    primary key (`id`),
    key `idx_user_id` (`user_id`) using btree,
    key `idx_role_id` (`role_id`) using btree
    ) engine=innodb default charset=utf8mb4 comment='用户角色中间表';

insert into sys_user_role values(1, sysdate(), 1, 1);
insert into sys_user_role values(2, sysdate(), 1, 2);

drop table if exists `sys_role_menu`;
create table if not exists `sys_role_menu` (
    `id` int not null auto_increment comment '主键',
    `created_time` datetime not null comment '创建时间',
    `role_id` int not null comment '角色主键',
    `menu_id` int not null comment '菜单id',
    key `idx_role_id` (`role_id`) using btree,
    key `idx_menu_id` (`menu_id`) using btree,
    primary key (`id`)
 ) comment='角色单资源中间表';

begin;
insert into sys_role_menu values (1001,   sysdate(),  '2', '1');
insert into sys_role_menu values (1002,   sysdate(),  '2', '2');
insert into sys_role_menu values (1003,   sysdate(),  '2', '3');
insert into sys_role_menu values (1004,   sysdate(),  '2', '4');

insert into sys_role_menu values (101001, sysdate(), '2', '100');
insert into sys_role_menu values (101002, sysdate(), '2', '101');
insert into sys_role_menu values (101003, sysdate(), '2', '102');
insert into sys_role_menu values (101004, sysdate(), '2', '103');
insert into sys_role_menu values (101005, sysdate(), '2', '104');
insert into sys_role_menu values (101006, sysdate(), '2', '105');
insert into sys_role_menu values (101007, sysdate(), '2', '106');
insert into sys_role_menu values (101008, sysdate(), '2', '107');
insert into sys_role_menu values (101009, sysdate(), '2', '108');
insert into sys_role_menu values (101010, sysdate(), '2', '109');
insert into sys_role_menu values (101011, sysdate(), '2', '110');
insert into sys_role_menu values (101012, sysdate(), '2', '111');
insert into sys_role_menu values (101013, sysdate(), '2', '112');
insert into sys_role_menu values (101014, sysdate(), '2', '113');
insert into sys_role_menu values (101015, sysdate(), '2', '114');
insert into sys_role_menu values (101016, sysdate(), '2', '115');
insert into sys_role_menu values (101017, sysdate(), '2', '116');
insert into sys_role_menu values (101018, sysdate(), '2', '117');
insert into sys_role_menu values (101019, sysdate(), '2', '500');
insert into sys_role_menu values (101020, sysdate(), '2', '501');

insert into sys_role_menu values (102001, sysdate(), '2', '1000');
insert into sys_role_menu values (102002, sysdate(), '2', '1001');
insert into sys_role_menu values (102003, sysdate(), '2', '1002');
insert into sys_role_menu values (102004, sysdate(), '2', '1003');
insert into sys_role_menu values (102005, sysdate(), '2', '1004');
insert into sys_role_menu values (102006, sysdate(), '2', '1005');
insert into sys_role_menu values (102007, sysdate(), '2', '1006');
insert into sys_role_menu values (102008, sysdate(), '2', '1007');
insert into sys_role_menu values (102009, sysdate(), '2', '1008');
insert into sys_role_menu values (102010, sysdate(), '2', '1009');
insert into sys_role_menu values (102011, sysdate(), '2', '1010');
insert into sys_role_menu values (102012, sysdate(), '2', '1011');
insert into sys_role_menu values (102013, sysdate(), '2', '1012');
insert into sys_role_menu values (102014, sysdate(), '2', '1013');
insert into sys_role_menu values (102015, sysdate(), '2', '1014');
insert into sys_role_menu values (102016, sysdate(), '2', '1015');
insert into sys_role_menu values (102017, sysdate(), '2', '1016');
insert into sys_role_menu values (102018, sysdate(), '2', '1017');
insert into sys_role_menu values (102019, sysdate(), '2', '1018');
insert into sys_role_menu values (102020, sysdate(), '2', '1019');
insert into sys_role_menu values (102021, sysdate(), '2', '1020');
insert into sys_role_menu values (102022, sysdate(), '2', '1021');
insert into sys_role_menu values (102023, sysdate(), '2', '1022');
insert into sys_role_menu values (102024, sysdate(), '2', '1023');
insert into sys_role_menu values (102025, sysdate(), '2', '1024');
insert into sys_role_menu values (102026, sysdate(), '2', '1025');
insert into sys_role_menu values (102027, sysdate(), '2', '1026');
insert into sys_role_menu values (102028, sysdate(), '2', '1027');
insert into sys_role_menu values (102029, sysdate(), '2', '1028');
insert into sys_role_menu values (102030, sysdate(), '2', '1029');
insert into sys_role_menu values (102031, sysdate(), '2', '1030');
insert into sys_role_menu values (102032, sysdate(), '2', '1031');
insert into sys_role_menu values (102033, sysdate(), '2', '1032');
insert into sys_role_menu values (102034, sysdate(), '2', '1033');
insert into sys_role_menu values (102035, sysdate(), '2', '1034');
insert into sys_role_menu values (102036, sysdate(), '2', '1035');
insert into sys_role_menu values (102037, sysdate(), '2', '1036');
insert into sys_role_menu values (102038, sysdate(), '2', '1037');
insert into sys_role_menu values (102039, sysdate(), '2', '1038');
insert into sys_role_menu values (102040, sysdate(), '2', '1039');
insert into sys_role_menu values (102041, sysdate(), '2', '1040');
insert into sys_role_menu values (102042, sysdate(), '2', '1041');
insert into sys_role_menu values (102043, sysdate(), '2', '1042');
insert into sys_role_menu values (102044, sysdate(), '2', '1043');
insert into sys_role_menu values (102045, sysdate(), '2', '1044');
insert into sys_role_menu values (102046, sysdate(), '2', '1045');
insert into sys_role_menu values (102047, sysdate(), '2', '1046');
insert into sys_role_menu values (102048, sysdate(), '2', '1047');
insert into sys_role_menu values (102049, sysdate(), '2', '1048');
insert into sys_role_menu values (102050, sysdate(), '2', '1049');
insert into sys_role_menu values (102051, sysdate(), '2', '1050');
insert into sys_role_menu values (102052, sysdate(), '2', '1051');
insert into sys_role_menu values (102053, sysdate(), '2', '1052');
insert into sys_role_menu values (102054, sysdate(), '2', '1053');
insert into sys_role_menu values (102055, sysdate(), '2', '1054');
insert into sys_role_menu values (102056, sysdate(), '2', '1055');
insert into sys_role_menu values (102057, sysdate(), '2', '1056');
insert into sys_role_menu values (102058, sysdate(), '2', '1057');
insert into sys_role_menu values (102059, sysdate(), '2', '1058');
insert into sys_role_menu values (102060, sysdate(), '2', '1059');
insert into sys_role_menu values (102061, sysdate(), '2', '1060');

commit;

drop table if exists `sys_menu`;
create table if not exists `sys_menu` (
`id` int not null auto_increment comment '主键',
`menu_type` int(1) not null comment '0代表菜单、1代表iframe、2代表外链、3代表按钮',
`parent_id` int not null comment '父菜单id',
`title` varchar(128) not null comment '菜单名称',
`name` varchar(128) not null comment '路由名称',
`path` varchar(128) default null comment '路由路径',
`component` varchar(256) default null comment '组件路径（传`component`组件路径，那么`path`可以随便写，如果不传，`component`组件路径会跟`path`保持一致',
`rank_order` int(16) not null comment '菜单排序',
`redirect` varchar(256) default null comment '路由重定向',
`icon` varchar(128) default null comment '菜单图标',
`extra_icon` varchar(128) default null comment '右侧图标',
`enter_transition` varchar(128) default null comment '进场动画',
`leave_transition` varchar(128) default null comment '离场动画',
`active_path` varchar(128) default null comment '菜单激活',
`auths` varchar(128) default null comment '权限标识（按钮级别权限设置）',
`frame_src` varchar(128) default null comment '链接地址',
`frame_loading` int(1) not null default b'0' comment '加载动画',
`keep_alive` int(1) not null default b'0' comment '缓存页面',
`hidden_tag` int(1) not null default b'0' comment '标签页（当前菜单名称或自定义信息禁止添加到标签页）',
`show_link` int(1) not null default b'0' comment '菜单（是否显示该菜单）',
`show_parent` int(1) not null default b'0' comment '父级菜单（是否显示父级菜单',
`created_by` int null comment '创建人',
`created_time` datetime null comment '创建时间',
`updated_by` int null comment '更新人',
`updated_time` datetime null comment '更新时间',
`inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
`del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)comment='菜单表';
insert into sys_menu values(1,    0, 0,  'menus.hssysManagement', 'System',       '/system',             null, 1, null,  'ri:settings-3-line', '', '',  '', '', '', '', b'0',  b'0', b'0', b'0', b'0',  null, sysdate(), null, null, b'0', b'0');
insert into sys_menu values(1004, 0, 1,  'menus.hsDept',          'SystemDept',  '/system/dept/index',   null, 2, null,  'ri:git-branch-line', '', '',  '', '', '', '', b'0',  b'0', b'0', b'0', b'0',  null, sysdate(), null, null, b'0', b'0');
insert into sys_menu values(1001, 0, 1,  'menus.hsRole',          'SystemRole',  '/system/role/index',   null, 1, null,  'ri:admin-fill',      '', '',  '', '', '', '', b'0',  b'0', b'0', b'0', b'0',  null, sysdate(), null, null, b'0', b'0');
insert into sys_menu values(1003, 0, 1,  'menus.hsUser',          'SystemUser',  '/system/user/index',   null, 3, null,  'ri:admin-line',      '', '',  '', '', '', '', b'0',  b'0', b'0', b'0', b'0',  null, sysdate(), null, null, b'0', b'0');
insert into sys_menu values(1002, 0, 1,  'menus.hsSystemMenu',    'SystemMenu',  '/system/menu/index',   null, 4, null,  'ep:menu',            '', '',  '', '', '', '', b'0',  b'0', b'0', b'0', b'0',  null, sysdate(), null, null, b'0', b'0');



drop table if exists `sys_dept`;
create table if not exists `sys_dept` (
`id` int not null auto_increment comment '主键',
`parent_id` int not null comment '父菜单id',
`name` varchar(128) not null comment '路由名称',
`sort` int not null comment '排序',
`phone` varchar(128) default null comment 'phone',
`principal` varchar(128) default null comment '负责人',
`email` varchar(128) default null comment 'email',
`type` int not null comment '1/公司 2 分公司 3 部门',
`remark` text default null comment '备注',
`created_by` int null comment '创建人',
`created_time` datetime null comment '创建时间',
`updated_by` int null comment '更新人',
`updated_time` datetime null comment '更新时间',
`inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
`del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
    )comment='部门表';


drop table if exists `sys_email_template`;
create table if not exists `sys_email_template` (
`id` int not null auto_increment comment '主键',
`code` varchar(128) not null comment '编码',
`name` varchar(128) not null comment '名称',
`title` varchar(128) not null comment '标题',
`content` varchar(4096) not null comment '内容',
`remark` text default null comment '备注',
`created_by` int null comment '创建人',
`created_time` datetime null comment '创建时间',
`updated_by` int null comment '更新人',
`updated_time` datetime null comment '更新时间',
`inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
`del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)comment='邮件模版';

INSERT INTO sys_email_template VALUES('CREATE_USER_EMAIL','创建用户email通知','欢迎使用','访问地址： http://localhost:8848',NULL,0,NULL,NULL,NULL,0,0);


drop table if exists `sys_email_send_history`;
create table if not exists `sys_email_send_history` (
`id` int not null auto_increment comment '主键',
`sent_to` varchar(128) not null comment '收件地址',
`cc` varchar(128) not null comment '抄送地址',
`title` varchar(1024) not null comment '标题',
`content` varchar(4096) not null comment '内容',
`file_id_array` varchar(128) default null comment '附件集',
`created_time` datetime null comment '创建时间',
`updated_time` datetime null comment '更新时间',
`del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)comment='邮件发送历史';


drop table if exists `sys_custom_property`;
create table if not exists `sys_custom_property` (
`id` int not null auto_increment comment '主键',
`code` varchar(128) not null comment '编码key',
`name` varchar(128) not null comment '名称',
`property_value` varchar(4096) not null comment '值',
`remark` text default null comment '备注',
`created_by` int null comment '创建人',
`created_time` datetime null comment '创建时间',
`updated_by` int null comment '更新人',
`updated_time` datetime null comment '更新时间',
`inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
`del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)comment='自定义属性';


drop table if exists sys_dict_type;
create table sys_dict_type
(
    id                bigint(20)      not null auto_increment    comment '字典主键',
    dict_name         varchar(100)    default ''                 comment '字典名称',
    dict_type         varchar(100)    default ''                 comment '字典类型',
    `remark` text default null comment '备注',
    `created_by` int null comment '创建人',
    `created_time` datetime null comment '创建时间',
    `updated_by` int null comment '更新人',
    `updated_time` datetime null comment '更新时间',
    `inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
    `del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
    primary key (id),
    unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

drop table if exists sys_dict_data;
create table sys_dict_data
(
    id               int             not null auto_increment    comment '字典编码',
    dict_sort        int(4)          default 0                  comment '字典排序',
    dict_label       varchar(100)    default ''                 comment '字典标签',
    dict_value       varchar(100)    default ''                 comment '字典键值',
    dict_type        varchar(100)    default ''                 comment '字典类型',
    remark           text            default null               comment '备注',
    created_by       int             null                       comment '创建人',
    created_time     datetime        null                       comment '创建时间',
    updated_by       int             null                       comment '更新人',
    updated_time     datetime        null                       comment '更新时间',
    inactive         int(1)          not null default b'0'      comment '状态（true/已失活 false/未失活）',
    del_flag         bit(1)          not null default b'0'      comment '删除标识（true/已删除 false/未删除）',
    primary key (id)
)  comment = '字典数据表';


--
-- drop table if exists `sys_menu`;
-- create table if not exists `sys_menu` (
--     `id` int not null auto_increment comment '主键',
--     `name` varchar(128) not null comment '名称',
--     `parent_id` varchar(128) not null comment '父菜单id',
--     `order_num` int(16) not null comment '显示顺序',
--     `path` varchar(128) default null comment '路由地址',
--     `component` varchar(256) default null comment '组件路径',
--     `query` varchar(256) default null comment '路由参数',
--     `is_frame` int(1) not null comment '是否为外链（0是 1否）',
--     `is_cache` int(1) not null comment '是否缓存（0缓存 1不缓存）',
--     `menu_type` varchar(8) not null comment '菜单类型（m目录 c菜单 f按钮）',
--     `visible` char(1) not null comment '菜单状态（0显示 1隐藏）',
--     `status` char(1)  not null comment '状态（0正常 1停用）',
--     `perms` varchar(128) default null comment '权限标识',
--     `icon` varchar(128) default null comment '菜单图标',
--     `created_by` int null comment '创建人',
--     `created_time` datetime null comment '创建时间',
--     `updated_by` int null comment '更新人',
--     `updated_time` datetime null comment '更新时间',
--     `remark` varchar(500) default null comment '备注',
--     `inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
--     `del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
--     primary key (`id`)
--     )comment='菜单表';
-- BEGIN;
-- -- ----------------------------
-- -- 初始化-菜单信息表数据
-- -- ----------------------------
-- -- 一级菜单
-- insert into sys_menu values('1', '系统管理', '0', '1', 'system',           null, 'system', 1, 0, 'M', '0', '0', '', 'system',   null, sysdate(), null, null, '系统管理目录', b'0', b'0');
-- insert into sys_menu values('2', '系统监控', '0', '2', 'monitor',          null, 'monitor', 1, 0, 'M', '0', '0', '', 'monitor',  null, sysdate(), null, null, '系统监控目录', b'0', b'0');
-- insert into sys_menu values('3', '系统工具', '0', '3', 'tool',             null, 'tool', 1, 0, 'M', '0', '0', '', 'tool',     null, sysdate(), null, null, '系统工具目录', b'0', b'0');
-- insert into sys_menu values('4', '若依官网', '0', '4', 'http://ruoyi.vip', null, 'guide', 0, 0, 'M', '0', '0', '', 'guide',    null, sysdate(), null, null, '若依官网地址', b'0', b'0');
-- insert into sys_menu values('6', '数据库管理', '0', '6', 'database',        null, '', 1, 0, 'M', '0', '0', 'database', 'example',   null, sysdate(), null, null, '数据库管理目录', b'0', b'0');
--
-- -- 二级菜单
-- insert into sys_menu values('600',  '实例管理', '6',   '6', 'instance',   'database/instance/index',  '', 1, 0, 'C', '0', '0', 'database:instance:list',  'list',      null, sysdate(), null, null, '实例管理菜单', b'0', b'0');
-- insert into sys_menu values('601',  '组名管理', '6',   '6', 'group',      'database/group/index',     '', 1, 0, 'C', '0', '0', 'database:group:list',     'dashboard',      null, sysdate(), null, null, '组名管理菜单', b'0', b'0');
-- insert into sys_menu values('603',  '版本管理', '6',   '6', 'git',      'database/git/index',     '', 1, 0, 'C', '0', '0', 'database:git:list',           'swagger',           null, sysdate(), null, null, '版本管理菜单', b'0', b'0');
-- insert into sys_menu values('604',  '工单管理', '6',   '6', 'ticket',      'database/ticket/index',     '', 1, 0, 'C', '0', '0', 'database:ticket:list',  'skill',        null, sysdate(), null, null, '工单管理菜单', b'0', b'0');
-- insert into sys_menu values('605',  '环境配置', '6',   '6', 'env',      'database/env/index',     '', 1, 0, 'C', '0', '0', 'database:env:list',  'color',        null, sysdate(), null, null, '环境配置菜单', b'0', b'0');
--
-- insert into sys_menu values('100',  '用户管理', '1',   '1', 'user',       'system/user/index',        '', 1, 0, 'C', '0', '0', 'system:user:list',        'user',          null, sysdate(), null, null, '用户管理菜单', b'0', b'0');
-- insert into sys_menu values('101',  '角色管理', '1',   '2', 'role',       'system/role/index',        '', 1, 0, 'C', '0', '0', 'system:role:list',        'peoples',       null, sysdate(), null, null, '角色管理菜单', b'0', b'0');
-- insert into sys_menu values('102',  '菜单管理', '1',   '3', 'menu',       'system/menu/index',        '', 1, 0, 'C', '0', '0', 'system:menu:list',        'tree-table',    null, sysdate(), null, null, '菜单管理菜单', b'0', b'0');
-- insert into sys_menu values('103',  '部门管理', '1',   '4', 'dept',       'system/dept/index',        '', 1, 0, 'C', '0', '0', 'system:dept:list',        'tree',          null, sysdate(), null, null, '部门管理菜单', b'0', b'0');
-- insert into sys_menu values('104',  '岗位管理', '1',   '5', 'post',       'system/post/index',        '', 1, 0, 'C', '0', '0', 'system:post:list',        'post',          null, sysdate(), null, null, '岗位管理菜单', b'0', b'0');
-- insert into sys_menu values('105',  '字典管理', '1',   '6', 'dict',       'system/dict/index',        '', 1, 0, 'C', '0', '0', 'system:dict:list',        'dict',          null, sysdate(), null, null, '字典管理菜单', b'0', b'0');
-- insert into sys_menu values('106',  '参数设置', '1',   '7', 'config',     'system/config/index',      '', 1, 0, 'C', '0', '0', 'system:config:list',      'edit',          null, sysdate(), null, null, '参数设置菜单', b'0', b'0');
-- insert into sys_menu values('107',  '通知公告', '1',   '8', 'notice',     'system/notice/index',      '', 1, 0, 'C', '0', '0', 'system:notice:list',      'message',       null, sysdate(), null, null, '通知公告菜单', b'0', b'0');
-- insert into sys_menu values('108',  '日志管理', '1',   '9', 'log',        '',                         '', 1, 0, 'M', '0', '0', '',                        'log',           null, sysdate(), null, null, '日志管理菜单', b'0', b'0');
-- insert into sys_menu values('109',  '在线用户', '2',   '1', 'online',     'monitor/online/index',     '', 1, 0, 'C', '0', '0', 'monitor:online:list',     'online',        null, sysdate(), null, null, '在线用户菜单', b'0', b'0');
-- insert into sys_menu values('110',  '定时任务', '2',   '2', 'job',        'monitor/job/index',        '', 1, 0, 'C', '0', '0', 'monitor:job:list',        'job',           null, sysdate(), null, null, '定时任务菜单', b'0', b'0');
-- insert into sys_menu values('111',  '数据监控', '2',   '3', 'druid',      'monitor/druid/index',      '', 1, 0, 'C', '0', '0', 'monitor:druid:list',      'druid',         null, sysdate(), null, null, '数据监控菜单', b'0', b'0');
-- insert into sys_menu values('112',  '服务监控', '2',   '4', 'server',     'monitor/server/index',     '', 1, 0, 'C', '0', '0', 'monitor:server:list',     'server',        null, sysdate(), null, null, '服务监控菜单', b'0', b'0');
-- insert into sys_menu values('113',  '缓存监控', '2',   '5', 'cache',      'monitor/cache/index',      '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis',         null, sysdate(), null, null, '缓存监控菜单', b'0', b'0');
-- insert into sys_menu values('114',  '缓存列表', '2',   '6', 'cacheList',  'monitor/cache/list',       '', 1, 0, 'C', '0', '0', 'monitor:cache:redis-list',      'redis-list',    null, sysdate(), null, null, '缓存列表菜单', b'0', b'0');
-- insert into sys_menu values('115',  '表单构建', '3',   '1', 'build',      'tool/build/index',         '', 1, 0, 'C', '0', '0', 'tool:build:list',         'build',         null, sysdate(), null, null, '表单构建菜单', b'0', b'0');
-- insert into sys_menu values('116',  '代码生成', '3',   '2', 'gen',        'tool/gen/index',           '', 1, 0, 'C', '0', '0', 'tool:gen:list',           'code',          null, sysdate(), null, null, '代码生成菜单', b'0', b'0');
-- insert into sys_menu values('117',  '系统接口', '3',   '3', 'swagger',    'tool/swagger/index',       '', 1, 0, 'C', '0', '0', 'tool:swagger:list',       'swagger',       null, sysdate(), null, null, '系统接口菜单', b'0', b'0');
-- -- 三级菜单, b'0', b'0'
-- insert into sys_menu values('500',  '操作日志', '108', '1', 'operlog',    'monitor/operlog/index',    '', 1, 0, 'C', '0', '0', 'monitor:operlog:list',    'form',          null, sysdate(), null, null, '操作日志菜单', b'0', b'0');
-- insert into sys_menu values('501',  '登录日志', '108', '2', 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor',    null, sysdate(), null, null, '登录日志菜单', b'0', b'0');
-- -- 用户管理按钮
-- insert into sys_menu values('1000', '查询', '100', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:query',          '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1001', '新增', '100', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:add',            '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1002', '修改', '100', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit',           '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1003', '删除', '100', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1004', '导出', '100', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:export',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1005', '导入', '100', '6',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:import',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1006', '重置密码', '100', '7',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd',       '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 角色管理按钮
-- insert into sys_menu values('1007', '查询', '101', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:query',          '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1008', '新增', '101', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:add',            '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1009', '修改', '101', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit',           '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1010', '删除', '101', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1011', '导出', '101', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:export',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 菜单管理按钮, b'0', b'0'
-- insert into sys_menu values('1012', '查询', '102', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query',          '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1013', '新增', '102', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add',            '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1014', '修改', '102', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit',           '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1015', '删除', '102', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 部门管理按钮, b'0', b'0'
-- insert into sys_menu values('1016', '查询', '103', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query',          '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1017', '新增', '103', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add',            '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1018', '修改', '103', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit',           '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1019', '删除', '103', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 岗位管理按钮, b'0', b'0'
-- insert into sys_menu values('1020', '查询', '104', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:query',          '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1021', '新增', '104', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:add',            '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1022', '修改', '104', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit',           '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1023', '删除', '104', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1024', '导出', '104', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:export',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 字典管理按钮, b'0', b'0'
-- insert into sys_menu values('1025', '查询', '105', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query',          '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1026', '新增', '105', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add',            '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1027', '修改', '105', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit',           '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1028', '删除', '105', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1029', '导出', '105', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 参数设置按钮, b'0', b'0'
-- insert into sys_menu values('1030', '查询', '106', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query',        '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1031', '新增', '106', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add',          '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1032', '修改', '106', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1033', '删除', '106', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove',       '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1034', '导出', '106', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export',       '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 通知公告按钮, b'0', b'0'
-- insert into sys_menu values('1035', '查询', '107', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query',        '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1036', '新增', '107', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add',          '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1037', '修改', '107', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1038', '删除', '107', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove',       '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 操作日志按钮
-- insert into sys_menu values('1039', '操作查询', '500', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query',      '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1040', '操作删除', '500', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove',     '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1041', '日志导出', '500', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export',     '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 登录日志按钮, b'0', b'0'
-- insert into sys_menu values('1042', '登录查询', '501', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query',   '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1043', '登录删除', '501', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove',  '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1044', '日志导出', '501', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export',  '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1045', '账户解锁', '501', '4', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock',  '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 在线用户按钮, b'0', b'0'
-- insert into sys_menu values('1046', '在线查询', '109', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query',       '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1047', '批量强退', '109', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1048', '单条强退', '109', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 定时任务按钮, b'0', b'0'
-- insert into sys_menu values('1049', '任务查询', '110', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query',          '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1050', '任务新增', '110', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add',            '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1051', '任务修改', '110', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit',           '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1052', '任务删除', '110', '4', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1053', '状态修改', '110', '5', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus',   '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1054', '任务导出', '110', '6', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export',         '#', null, sysdate(), null, null, '', b'0', b'0');
-- -- 代码生成按钮, b'0', b'0'
-- insert into sys_menu values('1055', '生成查询', '116', '1', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',             '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1056', '生成修改', '116', '2', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',              '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1057', '生成删除', '116', '3', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',            '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1058', '导入代码', '116', '4', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',            '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1059', '预览代码', '116', '5', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',           '#', null, sysdate(), null, null, '', b'0', b'0');
-- insert into sys_menu values('1060', '生成代码', '116', '6', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',              '#', null, sysdate(), null, null, '', b'0', b'0');

COMMIT;
