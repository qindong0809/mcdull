DROP TABLE IF EXISTS `demo`;
CREATE TABLE IF NOT EXISTS `demo` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
  `del_flag` int(1) NOT NULL DEFAULT 1 COMMENT '删除标识（1/正常 2/删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='这是demo表';

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE IF NOT EXISTS `sys_user` (
`id` bigint(20) NOT NULL COMMENT '主键',
`created_by` bigint(20) NOT NULL COMMENT '创建人',
`created_time` datetime NOT NULL COMMENT '创建时间',
`updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
`updated_time` datetime DEFAULT NULL COMMENT '更新时间',
`status` int(4) NOT NULL COMMENT '状态（1/正常 2/停用）',
`del_flag` int(1) NOT NULL DEFAULT 1 COMMENT '删除标识（1/正常 2/删除）',
`nickname` varchar(256) NOT NULL COMMENT '昵称',
`account` varchar(128) NOT NULL COMMENT '账号',
`password` varchar(128) NOT NULL COMMENT '密码',
`salt` varchar(128) NOT NULL COMMENT '密码盐',
`email` varchar(128) DEFAULT NULL COMMENT '邮箱',
`phone` varchar(128) DEFAULT NULL COMMENT '手机号',
`last_login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
PRIMARY KEY (`id`),
KEY `idx_pa_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
INSERT INTO sys_user(id, created_by, created_time, updated_by, updated_time, status, del_flag, nickname, account, password, salt, email, phone, last_login_time)
VALUES(1589631293412503554, 1589631293412503554, '2022-10-31 07:20:54', null, null, 1, 1, '麦兜', 'admin', '7a69d7186df1a65ed7af2ba00747488e2415bf1a', 'c7e87439-aef3-48e0-be26-678d0ab99345', 'admin@mcdull.com', '18238352145', null);

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

CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `created_time` datetime NOT NULL COMMENT '创建时间',
    `user_id` bigint(20) NOT NULL COMMENT '角色主键',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单主键',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色中间表';

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE IF NOT EXISTS `sys_dict` (
    `code` varchar(32) NOT NULL COMMENT '编码',
    `parent_code` varchar(32) DEFAULT NULL COMMENT '父级',
    `name` varchar(256) NOT NULL COMMENT '名称',
    `name_short` varchar(32) DEFAULT NULL COMMENT '简码',
    `select_type` varchar(32) NOT NULL COMMENT '类别',
    `sort` int(4) NOT NULL COMMENT '排序',
    `defaulted` int(1) NOT NULL COMMENT '当前是否为默认（1/是 2/否）',
    `status` int(1) NOT NULL COMMENT '状态（1/正常 2/停用）',
    `remark` varchar(128) DEFAULT NULL COMMENT '备注',
    `language` varchar(32) DEFAULT NULL,
    `del_flag` int(1) NOT NULL DEFAULT '1' COMMENT '1-未删除,2-删除'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='公共码表';

INSERT INTO `sys_dict` VALUES ('0', null, '未知的性别', null, 'sex_type', 1, 2, 1, '性别代码',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('1', null, '男', null, 'sex_type', 2, 2, 1, '性别代码',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '女', null, 'sex_type', 3, 2, 1, '性别代码',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('9', null, '未说明的性别', null, 'sex_type', 4, 1, 1, '性别代码',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('1', null, '启用', null, 'status_type', 1, 1, 1, '状态标识',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '停用', null, 'status_type', 2, 2, 1, '状态标识',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('1', null, '正常', null, 'del_flag_type', 1, 1, 1, '删除标识',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('2', null, '删除', null, 'del_flag_type', 2, 2, 1, '删除标识',  'zh-CN',  1);

INSERT INTO `sys_dict` VALUES ('zh-CN', null, '中文', null, 'language_type', 1, 1, 1, '语言类型',  'zh-CN',  1);
INSERT INTO `sys_dict` VALUES ('en-US', null, '英文', null, 'language_type', 2, 2, 1, '语言类型',  'zh-CN',  1);


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

DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` bigint(20) NOT NULL COMMENT '最后修改人',
  `biz_id` bigint(20) NOT NULL COMMENT '业务主键',
  `file_type` varchar(255) DEFAULT NULL COMMENT '文件类型',
  `bucket` varchar(255) DEFAULT '' COMMENT '桶',
  `path` varchar(255) DEFAULT '' COMMENT '文件相对地址',
  `url` varchar(255) DEFAULT NULL COMMENT '文件访问地址',
  `unique_file_name` varchar(255) DEFAULT '' COMMENT '唯一文件名',
  `file_md5` varchar(255) DEFAULT NULL COMMENT '文件md5',
  `original_file_name` varchar(255) DEFAULT '' COMMENT '原始文件名',
  `file_size` bigint(20) DEFAULT '0' COMMENT '大小',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件上传';