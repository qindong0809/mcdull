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
    `account` varchar(128) NOT NULL COMMENT '账号',
    `password` varchar(128) NOT NULL COMMENT '密码',
    `salt` varchar(128) NOT NULL COMMENT '密码盐',
    `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(128) DEFAULT NULL COMMENT '手机号',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
    PRIMARY KEY (`id`),
    KEY `idx_pa_account` (`account`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
INSERT INTO sys_user(id, created_by, created_time, updated_by, updated_time, status, del_flag, account, password, salt, email, phone, last_login_time)
VALUES(1589631293412503554, 1589631293412503554, '2022-10-31 07:20:54', null, null, 1, 1, 'admin', '7a69d7186df1a65ed7af2ba00747488e2415bf1a', 'c7e87439-aef3-48e0-be26-678d0ab99345', null, null, null);


CREATE TABLE IF NOT EXISTS `sys_dict` (
    `id` bigint(20) NOT NULL COMMENT '主键',
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
    `del_flag` int(1) NOT NULL DEFAULT '1' COMMENT '1-未删除,2-删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='公共码表';

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