
-- 主表基础字段
drop table if exists `xxx_xxx`;
create table if not exists `xxx_xxx` (
    `id` bigint(20) not null comment '主键',
    `created_by` bigint(20) not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by` bigint(20) default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive` int(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
    `del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
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


CREATE TABLE IF NOT EXISTS `sys_menu` (
`id` bigint(20) NOT NULL COMMENT '主键',
`created_by` bigint(20) NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
`parent_id` bigint(20) NOT NULL COMMENT '父级',
`name` varchar(128) NOT NULL COMMENT '名称',
`icon` varchar(128) NOT NULL COMMENT '图标',
`sort` varchar(128) NOT NULL COMMENT '排序',
`res_code` varchar(128) NOT NULL COMMENT '模块code 如sys:user:list',
`path` varchar(128) NOT NULL COMMENT '路由',
`component` varchar(128) NOT NULL COMMENT '组件',
`type` varchar(8) NOT NULL COMMENT '类型(menu/菜单、button/按钮)',
`del_flag` int(1) NOT NULL COMMENT '删除标识（1/正常 2/已删除）',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

CREATE TABLE IF NOT EXISTS `sys_role` (
`id` bigint(20) NOT NULL COMMENT '主键',
`created_by` bigint(20) NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
`name` varchar(512) NOT NULL COMMENT '名称',
`code` varchar(128) DEFAULT NULL COMMENT '编码',
`description` varchar(128) DEFAULT NULL COMMENT '描述',
`del_flag` int(1) NOT NULL COMMENT '删除标识（1/正常 2/已删除）',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `role_id` bigint(20) NOT NULL COMMENT '角色主键',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单主键',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单中间表';





CREATE TABLE IF NOT EXISTS `sys_mail_config` (
`id` bigint(20) NOT NULL COMMENT '主键',
`created_by` bigint(20) NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
`host` varchar(128) NOT NULL COMMENT '主机ip',
`port` varchar(128) NOT NULL COMMENT '主机端口',
`username` varchar(128) NOT NULL COMMENT '账户',
`password` varchar(128) NOT NULL COMMENT '密码',
`from` varchar(128) NOT NULL COMMENT '设置发送方,如：张三<123456@qq.com>',
`del_flag` int(1) NOT NULL COMMENT '删除标识（1/正常 2/删除）',
PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件发送方配置';

INSERT INTO `sys_mail_config` VALUES (1, 1, '2022-01-01 00:00:00', NULL, NULL, 1, 'smtp.163.com', 465, '12345678910@163.com', 'UUSAPIDNYJITQTCE', '麦兜<12345678910@163.com>', 1);

CREATE TABLE `sys_mail_template` (
 `id` bigint unsigned NOT NULL COMMENT '主键',
 `created_by` bigint(20) NOT NULL COMMENT '创建人',
 `created_time` datetime NOT NULL COMMENT '创建时间',
 `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
 `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
 `status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
 `del_flag` int(1) NOT NULL DEFAULT 1 COMMENT '删除标识（1/正常 2/删除）',
 `model_code` varchar(256)  NOT NULL COMMENT '所属模块编码 如：user:register',
 `template_name` varchar(1024)  NOT NULL COMMENT '模板名称',
 `template_type` varchar(256)  NOT NULL COMMENT '模板类型',
 `descr` varchar(2048)  DEFAULT NULL COMMENT '备注说明',
 `subject` text  NOT NULL COMMENT '标题（包含占位符）',
 `content` longtext  NOT NULL COMMENT '正文（包含占位符）',
 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件模板';


CREATE TABLE `sys_study_mail_template` (
`id` bigint unsigned NOT NULL COMMENT '主键',
`created_by` bigint(20) NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
`study_id` bigint(20) NOT NULL COMMENT '项目主键',
`mail_template_id` bigint(20) NOT NULL COMMENT '邮件模板主键',
`module` varchar(256)  NOT NULL COMMENT '所属模块编码',
PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件模板';

CREATE TABLE `sys_mail_placeholder_config` (
   `id` bigint unsigned NOT NULL COMMENT '主键',
   `sys_code` varchar(256)  NOT NULL COMMENT '所属系统编码 如：CTMS',
   `module` varchar(256)  NOT NULL COMMENT '所属模块编码',
   `code` varchar(256)  NOT NULL COMMENT '所属业务编码',
   `placeholder_text` text  NOT NULL COMMENT '占位符的具体值 如：{{@studyId}}',
   `descr` varchar(256)  NOT NULL COMMENT '备注说明',
   `status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件的占位符数据';

CREATE TABLE `sys_mail_template_detail` (
`mail_template_id` bigint unsigned NOT NULL COMMENT '模板主键',
`mail_placeholder_id` bigint unsigned NOT NULL COMMENT '占位符数据主键'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件对应占位符中间表';

--
// 获取占位符集
Result<configId, module, code> getPlaceholder(studyId, module)

// 获取标题正文
Result<subject, content> getMailContent(configId, module, code, values)
--

drop table if exists `sys_file`;
create table `sys_file` (
  `id` bigint(20) not null comment 'id',
  `create_time` datetime not null comment '创建时间',
  `created_by` bigint(20) not null comment '创建人',
  `update_time` datetime not null comment '最后修改时间',
  `updated_by` bigint(20) not null comment '最后修改人',
  `biz_id` bigint(20) not null comment '业务主键',
  `file_type` varchar(255) default null comment '文件类型',
  `bucket` varchar(255) default '' comment '桶',
  `path` varchar(255) default '' comment '文件相对地址',
  `url` varchar(255) default null comment '文件访问地址',
  `unique_file_name` varchar(255) default '' comment '唯一文件名',
  `file_md5` varchar(255) default null comment '文件md5',
  `original_file_name` varchar(255) default '' comment '原始文件名',
  `file_size` bigint(20) default '0' comment '大小',
  primary key (`id`) using btree
) engine=innodb default charset=utf8mb4 comment='文件上传';