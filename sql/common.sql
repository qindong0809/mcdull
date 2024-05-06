
-- 主表基础字段
drop table if exists `xxx_xxx`;
create table if not exists `xxx_xxx` (
    `id` bigint(20) not null comment '主键',
    `created_by` bigint(20) not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by` bigint(20) default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',

    `inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
    `del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',

-- 0 false，1true
    `bool_flag` tinyint(0) not null default 0 COMMENT '是否为超级管理员: 0 false，1true',
-- remark
    `remark` varchar(200)  NULL DEFAULT NULL COMMENT '备注',
    primary key (`id`)
)  comment='xxxxx';

-- 中间表基础字段, rel 是"relational"（关系型）的缩写
drop table if exists `xxx_xxx_rel`;
create table if not exists `xxx_xxx_rel` (
    `id` bigint(20) not null comment '主键',
    `created_time` datetime not null comment '创建时间',
    `updated_time` datetime default null comment '更新时间',
    `del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
    primary key (`id`)
)  comment='xxxxx';

