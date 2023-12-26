drop table if exists `sys_user`;
create table if not exists `sys_user` (
    `id` bigint(20) not null comment '主键',
    `nick_name` varchar(256) not null comment '昵称',
    `username` varchar(128) not null comment '账号',
    `password` varchar(128) not null comment '密码',
    `salt` varchar(128) not null comment '密码盐',
    `email` varchar(128) default null comment '邮箱',
    `phone` varchar(128) default null comment '手机号',
    `last_login_time` datetime default null comment '最后一次登录时间',
    `type` int(1) not null  comment '类型（1/自定义 2/内置）',
    `dept_id` bigint(20) not null comment '部门id',
    `created_by` bigint(20) not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by` bigint(20) default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
    `del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
    primary key (`id`),
    key `idx_username` (`username`)
    ) comment='用户表';
insert into sys_user values(1, '麦兜', 'admin', '7a69d7186df1a65ed7af2ba00747488e2415bf1a', 'c7e87439-aef3-48e0-be26-678d0ab99345', 'admin@mcdull.com', '18238352145', null, 2, 100,1589631293412503554, '2022-10-31 07:20:54', null, null, b'0', b'0');
