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

insert into `sys_user` values(1, 'admin', '123456', 'Terry', 0, '13800000000', 1, 1, '超级管理员', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_user` values(2, 'user', '123456', 'mcdull', 0, '13800000000', 1, 0, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);
insert into `sys_user` values(3, 'user1', '123456', 'mcdull1', 0, '13800000000', 1, 0, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);

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
`created_by` bigint(20) not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` bigint(20) default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
)comment = '菜单表';

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
`employee_id` bigint(0) not null comment '员工id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree,
unique index `uk_role_employee`(`role_id`, `employee_id`) using btree
) comment = '角色用户';