
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

create table `production_schedule` (
`id` int auto_increment primary key,
`customer` varchar(255) not null comment '客户',
`contact_info` varchar(255) not null comment '联系方式',
`address` varchar(255) not null comment '地址',
`product_name` varchar(255) not null comment '产品名称',
`measurement_date` datetime default null comment '测量日期',
`measurement_by` int default null comment '测量人',
`drawing_date` datetime default null comment '制图完成日期',
`drawing_by` int default null comment '制图人',
`customer_confirmation_date` datetime default null comment '客户确认日期',
`order_breaking_date` datetime default null comment '拆单日期',
`order_breaking_by` int default null comment '拆单人',
`production_order_date` datetime default null comment '下生产单日期',
`process_date` datetime default null comment '工序日期',
`process_description` varchar(64) default null comment '工序描述',
`delivery_date` datetime default null comment '出货日期',
`delivery_by` int default null comment '出货人',
`installation_date` datetime default null comment '安装日期',
`installation_by` int default null comment '安装人',
`production_cycle_remaining` int default null comment '生产周期倒计时',
`remark` varchar(200)  null default null comment '备注',
`owner` int not null comment '负责人',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）'
) comment='生产进度表';

INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, sort, `path`, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, inactive, del_flag, created_by, created_time, updated_by, updated_time) VALUES(255, '生产进度管理', 2, 0, 1, '/business/erp/catalog', '/business/erp/catalog/production-schedule-list.vue', 1, NULL, NULL, 'BarsOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-08-23 11:58:15', 1, '2024-08-23 12:00:18');