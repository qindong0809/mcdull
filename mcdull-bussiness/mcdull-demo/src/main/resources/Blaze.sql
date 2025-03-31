
INSERT INTO `sys_menu` VALUES (257, '企业管理', 2, 256, 1, '/blaze/customer/customer-info-list', '/blaze/customer/customer-info-list.vue', 1, NULL, NULL, 'AppstoreOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (256, '企业端', 1, 255, NULL, NULL, NULL, 1, NULL, NULL, 'AppstoreAddOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (255, '慧联智配', 1, 0, 0, NULL, NULL, 1, NULL, NULL, 'DeploymentUnitOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);

INSERT INTO `sys_menu` VALUES (262, '证书匹配', 2, 255, NULL, '/blaze/order/blaze-order-list', '/blaze/order/blaze-order-list.vue', 1, NULL, NULL, 'AlignCenterOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (261, '证书需求', 2, 256, NULL, '/blaze/certificate/certificate-requirements-list', '/blaze/certificate/certificate-requirements-list.vue', 1, NULL, NULL, 'AppstoreOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), 1, sysdate());
INSERT INTO `sys_menu` VALUES (260, '证书管理', 2, 258, NULL, '/blaze/talent/certificate-list', '/blaze/talent/certificate-list.vue', 1, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (259, '人才管理', 2, 258, NULL, '/blaze/talent/talent-list', '/blaze/talent/talent-list.vue', 1, NULL, NULL, 'BookOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (258, '人才端',   1, 255, NULL, NULL, NULL, 1, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, sysdate(), NULL, NULL);

INSERT INTO `sys_menu` VALUES (270, '导出', 3, 257, 3, NULL, NULL, 1, 'blaze:customer_info:export', 'blaze:customer_info:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (271, '写操作', 3, 257, 3, NULL, NULL, 1, 'blaze:customer_info:write', 'blaze:customer_info:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (272, '导出', 3, 259, 3, NULL, NULL, 1, 'blaze:talent:export', 'blaze:talent:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (273, '写操作', 3, 259, 3, NULL, NULL, 1, 'blaze:talent:write', 'blaze:talent:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (274, '导出', 3, 260, 3, NULL, NULL, 1, 'blaze:talent_certificate:export', 'blaze:talent_certificate:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (275, '写操作', 3, 260, 3, NULL, NULL, 1, 'blaze:talent_certificate:write', 'blaze:talent_certificate:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (276, '导出', 3, 261, 3, NULL, NULL, 1, 'blaze:certificate_requirements:export', 'blaze:certificate_requirements:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (277, '写操作', 3, 261, 3, NULL, NULL, 1, 'blaze:certificate_requirements:write', 'blaze:certificate_requirements:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (278, '导出', 3, 262, 3, NULL, NULL, 1, 'blaze:order:export', 'blaze:order:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (279, '写操作', 3, 262, 3, NULL, NULL, 1, 'blaze:order:write', 'blaze:order:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);

INSERT INTO `sys_menu` VALUES (280, '人才打款', 2, 258, 3, '/blaze/talent/talent-order-detail-list', '/blaze/talent/talent-order-detail-list.vue', 1, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (281, '企业回款', 2, 256, 3, '/blaze/certificate/cust-order-detail-list', '/blaze/certificate/cust-order-detail-list.vue', 1, NULL, NULL, 'AreaChartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2025-03-31 14:02:09', NULL, NULL);

INSERT INTO `sys_menu` VALUES (282, '导出', 3, 280, 3, NULL, NULL, 1, 'blaze:order_detail_talent:export', 'blaze:order_detail_talent:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (283, '写操作', 3, 280, 3, NULL, NULL, 1, 'blaze:order_detail_talent:write', 'blaze:order_detail_talent:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);


INSERT INTO `sys_menu` VALUES (284, '导出', 3, 281, 3, NULL, NULL, 1, 'blaze:order_detail_customer:export', 'blaze:order_detail_customer:export', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);
INSERT INTO `sys_menu` VALUES (285, '写操作', 3, 281, 3, NULL, NULL, 1, 'blaze:order_detail_customer:write', 'blaze:order_detail_customer:write', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, sysdate(), NULL, NULL);



drop table if exists
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