drop table if exists `app_user`;
create table if not exists `app_user` (
`id` int not null auto_increment comment '主键',
`login_name` varchar(512) NOT NULL,
`login_pwd` varchar(64)  not null comment '登录密码',
`email` varchar(64)  not null comment '邮箱',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)  comment='app 用户';

drop table if exists `app_task`;
create table if not exists `app_task` (
`id` int not null auto_increment comment '主键',
`name` varchar(512) NOT NULL,
`description` varchar(1024) NOT NULL,
  `due_date` datetime default null comment '到期时间',
  `priority` tinyint(1)   NOT NULL  comment '优先级',
  `category` varchar(64)   NOT NULL comment '状态',
  `completed` tinyint(0) not NULL comment '是否完成',
  `reminder_enabled` tinyint(0) not NULL comment '是否完成',
  `created_by` int not null comment '创建人',
  `created_time` datetime not null comment '创建时间',
  `updated_by` int default null comment '更新人',
  `updated_time` datetime default null comment '更新时间',
  `inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
  `del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
  primary key (`id`)
  ) ;
