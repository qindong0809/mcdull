drop table if exists `app_user`;
create table if not exists `app_user` (
`id` int not null auto_increment comment '主键',
`login_name` varchar(512) NOT NULL,
`login_pwd` varchar(64)  not null comment '登录密码',
`email` varchar(64)  not null comment '邮箱',
`gender` varchar(16)  default null comment '性别',
`stage` varchar(16)  default null comment '学生 / 职场新人 / 职场老手 / 自由职业 / 其他',
`theme` int  not null default 0 comment '主题',
`vip` tinyint(0) not null default 0 comment '是否为vip',
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

drop table if exists `app_focus`;
create table if not exists `app_focus` (
`id` int not null auto_increment comment '主键',
`duration_minutes` int default null comment '专注时长（分钟）',
`mode` tinyint(1)   NOT NULL  comment '模式 0=正计时 1=番茄钟 2=倒计时',
`task_id` int default null comment '关联任务ID，可为空',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
  );


drop table if exists `app_habit`;
create table if not exists `app_habit` (
`id` int not null auto_increment comment '主键',
`name` varchar(64)   NOT NULL comment '习惯名称',
`reminder_time` varchar(16)  NOT NULL comment '提醒时间描述',
`preset` tinyint(0) not null default 0 comment '是否预设习惯',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
);

drop table if exists `app_habit_record`;
create table if not exists `app_habit_record` (
`id` int not null auto_increment comment '主键',
`habit_id` int not null comment '习惯ID',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
  );


drop table if exists `app_schedule`;
create table if not exists `app_schedule` (
`id` int not null auto_increment comment '主键',
`title` varchar(512) NOT NULL,
`start_time` datetime NOT NULL,
`end_time` datetime NOT NULL,
`location` varchar(512) default null,
`note` varchar(1024) default null,
`priority` tinyint(1)   NOT NULL  comment '优先级：0=重要且紧急，1=重要不紧急，2=紧急不重要，3=不重要不紧急',
`repeat_type` tinyint(1)   NOT NULL  comment '重复类型：0=不重复，1=每天，2=每周，3=每月',
`remind_minutes` tinyint(1)   NOT NULL  comment '提前提醒分钟数：0=不提醒，5/15/30',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
  primary key (`id`)
  );
