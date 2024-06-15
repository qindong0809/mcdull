
create table if not exists `blaze_customer_info` (
`id` int auto_increment primary key,
`name` varchar(255) not null comment '企业名称',
`provinces_code` varchar(64) not null comment '所在地省代码',
`city_code` varchar(64) not null comment '所在市代码',
`contact_person` varchar(255) not null comment '联系人',
`phone_number` varchar(20) not null comment '联系电话',
`customer_type` varchar(100) not null comment '客户类型',
`remark` varchar(200)  null default null comment '备注',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
) comment='客户信息维护表';