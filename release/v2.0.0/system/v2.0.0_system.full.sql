-- ============================================================
-- v2.0.0 全量脚本 - system
-- 当前 schema 快照：每张表唯一 CREATE TABLE，后续变更直接改对应表定义
-- 手工维护文件；修改后同步更新 release/CHECKSUMS.txt
-- ============================================================

CREATE TABLE IF NOT EXISTS `sys_sql_log` (
    `system` varchar(32) NOT NULL,
    `scope` varchar(16) NOT NULL,
    `version` varchar(32) NOT NULL,
    `script_name` varchar(128) NOT NULL,
    `applied_time` datetime NOT NULL,
    `applied_by` varchar(64) DEFAULT NULL,
    PRIMARY KEY (`system`, `scope`, `version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SQL 脚本执行台账';

create table `administrator_user` (
`id` int not null auto_increment comment '主键',
`login_name` varchar(30)  not null comment '登录帐号',
`login_pwd` varchar(64)  not null comment '登录密码',
`actual_name` varchar(30)  not null comment '用户名称',
`phone` varchar(20) default null comment '联系电话',
`gender` int not null comment '性别（0：未知；1：男；2：女）',
`email` varchar(300)  default null comment 'email',
`administrator_flag` tinyint(0) not null default 0 comment '是否为超级管理员: 0 不是，1是',
`last_login_time` datetime default null comment '最近一次登录时间',
`pwd_reset_time` datetime default null comment '最后一次修改密码时间',
 `dept_id` int not null comment '部门ID',
 `role_join` varchar(64) not null comment '角色ID',
 `description` varchar(200) default null comment '描述',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)  comment='管理端 用户';

create table `administrator_dept` (
    `id`          int not null auto_increment comment '主键',
    `name`        varchar(30)  not null                    comment '名称',
    `parent_id`   int  not null default 0,
    `ancestors`   varchar(512) not null default ''         comment '祖级列表',
    `description` varchar(200) default null                comment '描述',
    `sort`        int          not null default 999        comment '排序',
    `status`      tinyint(1)   unsigned not null default 1 comment '状态（1：启用；2：禁用）',
    `is_system`   bit(1)       not null default b'0'       comment '是否为系统内置数据',
    `created_by` int not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by` int default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
    `del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
    primary key (`id`)
) comment='部门表';

create table `administrator_user_menu` (
`id`  int not null auto_increment comment '主键',
`user_id` int not null comment 'user id',
`menu_id` int not null comment '菜单id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)  comment='关联表';

CREATE TABLE IF NOT EXISTS `administrator_role` (
`id`                  int   NOT NULL AUTO_INCREMENT COMMENT 'ID',
`name`                varchar(30)  NOT NULL                COMMENT '名称',
`code`                varchar(30)  NOT NULL                COMMENT '编码',
`data_scope`          tinyint(1)   NOT NULL DEFAULT 4      COMMENT '数据权限（1：全部数据权限；2：本部门及以下数据权限；3：本部门数据权限；4：仅本人数据权限；5：自定义数据权限）',
`description`         varchar(200) DEFAULT NULL            COMMENT '描述',
`sort`                int          NOT NULL DEFAULT 999    COMMENT '排序',
`is_system`           bit(1)       NOT NULL DEFAULT b'0'   COMMENT '是否为系统内置数据',
`menu_join` varchar(1024) default null comment '菜单ID集合',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
PRIMARY KEY (`id`)
)  COMMENT='角色表';

create table `sys_user` (
`id` int not null auto_increment comment '主键',
`login_name` varchar(30)  not null comment '登录帐号',
`login_pwd` varchar(255) not null comment '登录密码（BCrypt）',
`actual_name` varchar(30)  not null comment '用户名称',
`email` varchar(300)  not null comment 'email',
`gender` tinyint(1) not null default 0 comment '性别',
`phone` varchar(15)  null default null comment '手机号码',
`department_id` int(0) not null comment '部门id',
`administrator_flag` tinyint(0) not null default 0 comment '是否为超级管理员: 0 不是，1是',
`last_login_time` datetime default null comment '最近一次登录时间',
`last_password_modified_date` datetime default null comment '最近一次修改密码时间',
`used_password` varchar(5000) null default null comment '密码历史记录',
`remark` varchar(200)  null default null comment '备注',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`login_name_key` varchar(64) generated always as (case when `del_flag` = 0 then lower(`login_name`) else null end) stored comment '活跃账号唯一键：删除后置 NULL 允许同名重建，小写化统一大小写',
primary key (`id`),
unique key `uk_login_name_active` (`login_name_key`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='用户';

create table `sys_role`  (
`id` int not null auto_increment comment '主键',
`role_name` varchar(20)  not null comment '角色名称',
`role_code` varchar(500)  null default null comment '角色编码',
`remark` varchar(200)  null default null comment '角色描述',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '角色表';

create table `sys_department`  (
`id` int not null auto_increment comment '部门主键id',
`name` varchar(50) not null comment '部门名称',
`manager_id` int null default null comment '部门负责人id',
`parent_id` int not null default 0 comment '部门的父级id',
`sort` int(0) not null comment '部门排序',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree,
index `parent_id`(`parent_id`) using btree
) comment = '部门';

create table `sys_menu`  (

`id` int not null auto_increment comment '菜单id',
`menu_name` varchar(200)  not null comment '菜单名称',
`menu_type` int(0) not null comment '类型 1/目录 2/菜单 3/功能',
`parent_id` int not null comment '父菜单id',
`sort` int(0) null default null comment '显示顺序',
`path` varchar(255)  null default null comment '路由地址',
`component` varchar(255)  null default null comment '组件路径',
`perms_type` int(0) null default null comment '权限类型',
`api_perms` text  null comment '后端权限字符串',
`web_perms` text  null comment '前端权限字符串',
`icon` varchar(100)  null default null comment '菜单图标',
`context_menu_id` int null default null comment '功能点关联菜单id',
`frame_flag` tinyint(1) not null default 0 comment '是否为外链',
`frame_url` text  null comment '外链地址',
`cache_flag` tinyint(1) not null default 0 comment '是否缓存',
`visible_flag` tinyint(1) not null default 1 comment '显示状态',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
    `application_code` varchar(64) NOT NULL DEFAULT 'system' COMMENT '应用code（默认 system）',
primary key (`id`) using btree
)comment = '菜单表';

create table `sys_role_menu`  (
`id` int not null auto_increment comment '主键id',
`role_id` int not null comment '角色id',
`menu_id` int not null comment '菜单id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
)comment = '角色-菜单';

create table `sys_role_user`  (
`id` int not null auto_increment,
`role_id` int not null comment '角色id',
`user_id` int not null comment '用户id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '角色用户';

create table `sys_role_data_scope`  (
`id` int not null auto_increment,
`data_scope_type` int(0) not null comment '数据范围id',
`view_type` int(0) not null comment '数据范围类型',
`role_id` int not null comment '角色id',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '角色的数据范围';

create table `sys_dict_key`  (
`id` int not null auto_increment,
`key_code` varchar(50) not null comment '编码',
`key_name` varchar(50) not null comment '名称',
`remark` varchar(500)  null default null comment '备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '字段key';

create table `sys_dict_value`  (
`id` int not null auto_increment,
`dict_key_id` int not null,
`value_code` varchar(50)  not null comment '编码',
`value_name` varchar(50)  not null comment '名称',
`remark` varchar(500)  null default null comment '备注',
`sort` int(0) not null default 0 comment '排序',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)comment = '字典的值';

create table `sys_config`  (
`id` int not null auto_increment comment '主键',
`config_name` varchar(255)  not null comment '参数名字',
`config_key` varchar(255)  not null comment '参数key',
`config_value` text  not null comment '参数值',
`remark` varchar(255)  null default null comment '备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '系统配置';

create table `sys_file`  (
`id` int not null auto_increment comment '主键id',
`folder_type` int not null comment '文件夹类型',
`file_name` varchar(100) null default null comment '文件名称',
`file_size` int(0) null default null comment '文件大小',
`file_key` varchar(200)  not null comment '文件key，用于文件下载',
`file_type` varchar(50)  not null comment '文件类型',
`created_by` int not null comment '创建人',
`updated_by` int default null comment '更新人',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree,
unique index `uk_file_key`(`file_key`) using btree,
index `module_id_module_type`(`folder_type`) using btree,
index `module_type`(`folder_type`) using btree
) comment = '文件';

create table `sys_file_biz`  (
`id` int not null auto_increment comment '主键id',
`file_id` int not null comment '文件id',
`biz_id` int not null comment '业务id',
`biz_code` varchar(32) not null comment '业务类型',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '文件与biz';

create table `sys_folder`  (
`id` int not null auto_increment comment '主键id',
`name` varchar(50) not null comment '名称',
`parent_id` int not null default 0 comment '父级id',
`id_path` varchar(1024) not null comment 'id路径',
`sort` int(0) not null comment '排序',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '文件夹';

create table `sys_table_column`  (
`id` int not null auto_increment,
`user_id` int not null comment '用户id',
`table_id` int(0) not null comment '表格id',
`columns` text null comment '具体的表格列，存入的json',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)  comment = '表格的自定义列存储';

create table `sys_help_doc_catalog`  (
`id` int not null auto_increment comment '帮助文档目录',
`name` varchar(1000) not null comment '名称',
`sort` int(0) not null default 0 comment '排序字段',
`parent_id` int not null comment '父级id',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '帮助文档-目录';

create table `sys_help_doc`  (
`id` int not null auto_increment,
`help_doc_catalog_id` int not null comment '类型1公告 2动态',
`title` varchar(200)  not null comment '标题',
`content_text` text  not null comment '文本内容',
`content_html` text  not null comment 'html内容',
`attachment` varchar(1000)  null default null comment '附件',
`sort` int(0) not null default 0 comment '排序',
`page_view_count` int(0) not null default 0 comment '页面浏览量pv',
`user_view_count` int(0) not null default 0 comment '用户浏览量uv',
`author` varchar(1000)  null default null comment '作者',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '帮助文档';

create table `sys_help_doc_relation`  (
`id` int not null auto_increment,
`relation_id` int not null comment '关联id',
`relation_name` varchar(255) null default null comment '关联名称',
`help_doc_id` int not null comment '文档id',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`, `relation_id`, `help_doc_id`) using btree,
unique index `uni_menu_help_doc`(`relation_id`, `help_doc_id`) using btree
) comment = '帮助文档-关联表';

create table `sys_help_doc_view_record`  (
`id` int not null auto_increment,
`help_doc_id` int not null comment 'help doc id',
`user_id` int not null comment '用户id',
`page_view_count` int(0) null default 0 comment '查看次数',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`, `help_doc_id`, `user_id`) using btree,
unique index `uk_notice_employee`(`help_doc_id`, `user_id`) using btree comment '资讯用户'
) comment = '帮助文档-查看记录';

create table `sys_change_log`  (
`id` int not null auto_increment comment '更新日志id',
`version` varchar(255) not null comment '版本',
`type` int(0) not null comment '更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]',
`publish_author` varchar(255) not null comment '发布人',
`public_date` date not null comment '发布日期',
`content` text not null comment '更新内容',
`link` text null comment '跳转链接',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree,
unique index `version_unique`(`version`) using btree
) comment = '系统更新日志';

create table `sys_feedback`  (
`id` int not null auto_increment comment '主键',
`code` varchar(512)  not null comment '编号',
`feedback_content` text  null comment '反馈内容',
`feedback_attachment` varchar(500)  null default null comment '反馈图片',
`user_id` int not null comment '创建人id',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '意见反馈';

create table `sys_code_generator_config`  (
`id` int not null auto_increment comment '主键',
`table_name` varchar(255) not null comment '表名',
`basic` text null comment '基础命名信息',
`fields` text null comment '字段列表',
`insert_and_update` text null comment '新建、修改',
`delete_info` text null comment '删除',
`query_fields` text null comment '查询',
`table_fields` text null comment '列表',
`detail` text null comment '详情',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`, `table_name`) using btree,
unique index `table_unique`(`table_name`) using btree
) comment = '代码生成器的每个表的配置' ;

create table `sys_login_log`  (
`id` int(0) not null auto_increment comment '主键',
`login_name` varchar(512) not null comment '用户id',
`login_ip` varchar(1000)  null default null comment '用户ip',
`login_ip_region` varchar(1000)  null default null comment '用户ip地区',
`user_agent` text  null comment 'user-agent信息',
`login_result` int(0) not null comment '登录结果：0成功 1失败 2 退出',
`remark` varchar(2000)  null default null comment '备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
primary key (`id`) using btree,
index `customer_id`(`login_name`) using btree
) comment = '用户登录日志';

create table `sys_operate_log`  (
`id` int(0) not null auto_increment comment '主键',
`user_id` int not null comment '用户id',
`trace_id` varchar(50) null default null comment 'Trace ID',
`time_taken` int(0) null default null comment '耗时(ms)',
`module` varchar(50) null default null comment '操作模块',
`content` varchar(500) null default null comment '操作内容',
`url` varchar(100) null default null comment '请求路径',
`method` varchar(100) null default null comment '请求方法',
`param` varchar(4096) null comment '请求参数',
`ip` varchar(255) null default null comment '请求ip',
`ip_region` varchar(1000) null default null comment '请求ip地区',
`user_agent` text null comment '请求user-agent',
`success_flag` tinyint(0) null default null comment '请求结果 0失败 1成功',
`fail_reason` longtext null comment '失败原因',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
primary key (`id`) using btree
) comment = '操作记录';

create table `sys_notice`  (
`id` int(0) not null auto_increment comment '主键',
`notice_type_id` int(0) not null comment '类型1公告 2动态',
`title` varchar(200) not null comment '标题',
`all_visible_flag` tinyint(1) not null comment '是否全部可见',
`scheduled_publish_flag` tinyint(1) not null comment '是否定时发布',
`publish_time` datetime(0) not null comment '发布时间',
`content_text` text not null comment '文本内容',
`content_html` text not null comment 'html内容',
`attachment` varchar(1000) null default null comment '附件',
`page_view_count` int(0) not null default 0 comment '页面浏览量pv',
`user_view_count` int(0) not null default 0 comment '用户浏览量uv',
`source` varchar(1000) null default null comment '来源',
`author` varchar(1000) null default null comment '作者',
`document_number` varchar(1000) null default null comment '文号',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = '通知';

create table `sys_notice_type`  (
`id` int(0) not null auto_increment comment '通知类型',
`notice_type_name` varchar(1000) not null comment '类型名称',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '通知类型';

create table `sys_notice_visible_range`  (
`id` int(0) not null auto_increment comment '主键',
`notice_id` int not null comment '通知id',
`data_type` tinyint(0) not null comment '数据类型1用户 2部门',
`data_id` int not null comment '用户or部门id',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '通知可见范围';

create table `sys_notice_view_record`  (
`id` int not null auto_increment comment '主键',
`notice_id` int not null comment '通知公告id',
`user_id` int not null comment '用户id',
`page_view_count` int(0) null default 0 comment '查看次数',
`first_ip` varchar(255)  null default null comment '首次ip',
`first_user_agent` varchar(1000) null default null comment '首次用户设备等标识',
`last_ip` varchar(255) null default null comment '最后一次ip',
`last_user_agent` varchar(1000) null default null comment '最后一次用户设备等标识',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '通知查看记录';

create table `sys_oa_enterprise`  (
`id` int not null auto_increment comment '企业id',
`enterprise_name` varchar(255) not null comment '企业名称',
`enterprise_logo` varchar(255) null default null comment '企业logo',
`type` int(0) not null default 1 comment '类型（1:有限公司;2:合伙公司）',
`unified_social_credit_code` varchar(255) not null comment '统一社会信用代码',
`contact` varchar(100) not null comment '联系人',
`contact_phone` varchar(100) not null comment '联系人电话',
`email` varchar(100) null default null comment '邮箱',
`province` varchar(100) null default null comment '省份',
`province_name` varchar(100) null default null comment '省份名称',
`city` varchar(100) null default null comment '市',
`city_name` varchar(100) null default null comment '城市名称',
`district` varchar(100) null default null comment '区县',
`district_name` varchar(100) null default null comment '区县名称',
`address` varchar(255) null default null comment '详细地址',
`business_license` varchar(255) null default null comment '营业执照',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment = 'oa企业模块' ;

create table `sys_serial_number`  (
`id` int not null auto_increment comment '主键',
`business_type` int not null comment '业务类型1/意见反馈 2/',
`format` varchar(50) null default null comment '格式[yyyy]表示年,[mm]标识月,[dd]表示日,[nnn]表示三位数字',
`step_random_range` int not null comment '步长随机数',
`remark` varchar(255) null default null comment '备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)comment = '单号生成器定义表';

create table `sys_serial_number_record`  (
`id` int not null auto_increment comment '主键',
`serial_number_id` int(0) not null,
`record_date` datetime not null comment '记录日期',
`last_number` int not null default 0 comment '最后更新值',
`last_time` datetime(0) not null comment '最后更新时间',
`count` int(0) not null default 0 comment '更新次数',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = 'serial_number记录表';

create table `sys_password_policy`  (
`id` int not null auto_increment comment '主键',
`repeatable_password_number` int not null comment '与旧密码重复次数',
`failed_login_maximum_number` int not null comment '登录失败最大次数',
`failed_login_maximum_time` int not null comment '登录失败的时间（minute）',
`password_expired_period` int not null comment '密码过期时间（day）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '密码配置';

create table `sys_login_locked`  (
`id` bigint(0) not null auto_increment comment '自增id',
`login_name` varchar(1000) null default null comment '登录名',
`login_fail_count` int(0) null default null comment '连续登录失败次数',
`lock_flag` tinyint(0) null default 0 comment '锁定状态:1锁定，0未锁定',
`login_lock_begin_time` datetime(0) null default null comment '连续登录失败锁定开始时间',
`login_lock_end_time` datetime(0) null default null comment '连续登录失败锁定结束时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment = '登录失败次数记录表';

create table `sys_email_send_history`  (
`id` int(0) not null auto_increment comment '主键',
`type` int(0) not null comment '类型',
`sent_to` varchar(1000) not null comment '发送对象',
`cc` varchar(1000) default null comment '抄送对象',
`title` varchar(500) not null comment '标题',
`content` varchar(4056) not null comment '内容',
`file_id_array` varchar(1000) default null comment '附件',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
primary key (`id`) using btree
) comment = 'email 发送历史';

create table `sys_info` (
`id` int(0) not null auto_increment comment '主键',
`email_host` varchar(128) not null comment '主机ip',
`email_port` varchar(128) not null comment '主机端口',
`email_username` varchar(128) not null comment '账户',
`email_password` varchar(128) not null comment '密码',
`email_from` varchar(128) not null comment '设置发送方,如：xx<postmaster@xxx.com>',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
)  comment='系统信息';

create table `sys_message`  (
`id` int(0) not null auto_increment comment '主键',
`message_type` smallint(0) not null comment '消息类型',
`receiver_user_id` bigint(0) not null comment '接收者用户id',
`data_id` varchar(500) null default '' comment '相关数据id',
`title` varchar(1000) not null comment '标题',
`content` text not null comment '内容',
`read_flag` tinyint(1) not null default 0 comment '是否已读',
`read_time` datetime(0) null default null comment '已读时间',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree,
index `idx_msg`(`message_type`, `receiver_user_id`) using btree
)comment = '通知消息';

create table `sys_form` (
`id` int auto_increment comment '主键',
`name` varchar(255) not null comment '表单名称',
`json_text` varchar(4056) default null comment '表单内容',
`publish` tinyint(0) not null default 0 comment '发布（true/false）',
`remark` varchar(512)  null default null comment '备注',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
) comment='表单信息';

create table `sys_form_item` (
`id` int auto_increment comment '主键',
`form_id` int not null comment '表单id',
`control_type` varchar(45) not null comment '控件类型 (input、textarea、select、radio、checkbox、date、time、datetime、number、file)',
`label` varchar(128) not null comment '显示名称',
`label_code` varchar(64) not null comment '显示名称code(业务唯一)',
`options` varchar(512) default null  comment '选项数组 select/radio/checkbox',
`required` tinyint(0) not null default 0 comment '必填标识（true、false）',
`order_number` int not null,
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment='表单输入项信息';

create table `sys_form_record` (
`id` int auto_increment comment '主键',
`form_id` int not null comment '表单id',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`) using btree
) comment='表单业务数据信息';

create table `sys_form_record_item` (
`id` int auto_increment comment '主键',
`form_id` int not null comment '表单id',
`form_item_id` int not null comment '表单项id',
`form_record_id` int not null comment '表单业务id',
`current_value` varchar(512) default null comment '当前值',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间',
primary key (`id`) using btree
) comment='表单业务数据输入项信息';

create table `sys_biz_audit` (
`id` int auto_increment comment '主键',
`trace_id` varchar(64) not null comment 'traceId',
`biz_type_code` varchar(64) not null comment '业务类型, 权限code',
`operation` int not null comment '动作 1/新增 2/更新 3/启用 4/停用 5/删除 ',
`biz_index` varchar(128) not null comment '操作索引',
`biz_id` int not null,
`comment` text default null,
`operator` varchar(64) ,
`operation_time` datetime not null,
`ext` json default null,
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
primary key (`id`) using btree,
key `idx_biz_type` (`biz_type_code`) using btree
) comment='Biz Audit';

create table `sys_biz_audit_field` (
`id` int auto_increment,
`biz_audit_id` int not null,
`field_name` varchar(64) not null,
`sort_order` int not null,
`old_value` varchar(1024),
`new_value` varchar(1024),
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
primary key (`id`) using btree,
key `idx_biz_audit_id` (`biz_audit_id`) using btree
) comment='Biz Audit';

create table blaze_certificate_requirements (
`id` int auto_increment primary key,
`customer_id` int not null comment '所属公司',
`certificate_level` int not null comment '证书级别',
`specialty` int not null comment '专业',
`provinces_code` varchar(64) not null comment '单位所在省',
`city_code` varchar(64) not null comment '单位所在市',
`quantity` int not null comment '数量',
`title` int not null comment '职称 1/无 2/初级 3/中级 4/高级 5/不限',
`initial_or_transfer` int not null comment '初始/转注（1/无 2/初始 3/转注 4/其它）',
`certificate_status` int not null comment '证书状态（1/正常 2/不正常）',
`position_contract_price` decimal(10, 2) not null comment '职位合同价',
`other_costs` decimal(10, 2) not null comment '其他费用',
`actual_position_price` decimal(10, 2) not null comment '职位实际价',
`duration` int not null comment '期限（月）',
`bidding_exit` int not null comment '招标出场（1、可出场不招标 2、不出场可招标 3、出场招标 4、项目 5、资质 6、其它情况）',
`three_personnel` int not null comment '三类人员（1、无 2、有A证 3、有B证 4、有C证 5、可考A证 6、可考B证 7、可考C证 8、不考A证 9、不考B证 10、不考C证 11、其它）',
`social_security_requirement` int not null comment '社保要求（1、无社保 2、唯一社保可转 3、唯一社保可停 4、国企社保非唯一 5、私企社保非唯一 ）',
`position_source` int not null comment '职位来源（1、企业直签 2、同行中介）',
`position_title` varchar(255) not null comment '职位标题',
`remarks` text comment '备注',
`responsible_user_id` int not null comment '负责人',
`approve` tinyint(0) not null default 0 comment '审批状态',
`approve_remarks` varchar(32) comment '审批备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间'
) comment='证书需求表';

create table `blaze_talent` (
`id` int auto_increment primary key,
`name` varchar(255) not null comment '姓名',
`id_number` varchar(18) not null unique comment '身份证号',
`contact_number` varchar(15) not null comment '联系电话',
`work_unit_type` int not null comment '工作单位性质 1、私企 2、国企 3、其它',
`social_security_status` int  not null comment '社保状态 1、无社保 2、唯一社保可转 3、唯一社保可停 4、国企社保非唯一 5、私企社保非唯一 ',
`provinces_code` varchar(64) not null comment '社保所在省',
`city_code` varchar(64) not null comment '社保所在市',
`gender` int not null comment '性别 0、未知 1、男 2、女 9、不明',
`title` varchar(100) not null comment '职称 1/无 2/初级 3/中级 4/高级 5/不限',
`responsible_user_id` int not null comment '负责人',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间'
) comment='人才表';

create table blaze_talent_certificate (
`id` int auto_increment primary key,
`talent_id` int not null comment '所属人才',
`certificate_level` int not null comment '证书级别',
`specialty` int not null comment '专业',
`provinces_code` varchar(64) not null comment '发证所在省',
`city_code` varchar(64) not null comment '发证所在市',
`title` int not null comment '职称 1/无 2/初级 3/中级 4/高级 5/不限',
`initial_or_transfer` int not null comment '初始/转注（1/无 2/初始 3/转注 4/其它）',
`certificate_status` int not null comment '证书状态（1/正常 2/不正常）',
`position_contract_price` decimal(10, 2) not null comment '证书合同价',
`other_costs` decimal(10, 2) not null comment '其他费用',
`actual_position_price` decimal(10, 2) not null comment '证书实际价',
`duration` int not null comment '期限（月）',
`bidding_exit` int not null comment '招标出场（1、可出场不招标 2、不出场可招标 3、出场招标 4、项目 5、资质 6、其它情况）',
`three_personnel` int not null comment '三类人员（1、无 2、有A证 3、有B证 4、有C证 5、可考A证 6、可考B证 7、可考C证 8、不考A证 9、不考B证 10、不考C证 11、其它）',
`social_security_requirement` int not null comment '社保要求（1、无社保 2、唯一社保可转 3、唯一社保可停 4、国企社保非唯一 5、私企社保非唯一 ）',
`position_source` int not null comment '证书来源（1、企业直签 2、同行中介）',
`position_title` varchar(255) not null comment '证书标题',
`remarks` text comment '备注',
`responsible_user_id` int not null comment '负责人',
`approve` tinyint(0) not null default 0 comment '审批状态',
`approve_remarks` varchar(32) comment '审批备注',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`created_time` datetime not null comment '创建时间',
`updated_time` datetime default null comment '更新时间'
) comment='人才证书需求';

create table blaze_order (
`id` int auto_increment primary key,
`order_no` varchar(32) null default null comment '订单号',
`talent_cert_id` int not null comment '所属人才证书',
`customer_cert_id` int not null comment '所属企业证书',
`responsible_user_id` int not null comment '负责人',
`contract_time` datetime not null comment '合同时间',
`start_date` datetime default null comment '开始时间',
`end_date` datetime default null comment '结束时间',
`approve` tinyint(0) not null default 0 comment '审批状态',
`approve_remarks` varchar(32) comment '审批备注',
`remarks` text comment '备注',
`first_is_talent` tinyint(0) not null default 0 comment 'true/人才 false/企业',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）'
) comment='订单合同';

create table `sys_user_config` (
`id` int not null auto_increment comment '主键',
`user_id` int not null comment 'user id',
`date_format` varchar(16)  null default null comment '日期显示格式:yyyy-MM-dd',
`timezone` varchar(16)  null default null comment '日期显示格式:Asia/Shanghai',
`append_timezone_style` tinyint(0) not null default 0 comment 'eg: 2025-03-21 15:52:55(+08:00)',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)  comment='用户个性化配置';

create table `blaze_order_detail` (
`id` int not null auto_increment comment '主键',
`blaze_order_id` int not null comment 'blaze order id',
`is_talent` tinyint(0) not null default 0 comment 'true/人才 false/企业',
`operation_time` datetime not null comment '操作时间',
`price` decimal(10, 2) not null comment '金额',
`remarks` text comment '备注',
`responsible_user_id` int not null comment '负责人',
`approve` tinyint(0) not null default 0 comment '审批状态',
`approve_remarks` varchar(32) comment '审批备注',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
primary key (`id`)
)  comment='订单流水';

create table `sys_user_filter` (
  `id` int not null auto_increment comment '主键',
  `user_id` int not null comment 'user id',
  `role_id` int NOT NULL,
  `category_code` varchar(512) NOT NULL,
  `sub_category_code` varchar(512) DEFAULT NULL,
  `filter_title` varchar(512) NOT NULL,
  `filter_content` text DEFAULT NULL comment 'json str',
  `created_by` int not null comment '创建人',
  `created_time` datetime not null comment '创建时间',
  `updated_by` int default null comment '更新人',
  `updated_time` datetime default null comment '更新时间',
  `inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
  `del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
  primary key (`id`)
)  comment='用户表格过滤记录';

create table `app_user` (
`id` int not null auto_increment comment '主键',
`login_name` varchar(64) NOT NULL comment '登录账号',
`login_pwd` varchar(64)  not null comment '登录密码',
`email` varchar(64)  not null comment '邮箱',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
`login_name_key` varchar(64) generated always as (case when `del_flag` = 0 then lower(`login_name`) else null end) stored comment '活跃账号唯一键：删除后置 NULL 允许同名重建，小写化统一大小写',
primary key (`id`),
unique key `uk_login_name_active` (`login_name_key`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='app 用户';

create table `app_user_focus_time` (
`id` int not null auto_increment comment '主键',
`app_user_id` int not null comment 'app user id',
`duration` int not null comment '专注时长',
`focused_time` int not null comment '专注时间',
`is_completed` tinyint(0) not null comment '是否完成',
`white_noise` varchar(512) not null comment '白噪声',
`timestamp` datetime not null comment '时间戳',
`created_by` int not null comment '创建人',
`created_time` datetime not null comment '创建时间',
`updated_by` int default null comment '更新人',
`updated_time` datetime default null comment '更新时间',
`inactive` tinyint(0) not null default 0 comment '状态（true/已失活 false/未失活）',
`del_flag` tinyint(0) not null default 0 comment '删除标识（true/已删除 false/未删除）',
  primary key (`id`)
)

create table `sys_tenant` (
    `id`             int not null auto_increment comment '主键',
    `tenant_name`    varchar(128) not null comment '租户名称',
    `datasource_id`  int not null comment '数据源ID（sys_datasource.id）',
    `database_name`  varchar(64)  default null comment '数据库名称（初始化后自动赋值，不可修改）生成规则 tenant_{id}，同时作为路由 lookupKey',
    `owner_type`     tinyint      not null default 1 comment '归属类型：1=企业 2=团队 3=个人 4=试用 5=内部',
    `isolation_mode` tinyint      not null default 1 comment '隔离模式：1=独立库(当前支持) 2=共享库-行隔离(预留，需框架配套后开放)',
    `expire_time`    datetime     default null comment '到期时间（试用租户用；NULL=长期有效）',
    `init_status`    tinyint      not null default 0 comment '租户库初始化状态：0=未初始化 1=初始化中 2=成功 3=失败（初始化脚本统一放在指定目录与app一一对应）',
    `application_code`  varchar(512) default null comment '关联应用code JSON集合',
    `tenant_name_key` varchar(128) generated always as (case when `del_flag` = 0 then `tenant_name` else null end) stored comment '活跃租户名唯一键：删除后置 NULL 允许重建',
    `created_by`     int not null comment '创建人',
    `created_time`   datetime not null comment '创建时间',
    `updated_by`     int default null comment '更新人',
    `updated_time`   datetime default null comment '更新时间',
    `inactive`       tinyint(1) not null default 0 comment '状态（true=已失活 false=未失活）',
    `del_flag`       tinyint(1) not null default 0 comment '删除标识（true=已删除 false=未删除）',
    `remark`         varchar(512) default null comment '备注',
    primary key (`id`),
    unique key `uk_tenant_name_active` (`tenant_name_key`)
) comment = '租户主表';

create table `sys_datasource` (
    `id`               int not null auto_increment comment '主键',
    `datasource_name`  varchar(128) not null comment '数据源名称（下拉显示用）',
    `jdbc_url`         varchar(512) not null comment 'JDBC URL',
    `username`         varchar(64)  not null comment '数据库账号',
    `password_cipher`  varchar(512) not null comment '数据库密码密文（AES+Base64，密钥独立配置）',
    `created_by`       int not null comment '创建人',
    `created_time`     datetime not null comment '创建时间',
    `updated_by`       int default null comment '更新人',
    `updated_time`     datetime default null comment '更新时间',
    `inactive`         tinyint(1) not null default 0 comment '状态（true=已失活 false=未失活）',
    `del_flag`         tinyint(1) not null default 0 comment '删除标识',
    `remark`           varchar(512) default null comment '备注',
    primary key (`id`)
)  comment = '数据源配置表';

create table `sys_tenant_user` (
    `id`           int not null auto_increment comment '主键',
    `tenant_id`    int not null comment '租户ID',
    `user_id`      int not null comment '用户ID（sys_user.id）',
    `created_by`   int not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by`   int default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive`     tinyint(1) not null default 0 comment '状态',
    `del_flag`     tinyint(1) not null default 0 comment '删除标识',
    primary key (`id`),
    unique key `uk_tenant_user` (`tenant_id`, `user_id`)
) comment = '租户-用户关系';

create table `sys_tenant_menu` (
    `id`           int not null auto_increment comment '主键',
    `tenant_id`    int not null comment '租户ID',
    `menu_id`      int not null comment '菜单ID（sys_menu.id，master 库全局菜单树）',
    `created_by`   int not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by`   int default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive`     tinyint(1) not null default 0 comment '状态',
    `del_flag`     tinyint(1) not null default 0 comment '删除标识',
    primary key (`id`),
    unique key `uk_tenant_menu` (`tenant_id`, `menu_id`)
)  comment = '租户-菜单白名单';

CREATE TABLE IF NOT EXISTS `sys_tenant_init_task` (
    `id`               bigint not null auto_increment comment '主键',
    `task_no`          varchar(64) not null comment '任务号（SSE 订阅标识）',
    `tenant_id`        int not null comment '租户ID',
    `datasource_id`    int not null comment '数据源ID',
    `database_name`    varchar(64) not null comment '目标库名 tenant_{id}',
    `status`           tinyint not null default 0 comment '0运行中 1成功 2失败',
    `current_step_code` varchar(32) default null comment 'DB_EXISTS_CHECK/CREATE_DATABASE/APP_SCRIPT/WRITE_BACK/DONE',
    `progress`         int not null default 0 comment '进度 0-100',
    `database_created` tinyint not null default 0 comment '本次任务是否由系统创建数据库',
    `force_flag`       tinyint not null default 0 comment '是否重新初始化',
    `application_codes` text comment 'application_code JSON 数组字符串',
    `script_statuses`  text comment '各应用脚本执行状态 JSON',
    `error_code`       varchar(64) default null comment '错误码',
    `error_message`    varchar(512) default null comment '脱敏错误摘要',
    `started_time`     datetime default null comment '开始时间',
    `finished_time`    datetime default null comment '结束时间',
    `created_by`       int default null comment '创建人',
    `created_time`     datetime default null comment '创建时间',
    `updated_by`       int default null comment '更新人',
    `updated_time`     datetime default null comment '更新时间',
    `inactive`         tinyint(1) not null default 0 comment '状态',
    `del_flag`         tinyint(1) not null default 0 comment '删除标识',
    primary key (`id`),
    unique key `uk_init_task_no` (`task_no`),
    key `idx_init_task_tenant` (`tenant_id`, `status`)
) engine=InnoDB default charset=utf8mb4 comment='租户初始化任务记录';

INSERT INTO `administrator_user` (`id`, `login_name`, `login_pwd`, `actual_name`, `phone`, `gender`, `email`, `administrator_flag`, `last_login_time`, `pwd_reset_time`, `dept_id`, `role_join`, `description`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (null, 'administrator', '$2a$12$ZfcrKBFIrvg8U1WAzOK9NulftyscQQ/mxCb7gPw6epUh7CbQVaI1u', 'Terry', '', 0, '1@sina.com', 1, null, null, 1, '1', '这是内置的人员', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `administrator_dept` (`id`, `name`, `parent_id`, `ancestors`, `description`, `sort`, `status`, `is_system`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (1, '系统内置部门', 0, '0', '系统内置部门', 999, 1, 1, 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `administrator_dept` (`id`, `name`, `parent_id`, `ancestors`, `description`, `sort`, `status`, `is_system`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (2, '系统内置部门2', 1, '0,1', '系统内置部门2', 999, 1, 0, 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `administrator_role` (`id`, `name`, `code`, `data_scope`, `description`, `sort`, `is_system`, `menu_join`, `created_time`, `updated_time`, `del_flag`) VALUES (1, '超级管理员', 'super_admin', 4, '系统内置角色', 0, 1, null, sysdate(), sysdate(), 0);
INSERT INTO `sys_user` (`id`, `login_name`, `login_pwd`, `actual_name`, `email`, `gender`, `phone`, `department_id`, `administrator_flag`, `last_login_time`, `last_password_modified_date`, `used_password`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (1, 'admin', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', 'Terry', '1@sina.com', 0, '13800000000', 1, 1, null, null, null, '超级管理员', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_user` (`id`, `login_name`, `login_pwd`, `actual_name`, `email`, `gender`, `phone`, `department_id`, `administrator_flag`, `last_login_time`, `last_password_modified_date`, `used_password`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (2, 'dev', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', '王大锤', '1@sina.com', 0, '13800000000', 1, 0, null, null, null, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_user` (`id`, `login_name`, `login_pwd`, `actual_name`, `email`, `gender`, `phone`, `department_id`, `administrator_flag`, `last_login_time`, `last_password_modified_date`, `used_password`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (3, 'blaze', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', '演示账号（只读）', '1@sina.com', 0, '13800000000', 1, 0, null, null, null, '只读用户', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_user` (`id`, `login_name`, `login_pwd`, `actual_name`, `email`, `gender`, `phone`, `department_id`, `administrator_flag`, `last_login_time`, `last_password_modified_date`, `used_password`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (4, 'zhaoming', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', '赵敏', '1@sina.com', 0, '13800000000', 1, 0, null, null, null, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_user` (`id`, `login_name`, `login_pwd`, `actual_name`, `email`, `gender`, `phone`, `department_id`, `administrator_flag`, `last_login_time`, `last_password_modified_date`, `used_password`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (5, 'qiangang', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', '钱刚', '1@sina.com', 0, '13800000000', 1, 0, null, null, null, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_user` (`id`, `login_name`, `login_pwd`, `actual_name`, `email`, `gender`, `phone`, `department_id`, `administrator_flag`, `last_login_time`, `last_password_modified_date`, `used_password`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (6, 'sunli', 'a29c57c6894dee6e8251510d58c07078ee3f49bf', '孙丽', '1@sina.com', 0, '13800000000', 1, 0, null, null, null, '普通用户', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (1, '系统管理员', 'sys_admin', '管理员', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (3, '运维管理员', 'devops_admin', '管理员', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (4, 'HR', 'hr', '人事', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (5, '访客', 'visitor', '只读', 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_department` (`id`, `name`, `manager_id`, `parent_id`, `sort`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (1, '四方合创', 1, 0, 1, 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_department` (`id`, `name`, `manager_id`, `parent_id`, `sort`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (2, '四川壹把火工程咨询有限公司', 1, 1, 1, 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (26, '菜单管理', 2, 50, 1, '/menu/list', '/system/menu/menu-list.vue', NULL, NULL, NULL, 'CopyOutlined', NULL, 0, NULL, 1, 1, 0, 0, 2, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (40, '写操作', 3, 26, NULL, NULL, NULL, 1, 'system:menu:write', 'system:menu:write', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (45, '部门用户', 1, 0, 3, '/organization', NULL, NULL, NULL, NULL, 'UserSwitchOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (46, '部门用户', 2, 45, 1, '/employee/department', '/system/employee/department/index.vue', NULL, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (85, '读操作', 3, 46, 1, NULL, NULL, 1, 'system:department:read', 'system:department:read', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (86, '写操作', 3, 46, 1, NULL, NULL, 1, 'system:department:write', 'system:department:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (91, '写操作', 3, 46, NULL, NULL, NULL, 1, 'system:user:write', 'system:user:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (76, '角色管理', 2, 45, 2, '/employee/role', '/system/employee/role/index.vue', NULL, NULL, NULL, 'SlidersOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (97, '写操作', 3, 76, NULL, NULL, NULL, 1, 'system:role:write', 'system:role:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (80, '日志审计', 2, 111, 3, '/support/audit-log/audit-log-list', '/support/audit-log/audit-log-list.vue', NULL, NULL, NULL, 'VideoCameraOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (87, '邮件记录', 2, 111, 3, '/support/email-history/email-history-list', '/support/email-history/email-history-list.vue', NULL, NULL, NULL, 'MailOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (41, '导出', 3, 26, 3, NULL, NULL, 1, 'system:menu:export', 'system:menu:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (92, '导出', 3, 46, 3, NULL, NULL, 1, 'system:user:export', 'system:user:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (82, '导出', 3, 80, 3, NULL, NULL, 1, 'system:biz_audit:export', 'system:biz_audit:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (93, '导出', 3, 81, 3, NULL, NULL, 1, 'system:operateLog:export', 'system:operateLog:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (94, '导出', 3, 87, 3, NULL, NULL, 1, 'system:emails_send:export', 'system:emails_send:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (96, '导出', 3, 221, 3, NULL, NULL, 1, 'system:session:export', 'system:session:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (98, '导出', 3, 143, 3, NULL, NULL, 1, 'system:loginLog:export', 'system:loginLog:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (99, '导出', 3, 242, 3, NULL, NULL, 1, 'system:area:export', 'system:area:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (100, '导出', 3, 109, 3, NULL, NULL, 1, 'system:config:export', 'system:config:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (101, '导出', 3, 132, 3, NULL, NULL, 1, 'system:notice:export', 'system:notice:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (102, '导出', 3, 144, 3, NULL, NULL, 1, 'system:enterprise:export', 'system:enterprise:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (103, '导出', 3, 152, 3, NULL, NULL, 1, 'system:changeLog:export', 'system:changeLog:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (104, '导出', 3, 110, 3, NULL, NULL, 1, 'support:dict:export', 'system:changeLog:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (105, '导出', 3, 133, 3, NULL, NULL, 1, 'support:cache:export', 'system:changeLog:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (47, '商品管理', 2, 48, 1, '/erp/goods/list', '/business/erp/goods/goods-list.vue', NULL, NULL, NULL, 'AliwangwangOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (48, '商品管理', 1, 137, 10, '/goods', NULL, NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (50, '系统设置', 1, 0, 6, '/setting', NULL, NULL, NULL, NULL, 'SettingOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (78, '商品分类', 2, 48, 2, '/erp/catalog/goods', '/business/erp/catalog/goods-catalog.vue', NULL, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (79, '自定义分组', 2, 48, 3, '/erp/catalog/custom', '/business/erp/catalog/custom-catalog.vue', NULL, NULL, NULL, 'AppstoreAddOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (81, '请求监控', 2, 111, 3, '/support/operate-log/operate-log-list', '/support/operate-log/operate-log-list.vue', NULL, NULL, NULL, 'VideoCameraOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (95, '重置密码', 3, 46, NULL, NULL, NULL, 1, 'system:user:password:reset', 'system:user:password:reset', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (109, '参数配置', 2, 50, 3, '/config/config-list', '/support/config/config-list.vue', NULL, NULL, NULL, 'AntDesignOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (110, '数据字典', 2, 50, 4, '/setting/dict', '/support/dict/index.vue', NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (111, '监控服务', 1, 0, 100, '/monitor', NULL, NULL, NULL, NULL, 'BarChartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (114, '运维工具', 1, 0, 200, NULL, NULL, NULL, NULL, NULL, 'NodeCollapseOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (117, 'Reload', 2, 50, 12, '/hook', '/support/reload/reload-list.vue', NULL, NULL, NULL, 'ReloadOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (122, '数据库监控', 2, 111, 4, '/support/druid/index', NULL, NULL, NULL, NULL, 'ConsoleSqlOutlined', NULL, 1, 'http://mcdull.io:8090/druid', 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (130, '单号管理', 2, 50, 6, '/support/serial-number/serial-number-list', '/support/serial-number/serial-number-list.vue', NULL, NULL, NULL, 'NumberOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (132, '通知公告', 2, 138, 2, '/oa/notice/notice-list', '/business/oa/notice/notice-list.vue', NULL, NULL, NULL, 'SoundOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (133, '缓存管理', 2, 50, 11, '/support/cache/cache-list', '/support/cache/cache-list.vue', NULL, NULL, NULL, 'BorderInnerOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (137, '进销存系统', 1, 0, 2, NULL, NULL, NULL, NULL, NULL, 'AccountBookOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (138, 'OA系统', 1, 0, 1, NULL, NULL, NULL, NULL, NULL, 'BankOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (142, '公告详情', 2, 132, NULL, '/oa/notice/notice-detail', '/business/oa/notice/notice-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (143, '登录日志', 2, 213, 2, '/support/login-log/login-log-list', '/support/login-log/login-log-list.vue', NULL, NULL, NULL, 'LoginOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (144, '企业信息', 2, 138, 1, '/oa/enterprise/enterprise-list', '/business/oa/enterprise/enterprise-list.vue', NULL, NULL, NULL, 'ShopOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (145, '企业详情', 2, 138, NULL, '/oa/enterprise/enterprise-detail', '/business/oa/enterprise/enterprise-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (147, '帮助文档', 2, 218, 1, '/help-doc/help-doc-manage-list', '/support/help-doc/management/help-doc-manage-list.vue', NULL, NULL, NULL, 'FolderViewOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (148, '意见反馈', 2, 218, 2, '/feedback/feedback-list', '/support/feedback/feedback-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (149, '我的通知', 2, 132, NULL, '/oa/notice/notice-employee-list', '/business/oa/notice/notice-employee-list.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (150, '我的通知公告详情', 2, 132, NULL, '/oa/notice/notice-employee-detail', '/business/oa/notice/notice-employee-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (151, '代码生成', 2, 0, 600, '/support/code-generator', '/support/code-generator/code-generator-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (152, '更新日志', 2, 218, 3, '/support/change-log/change-log-list', '/support/change-log/change-log-list.vue', NULL, NULL, NULL, 'HeartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 44, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (156, '查看结果', 3, 117, NULL, NULL, NULL, 1, 'support:reload:result', 'support:reload:result', NULL, 117, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (157, '单号生成', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:generate', 'support:serialNumber:generate', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (158, '生成记录', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:record', 'support:serialNumber:record', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (159, '新建', 3, 110, NULL, NULL, NULL, 1, 'support:dict:add', 'support:dict:add', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (160, '编辑', 3, 110, NULL, NULL, NULL, 1, 'support:dict:edit', 'support:dict:edit', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (161, '批量删除', 3, 110, NULL, NULL, NULL, 1, 'support:dict:delete', 'support:dict:delete', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (162, '刷新缓存', 3, 110, NULL, NULL, NULL, 1, 'support:dict:refresh', 'support:dict:refresh', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (163, '新建', 3, 109, NULL, NULL, NULL, 1, 'support:config:add', 'support:config:add', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (164, '编辑', 3, 109, NULL, NULL, NULL, 1, 'support:config:update', 'support:config:update', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (165, '查询', 3, 47, NULL, NULL, NULL, 1, 'goods:query', 'goods:query', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (166, '新建', 3, 47, NULL, NULL, NULL, 1, 'goods:add', 'goods:add', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (167, '批量删除', 3, 47, NULL, NULL, NULL, 1, 'goods:batchDelete', 'goods:batchDelete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (168, '查询', 3, 147, 11, NULL, NULL, 1, 'support:helpDoc:query', 'support:helpDoc:query', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (169, '新建', 3, 147, 12, NULL, NULL, 1, 'support:helpDoc:add', 'support:helpDoc:add', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (170, '新建目录', 3, 147, 1, NULL, NULL, 1, 'support:helpDocCatalog:addCategory', 'support:helpDocCatalog:addCategory', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (171, '修改目录', 3, 147, 2, NULL, NULL, 1, 'support:helpDocCatalog:update', 'support:helpDocCatalog:update', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (173, '新建', 3, 78, NULL, NULL, NULL, 1, 'category:add', 'category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (174, '查询', 3, 78, NULL, NULL, NULL, 1, 'category:tree', 'category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (175, '编辑', 3, 78, NULL, NULL, NULL, 1, 'category:update', 'category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (176, '删除', 3, 78, NULL, NULL, NULL, 1, 'category:delete', 'category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (177, '新建', 3, 79, NULL, NULL, NULL, 1, 'category:add', 'custom:category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (178, '查询', 3, 79, NULL, NULL, NULL, 1, 'category:tree', 'custom:category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (179, '编辑', 3, 79, NULL, NULL, NULL, 1, 'category:update', 'custom:category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (180, '删除', 3, 79, NULL, NULL, NULL, 1, 'category:delete', 'custom:category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (181, '查询', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:query', 'oa:enterprise:query', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (182, '新建', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:add', 'oa:enterprise:add', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (183, '编辑', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:update', 'oa:enterprise:update', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (184, '删除', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:delete', 'oa:enterprise:delete', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (185, '查询', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:query', 'oa:notice:query', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (186, '写操作', 3, 132, NULL, NULL, NULL, 1, 'system:notice:write', 'system:notice:write', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (190, '查询', 3, 152, NULL, NULL, NULL, 1, '', 'support:changeLog:query', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (191, '新建', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:add', 'support:changeLog:add', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (192, '批量删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:batchDelete', 'support:changeLog:batchDelete', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (193, '文件管理', 2, 50, 20, '/support/file/file-list', '/support/file/file-list.vue', NULL, NULL, NULL, 'FolderOpenOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (194, '删除', 3, 47, NULL, NULL, NULL, 1, 'goods:delete', 'goods:delete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (195, '修改', 3, 47, NULL, NULL, NULL, 1, 'goods:update', 'goods:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (196, '查看详情', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:detail', 'oa:enterprise:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (198, '删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:delete', 'support:changeLog:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (199, '查询', 3, 109, NULL, NULL, NULL, 1, 'support:config:query', 'support:config:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (200, '查询', 3, 193, NULL, NULL, NULL, 1, 'support:file:query', 'support:file:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (201, '删除', 3, 147, 14, NULL, NULL, 1, 'support:helpDoc:delete', 'support:helpDoc:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (202, '更新', 3, 147, 13, NULL, NULL, 1, 'support:helpDoc:update', 'support:helpDoc:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (203, '查询', 3, 143, NULL, NULL, NULL, 1, 'support:loginLog:query', 'support:loginLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (204, '查询', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:query', 'support:operateLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (205, '详情', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:detail', 'support:operateLog:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (206, '心跳监控', 2, 111, 1, '/support/heart-beat/heart-beat-list', '/support/heart-beat/heart-beat-list.vue', 1, NULL, NULL, 'FallOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (207, '更新', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:update', 'support:changeLog:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (212, '查询', 3, 117, NULL, NULL, NULL, 1, 'support:reload:query', 'support:reload:query', NULL, NULL, 0, NULL, 1, 1, 1, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (213, '网络安全', 1, 0, 5, NULL, NULL, 1, NULL, NULL, 'SafetyCertificateOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (214, '登录锁定', 2, 213, 1, '/support/login-fail', '/support/login-fail/login-fail-list.vue', 1, NULL, NULL, 'LockOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (216, '导出', 3, 47, NULL, NULL, NULL, 1, 'goods:exportGoods', 'goods:exportGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (217, '导入', 3, 47, 3, NULL, NULL, 1, 'goods:importGoods', 'goods:importGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (218, '文档中心', 1, 0, 4, NULL, NULL, 1, NULL, NULL, 'FileSearchOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (221, '会话信息', 2, 111, 1, '/support/session/session-list', '/support/session/session-list.vue', 1, 'system:monitor:session_read', 'system:monitor:session_read', 'DashboardOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (223, '强退', 3, 221, NULL, NULL, NULL, 1, 'system:monitor:session_kickout', 'system:monitor:session_kickout', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (222, '查询', 3, 221, NULL, NULL, NULL, 1, 'system:monitor:session_read', 'system:monitor:session_read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (225, '表单设计器', 1, 0, 0, NULL, NULL, 1, NULL, NULL, 'ApiOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (226, '设计表单', 2, 225, NULL, '/support/designer/index', '/support/designer/index.vue', 1, NULL, NULL, 'AndroidOutlined', NULL, 0, NULL, 1, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (229, '密码策略', 2, 213, NULL, '/support/password-policy/index', '/support/password-policy/index.vue', 1, NULL, NULL, 'ContactsOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (230, '读操作', 3, 229, 1, NULL, NULL, 1, 'support:password_policy:read', 'support:password_policy:read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (231, '写操作', 3, 229, 2, NULL, NULL, 1, 'support:password_policy:write', 'support:password_policy:write', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (232, '服务监控', 2, 111, 4, '/support/server/index', '/support/server/index.vue', 1, NULL, NULL, 'CloudServerOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (233, '读操作', 3, 232, 1, NULL, NULL, 1, 'system:monitor_server:read', 'system:monitor_server:read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (235, '查看缓存值', 3, 133, 1, NULL, NULL, 1, 'support:cache:value', 'support:cache:value', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (153, '清除缓存', 3, 133, 2, NULL, NULL, 1, 'support:cache:delete', 'support:cache:delete', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (154, '获取缓存key', 3, 133, 3, NULL, NULL, 1, 'support:cache:list', 'support:cache:list', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (236, '邮箱配置', 2, 50, 1, '/support/email-config/index', '/support/email-config/index.vue', 1, NULL, NULL, 'MailOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (237, '读操作', 3, 236, 1, NULL, NULL, 1, 'support:email_config:read', 'support:email_config:read', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (238, '写操作', 3, 236, 2, NULL, NULL, 1, 'support:email_config:write', 'support:email_config:write', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (239, '表单设计列表', 2, 225, 1, '/support/form/form-list', '/support/form/form-list.vue', 1, NULL, NULL, 'FormOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (240, '表单数据', 2, 225, NULL, '1', NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, 1, 1, 0, 1, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (241, '应用监控', 2, 111, 6, '/supprot/spring-boota-dmin/index', NULL, 1, NULL, NULL, 'AlertOutlined', NULL, 1, 'http://mcdull.io:9000', 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (242, '行政区域', 2, 50, 7, '/support/area/index', '/support/area/index.vue', 1, NULL, NULL, 'BankOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (243, '数据列表', 2, 225, 3, '/support/form/form-data-list', '/support/form/form-data-list.vue', 1, NULL, NULL, 'DiffOutlined', NULL, 0, NULL, 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (244, '查询', 3, 239, NULL, NULL, NULL, 1, 'support:form:page', 'support:form:page', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (245, '新增', 3, 239, NULL, NULL, NULL, 1, 'support:form:add', 'support:form:add', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (246, '更新', 3, 239, NULL, NULL, NULL, 1, 'support:form:update', 'support:form:update', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (247, '删除', 3, 239, NULL, NULL, NULL, 1, 'support:form:delete', 'support:form:delete', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (248, '保存设计', 3, 226, NULL, NULL, NULL, 1, 'support:form:designer', 'support:form:designer', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (249, '发布', 3, 239, NULL, NULL, NULL, 1, 'support:form:publish', 'support:form:publish', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (250, '新增', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:add', 'support:form:record:add', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (251, '查询', 3, 243, 2, NULL, NULL, 1, 'support:form:record:list', 'support:form:record:list', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (252, '导出', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:export', 'support:form:record:export', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (253, '删除', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:delete', 'support:form:record:delete', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (254, '更新', 3, 243, NULL, NULL, NULL, 1, 'support:form:record:update', 'support:form:record:update', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (297, '流程管理', 1, 0, 99, NULL, NULL, 1, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL, 'workflow');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (298, '流程定义', 2, 297, 1, '/system/workflow/def/def-list', '/system/workflow/def/def-list.vue', 1, NULL, NULL, 'BookOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL, 'workflow');
INSERT INTO `sys_role_user` (`role_id`, `user_id`, `created_time`) VALUES (3, 5, sysdate());
INSERT INTO `sys_dict_key` (`id`, `key_code`, `key_name`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (1, 'GODOS_PLACE', '商品产地', '商品产地的字典', 0, sysdate(), sysdate());
INSERT INTO `sys_dict_key` (`id`, `key_code`, `key_name`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (2, 'IN_ACTIVE', '失活标识', 'true/停用 false/启用 ', 0, sysdate(), sysdate());
INSERT INTO `sys_dict_key` (`id`, `key_code`, `key_name`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (3, 'CUSTOMER_TYPE', '客户类型', ' ', 0, sysdate(), sysdate());
INSERT INTO `sys_dict_value` (`id`, `dict_key_id`, `value_code`, `value_name`, `remark`, `sort`, `del_flag`, `created_time`, `updated_time`) VALUES (1, 1, 'luo_yang', '洛阳', '', 1, 0, sysdate(), sysdate());
INSERT INTO `sys_dict_value` (`id`, `dict_key_id`, `value_code`, `value_name`, `remark`, `sort`, `del_flag`, `created_time`, `updated_time`) VALUES (2, 1, 'zheng_zhou', '郑州', '', 2, 0, sysdate(), sysdate());
INSERT INTO `sys_dict_value` (`id`, `dict_key_id`, `value_code`, `value_name`, `remark`, `sort`, `del_flag`, `created_time`, `updated_time`) VALUES (3, 1, 'bei_jing', '北京', '', 3, 0, sysdate(), sysdate());
INSERT INTO `sys_dict_value` (`id`, `dict_key_id`, `value_code`, `value_name`, `remark`, `sort`, `del_flag`, `created_time`, `updated_time`) VALUES (4, 2, 'false', '启用', '', 1, 0, sysdate(), sysdate());
INSERT INTO `sys_dict_value` (`id`, `dict_key_id`, `value_code`, `value_name`, `remark`, `sort`, `del_flag`, `created_time`, `updated_time`) VALUES (5, 2, 'true', '停用', '', 1, 0, sysdate(), sysdate());
INSERT INTO `sys_dict_value` (`id`, `dict_key_id`, `value_code`, `value_name`, `remark`, `sort`, `del_flag`, `created_time`, `updated_time`) VALUES (6, 3, '1', '私企', '', 1, 0, sysdate(), sysdate());
INSERT INTO `sys_dict_value` (`id`, `dict_key_id`, `value_code`, `value_name`, `remark`, `sort`, `del_flag`, `created_time`, `updated_time`) VALUES (7, 3, '2', '国企', '', 1, 0, sysdate(), sysdate());
INSERT INTO `sys_dict_value` (`id`, `dict_key_id`, `value_code`, `value_name`, `remark`, `sort`, `del_flag`, `created_time`, `updated_time`) VALUES (8, 3, '3', '同行中介', '', 1, 0, sysdate(), sysdate());
INSERT INTO `sys_dict_value` (`id`, `dict_key_id`, `value_code`, `value_name`, `remark`, `sort`, `del_flag`, `created_time`, `updated_time`) VALUES (9, 3, '4', '资质公司', '', 1, 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '登录验证码', 'login-captcha', 'yes', 'yes/启用 no/不启用', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '系统名称', 'system-name', 'xxx系统', '用于重置密码占位符', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '域名名称', 'domain-name', 'http://mcdull.io:8081', 'email 链接', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '文件域名', 'file-domain-name', 'http://localhost:8090', '图片文件访问', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '重置密码邮件标题', 'forget-password-email-title', '密码重置请求', '', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '重置密码链接有效期（分钟）', 'forget-password-timeout', '5', '链接有效期', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '是否记录操作请求日志', 'log-operation-request', 'yes', 'yes/启用 no/不启用', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '设置定时任务（数据库备份）', 'task-database-backup', '0 0 */2 * * ?', '比如：*/2 * * * *（每2分钟）空则不启用', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '新增用户Email通知', 'create-account-email', '欢迎加入，您的账号为：{}，初始密码请联系管理员，登录系统后及时修改密码。', '空则不通知', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '新增用户Email自动生成PDF', 'create-account-email-build-pdf', 'yes', 'yes/启用 no/不启用', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '设置账号初始默认密码', 'init-account-password', '123456', '建议五位数以上', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '首次登录自动发送消息通用文案', 'first-login-send-message', '您好！为了确保您的账户安全，请定期更新您的密码，如果有任何疑问，请及时与我们联系。', '站内信', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '密码过期后提醒文案', 'expired-password-reminder', '您的密码于{}已过期，请尽快修改密码。', '站内信', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '密码过期后提醒频次', 'expired-password-reminder-frequency', '1', '1/一次 2/每次 0/从不', 0, sysdate(), sysdate());
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (null, '导出后自动存储', 'export-auto-storage', 'yes', 'yes/启用 no/不启用', 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_catalog` (`id`, `name`, `sort`, `parent_id`, `del_flag`, `created_time`, `updated_time`) VALUES (6, '企业信息', 0, 0, 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_catalog` (`id`, `name`, `sort`, `parent_id`, `del_flag`, `created_time`, `updated_time`) VALUES (9, '企业信用', 0, 6, 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_catalog` (`id`, `name`, `sort`, `parent_id`, `del_flag`, `created_time`, `updated_time`) VALUES (10, '采购文档', 0, 11, 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_catalog` (`id`, `name`, `sort`, `parent_id`, `del_flag`, `created_time`, `updated_time`) VALUES (11, '进销存', 0, 0, 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc` (`id`, `help_doc_catalog_id`, `title`, `content_text`, `content_html`, `attachment`, `sort`, `page_view_count`, `user_view_count`, `author`, `del_flag`, `created_time`, `updated_time`) VALUES (32, 6, '企业名称该写什么？', '、行、备注等，数据变动记录；', '<ul><li style=\"text-align: start;\">需求1：管理公司基本信息，包含：企业名称、logo、地区、营业执照、联系人 等等，可以 增删拆改</li><li style=\"text-align: start;\">需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改</li><li style=\"text-align: start;\">需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改</li><li style=\"text-align: start;\">需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；</li></ul>', '', 0, 49, 1, '卓大', 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc` (`id`, `help_doc_catalog_id`, `title`, `content_text`, `content_html`, `attachment`, `sort`, `page_view_count`, `user_view_count`, `author`, `del_flag`, `created_time`, `updated_time`) VALUES (33, 6, '谁有权限查看企业信息', '纳税号、银行账 任何的修改，都有记录 数据变动记录；', '<ul><li style=\"text-align: start;\">需求1：管理公司基本信息，包含：企业名称、logo、地区、营业执照、联系人 等等，可以 增删拆改</li><li style=\"text-align: start;\">需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改</li><li style=\"text-align: start;\">需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改</li><li style=\"text-align: start;\">需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；</li></ul>', '', 0, 12, 1, '卓大', 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_relation` (`id`, `relation_id`, `relation_name`, `help_doc_id`, `del_flag`, `created_time`, `updated_time`) VALUES (1, 0, '首页', 32, 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_relation` (`id`, `relation_id`, `relation_name`, `help_doc_id`, `del_flag`, `created_time`, `updated_time`) VALUES (2, 0, '首页', 33, 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_view_record` (`id`, `help_doc_id`, `user_id`, `page_view_count`, `del_flag`, `created_time`, `updated_time`) VALUES (1, 31, 1, 3, 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_view_record` (`id`, `help_doc_id`, `user_id`, `page_view_count`, `del_flag`, `created_time`, `updated_time`) VALUES (2, 32, 1, 49, 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_view_record` (`id`, `help_doc_id`, `user_id`, `page_view_count`, `del_flag`, `created_time`, `updated_time`) VALUES (3, 33, 1, 12, 0, sysdate(), sysdate());
INSERT INTO `sys_help_doc_view_record` (`id`, `help_doc_id`, `user_id`, `page_view_count`, `del_flag`, `created_time`, `updated_time`) VALUES (4, 34, 1, 5, 0, sysdate(), sysdate());
INSERT INTO `sys_change_log` (`id`, `version`, `type`, `publish_author`, `public_date`, `content`, `link`, `del_flag`, `created_time`, `updated_time`) VALUES (1, 'v1.0.0', 1, '王大锤', sysdate(), 'v1.0.0 版本正式上线，内容如下：\n\n1.【新增】增加用户姓名查询\n\n2.【新增】增加文件预览组件\n\n3.【新增】新增四级菜单\n', '', 0, sysdate(), sysdate());
INSERT INTO `sys_change_log` (`id`, `version`, `type`, `publish_author`, `public_date`, `content`, `link`, `del_flag`, `created_time`, `updated_time`) VALUES (2, 'v1.0.1', 2, '王大锤', sysdate(), 'v1.0.1 版本正式更新上线，更新内容如下：\n\n1.【新增】增加用户姓名查询\n\n2.【新增】增加文件预览组件\n\n3.【新增】新增四级菜单\n', '', 0, sysdate(), sysdate());
INSERT INTO `sys_change_log` (`id`, `version`, `type`, `publish_author`, `public_date`, `content`, `link`, `del_flag`, `created_time`, `updated_time`) VALUES (3, 'v1.0.2', 3, '王大锤', sysdate(), 'v1.0.2 版本bug修复，更新内容如下：\n\n1.【新增】增加用户姓名查询\n\n2.【新增】增加文件预览组件\n\n3.【新增】新增四级菜单\n', '', 0, sysdate(), sysdate());
INSERT INTO `sys_notice_type` (`id`, `notice_type_name`, `del_flag`, `created_time`, `updated_time`) VALUES (1, '公告', 0, sysdate(), sysdate());
INSERT INTO `sys_notice_type` (`id`, `notice_type_name`, `del_flag`, `created_time`, `updated_time`) VALUES (2, '通知', 0, sysdate(), sysdate());
INSERT INTO `sys_oa_enterprise` (`id`, `enterprise_name`, `enterprise_logo`, `type`, `unified_social_credit_code`, `contact`, `contact_phone`, `email`, `province`, `province_name`, `city`, `city_name`, `district`, `district_name`, `address`, `business_license`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (1, '区块链实验室', '', 1, 'block', 'kk', '18637922222', null, '410000', '河南省', '410300', '洛阳市', '410311', '洛龙区', '区块链大楼', null, 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_oa_enterprise` (`id`, `enterprise_name`, `enterprise_logo`, `type`, `unified_social_credit_code`, `contact`, `contact_phone`, `email`, `province`, `province_name`, `city`, `city_name`, `district`, `district_name`, `address`, `business_license`, `created_by`, `created_time`, `updated_by`, `updated_time`, `inactive`, `del_flag`) VALUES (2, '创新实验室', '', 2, '创新', 'xx', '18637921111', 'xxx@163.com', '410000', '河南省', '410300', '洛阳市', '410311', '洛龙区', '1024大楼', null, 0, sysdate(), 0, sysdate(), 0, 0);
INSERT INTO `sys_serial_number` (`id`, `business_type`, `format`, `step_random_range`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (1, 1, 'FDBK.{yyyy}{mm}{dd}{nnnnn}', 1, 'FDBK.20201101321', 0, sysdate(), sysdate());
INSERT INTO `sys_serial_number` (`id`, `business_type`, `format`, `step_random_range`, `remark`, `del_flag`, `created_time`, `updated_time`) VALUES (2, 2, 'OD{yyyy}{mm}{dd}{nnnnn}', 1, 'OD20201101321', 0, sysdate(), sysdate());
INSERT INTO `sys_password_policy` (`id`, `repeatable_password_number`, `failed_login_maximum_number`, `failed_login_maximum_time`, `password_expired_period`, `del_flag`, `created_time`, `updated_time`) VALUES (1, 3, 5, 10, 30, 0, sysdate(), sysdate());
INSERT INTO `sys_info` (`id`, `email_host`, `email_port`, `email_username`, `email_password`, `email_from`, `del_flag`, `created_time`, `updated_time`) VALUES (1, 'smtp.163.com', '25', 'postmaster@163.com', 'postmaster', 'postmaster', 0, sysdate(), sysdate());
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(500, '租户管理', 2, 50, 7, '/system/tenant', '/system/tenant/tenant-list.vue', 1,
 'system:tenant:query', 'system:tenant:query', 'ClusterOutlined', NULL, 0, NULL,
 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(507, '租户配置工作台', 2, 500, 8, '/system/tenant/setting', '/system/tenant/components/tenant-setting/index.vue', 1,
 'system:tenant:query', 'system:tenant:query', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(510, '数据源配置', 2, 50, 8, '/system/datasource', '/system/datasource/datasource-list.vue', 1,
 'system:datasource:query', 'system:datasource:query', 'DatabaseOutlined', NULL, 0, NULL,
 0, 1, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(501, '查询', 3, 500, 1, NULL, NULL, 1,
 'system:tenant:query', 'system:tenant:query', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(502, '写操作', 3, 500, 2, NULL, NULL, 1,
 'system:tenant:write', 'system:tenant:write', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(503, '删除租户', 3, 500, 3, NULL, NULL, 1,
 'system:tenant:delete', 'system:tenant:delete', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(504, '初始化', 3, 500, 4, NULL, NULL, 1,
 'system:tenant:init', 'system:tenant:init', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(505, '配置租户用户', 3, 500, 5, NULL, NULL, 1,
 'system:tenant:write', 'system:tenant:write', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(506, '配置菜单白名单', 3, 500, 6, NULL, NULL, 1,
 'system:tenant:write', 'system:tenant:write', NULL, 500, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(511, '查询', 3, 510, 1, NULL, NULL, 1,
 'system:datasource:query', 'system:datasource:query', NULL, 510, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(512, '写操作', 3, 510, 2, NULL, NULL, 1,
 'system:datasource:write', 'system:datasource:write', NULL, 510, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(513, '删除数据源', 3, 510, 3, NULL, NULL, 1,
 'system:datasource:delete', 'system:datasource:delete', NULL, 510, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT IGNORE INTO `sys_menu`
(`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`,
 `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`,
 `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`,
 `updated_by`, `updated_time`, `application_code`)
VALUES
(514, '测试连接', 3, 510, 4, NULL, NULL, 1,
 'system:datasource:test', 'system:datasource:test', NULL, 510, 0, NULL,
 0, 0, 0, 0, 1, sysdate(), NULL, NULL, 'system');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`)
SELECT 1, m.id, sysdate()
FROM `sys_menu` m
WHERE m.id BETWEEN 500 AND 514
  AND m.del_flag = 0
  AND NOT EXISTS (
      SELECT 1 FROM `sys_role_menu` rm
      WHERE rm.role_id = 1 AND rm.menu_id = m.id AND rm.del_flag = 0
  );
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (400, '工作流中心', 1, 0, 98, NULL, NULL, 1, NULL, NULL, 'PartitionOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), 1, NULL, 'workflow');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (401, '工作流概览', 2, 400, 1, '/system/workflow/center/overview', '/system/workflow/center/overview.vue', 1, NULL, NULL, 'DashboardOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL, 'workflow');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (402, '流程定义', 2, 400, 2, '/system/workflow/center/definition', '/system/workflow/center/definition.vue', 1, 'workflow:center:definition:list', 'workflow:center:definition:list', 'BookOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL, 'workflow');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (403, '流程设计器', 2, 400, 3, '/system/workflow/center/designer', '/system/workflow/center/designer.vue', 1, 'workflow:center:definition:save', 'workflow:center:definition:save', 'FormOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL, 'workflow');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (404, '流程实例', 2, 400, 4, '/system/workflow/center/instance', '/system/workflow/center/instance.vue', 1, 'workflow:center:instance:list', 'workflow:center:instance:list', 'PlayCircleOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL, 'workflow');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (405, '待办任务', 2, 400, 5, '/system/workflow/center/todo', '/system/workflow/center/todo.vue', 1, 'workflow:center:task:handle', 'workflow:center:task:handle', 'CheckCircleOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL, 'workflow');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (406, '历史任务', 2, 400, 6, '/system/workflow/center/history', '/system/workflow/center/history.vue', 1, 'workflow:center:history:list', 'workflow:center:history:list', 'HistoryOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL, 'workflow');
INSERT INTO `sys_menu` (`id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `inactive`, `del_flag`, `created_by`, `created_time`, `updated_by`, `updated_time`, `application_code`) VALUES (407, '流程图', 2, 400, 7, '/system/workflow/center/diagram', '/system/workflow/center/diagram.vue', 1, NULL, NULL, 'ShareAltOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, NULL, 'workflow');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 400, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 401, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 402, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 403, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 404, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 405, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 406, sysdate());
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_time`) VALUES (1, 407, sysdate());

INSERT IGNORE INTO sys_sql_log(system, scope, version, script_name, applied_time) VALUES ('system', 'master', 'v2.0.0', 'v2.0.0_system.full', NOW());
