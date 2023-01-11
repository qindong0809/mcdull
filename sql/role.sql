CREATE TABLE IF NOT EXISTS `sys_resource` (
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
  `res_code` varchar(128) NOT NULL COMMENT '模块code',
  `path` varchar(128) NOT NULL COMMENT '路由',
  `component` varchar(128) NOT NULL COMMENT '组件',
  `type` varchar(8) NOT NULL COMMENT '资源类型 系统sys 菜单menu、按钮button',
  `sys_id` bigint(20) NOT NULL COMMENT '所属系统 1/权限系统',
  `group_no` varchar(16) DEFAULT NULL COMMENT '同组标识',
  `disabled` INT(1) NULL DEFAULT 0 COMMENT '是否默认勾选并且不可变更：1：是，0：否',
  `del_flag` int(1) NOT NULL '删除标识（1/正常 2/删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';


CREATE TABLE IF NOT EXISTS `mds_role_resource` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `role_id` bigint(20) NOT NULL COMMENT '角色主键',
  `resource_id` bigint(20) NOT NULL COMMENT '资源主键',
  `del_flag` int(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `idx_mrr_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色资源中间表';