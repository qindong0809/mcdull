
INSERT INTO `sys_menu` VALUES (257, '企业管理', 2, 256, 1, '/blaze/customer/customer-info-list', '/blaze/customer/customer-info-list.vue', 1, NULL, NULL, 'AppstoreOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-24 23:12:50', NULL, NULL);
INSERT INTO `sys_menu` VALUES (256, '企业端', 1, 255, NULL, NULL, NULL, 1, NULL, NULL, 'AppstoreAddOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-24 23:10:15', NULL, NULL);
INSERT INTO `sys_menu` VALUES (255, '慧联智配', 1, 0, 0, NULL, NULL, 1, NULL, NULL, 'DeploymentUnitOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-24 23:03:54', NULL, NULL);

INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, sort, `path`, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, inactive, del_flag, created_by, created_time, updated_by, updated_time) VALUES(262, '证书匹配', 2, 255, NULL, '/blaze/order/blaze-order-list', '/blaze/order/blaze-order-list.vue', 1, NULL, NULL, 'AlignCenterOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2025-03-19 13:25:09', NULL, NULL);
INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, sort, `path`, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, inactive, del_flag, created_by, created_time, updated_by, updated_time) VALUES(261, '证书需求', 2, 256, NULL, '/blaze/certificate/certificate-requirements-list', '/blaze/certificate/certificate-requirements-list.vue', 1, NULL, NULL, 'AppstoreOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2025-03-19 13:16:51', 1, '2025-03-19 13:17:59');
INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, sort, `path`, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, inactive, del_flag, created_by, created_time, updated_by, updated_time) VALUES(260, '证书管理', 2, 258, NULL, '/blaze/talent/certificate-list', '/blaze/talent/certificate-list.vue', 1, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2025-03-19 13:12:00', NULL, NULL);
INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, sort, `path`, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, inactive, del_flag, created_by, created_time, updated_by, updated_time) VALUES(259, '人才管理', 2, 258, NULL, '/blaze/talent/talent-list', '/blaze/talent/talent-list.vue', 1, NULL, NULL, 'BookOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2025-03-19 12:46:56', NULL, NULL);
INSERT INTO sys_menu (id, menu_name, menu_type, parent_id, sort, `path`, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, inactive, del_flag, created_by, created_time, updated_by, updated_time) VALUES(258, '人才端', 1, 255, NULL, NULL, NULL, 1, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2025-03-19 12:32:40', NULL, NULL);

drop table if exists `blaze_customer_info`;
create table if not exists `blaze_customer_info` (
`id` int auto_increment comment '主键',
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