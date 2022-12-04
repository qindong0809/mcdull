DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
`id` bigint(20) NOT NULL COMMENT '主键',
`mobile` tinyint(4) NOT NULL COMMENT '是否为移动端',
`account_id` bigint(20)  NOT NULL COMMENT '操作人的账号主键',
`tenant_id` bigint(20)  DEFAULT NULL COMMENT '租户主键',
`created_time` datetime  NOT NULL COMMENT '创建时间',
`client_ip` varchar(128)  DEFAULT NULL COMMENT '客户端ip',
`browser` varchar(128)  DEFAULT NULL COMMENT '客户端',
`platform` varchar(128)  DEFAULT NULL COMMENT '平台',
`os` varchar(128)  DEFAULT NULL COMMENT '操作系统',
`engine` varchar(128)  DEFAULT NULL COMMENT '引擎',
`version` varchar(128)  DEFAULT NULL COMMENT '版本',
`engine_version` varchar(128)  DEFAULT NULL COMMENT '引擎版本',
`time` datetime  DEFAULT NULL COMMENT '操作时间',
`method` varchar(128)  DEFAULT NULL COMMENT '请求方法',
`path` varchar(128)   DEFAULT NULL COMMENT '路径',
`time_taken` bigint(20) DEFAULT NULL COMMENT '耗时',
`status` bigint(20) DEFAULT NULL COMMENT 'http状态',
`parameter_map` varchar(1024)  DEFAULT NULL COMMENT '参数map',
`request_body` longtext  DEFAULT NULL COMMENT '请求体',
`headers` text  DEFAULT NULL COMMENT '请求头',
`response_body` longtext  DEFAULT NULL COMMENT '响应头',
PRIMARY KEY (`id`) USING BTREE

) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志记录 ' ROW_FORMAT = DYNAMIC;