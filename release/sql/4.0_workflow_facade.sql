-- workflow facade 支撑脚本：幂等、快照、乐观锁与批量查询索引
-- 适用版本：3.0_workflow_1.7.4.sql 之后执行

ALTER TABLE `flow_definition`
    ADD COLUMN `revision` bigint NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';

ALTER TABLE `flow_instance`
    ADD COLUMN `flow_code` varchar(40) DEFAULT NULL COMMENT '流程编码快照，避免批量查询关联定义表',
    ADD COLUMN `starter_id` varchar(64) DEFAULT NULL COMMENT '发起人 ID 快照',
    ADD COLUMN `starter_name` varchar(100) DEFAULT NULL COMMENT '发起人名称快照',
    ADD COLUMN `business_title` varchar(200) DEFAULT NULL COMMENT '业务标题快照，工作流不回查业务表',
    ADD COLUMN `business_type` varchar(64) DEFAULT NULL COMMENT '业务类型快照',
    ADD COLUMN `revision` bigint NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';

ALTER TABLE `flow_task`
    ADD COLUMN `revision` bigint NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';

ALTER TABLE `flow_his_task`
    ADD COLUMN `approver_id` varchar(64) DEFAULT NULL COMMENT '审批人 ID 快照',
    ADD COLUMN `approver_name` varchar(100) DEFAULT NULL COMMENT '审批人名称快照',
    ADD COLUMN `collaborator_id` varchar(64) DEFAULT NULL COMMENT '协作人 ID 快照',
    ADD COLUMN `collaborator_name` varchar(100) DEFAULT NULL COMMENT '协作人名称快照';

ALTER TABLE `flow_user`
    ADD COLUMN `processed_name` varchar(100) DEFAULT NULL COMMENT '办理人名称快照';

UPDATE `flow_instance` i
    LEFT JOIN `flow_definition` d ON i.definition_id = d.id
SET i.flow_code = d.flow_code,
    i.starter_id = i.create_by,
    i.starter_name = i.create_by
WHERE i.flow_code IS NULL;

UPDATE `flow_his_task` h
SET h.approver_id = h.approver,
    h.approver_name = h.approver
WHERE h.approver IS NOT NULL AND h.approver_id IS NULL;

CREATE TABLE `flow_event_outbox`
(
    `id`             bigint       NOT NULL COMMENT '主键id',
    `tenant_id`      varchar(40)  DEFAULT NULL COMMENT '租户id',
    `event_type`     varchar(64)  NOT NULL COMMENT '事件类型',
    `aggregate_id`   varchar(100) DEFAULT NULL COMMENT '聚合根id',
    `payload_json`   text         COMMENT '事件载荷，只含工作流数据',
    `status`         varchar(20)  NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING/SENT/FAILED',
    `retry_count`    int          NOT NULL DEFAULT 0 COMMENT '重试次数',
    `next_retry_time` datetime    DEFAULT NULL COMMENT '下次重试时间',
    `create_time`    datetime     DEFAULT NULL,
    `update_time`    datetime     DEFAULT NULL,
    `del_flag`       char(1)      DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_flow_event_outbox_status` (`status`, `next_retry_time`, `id`) USING BTREE
) ENGINE = InnoDB COMMENT ='工作流事件发件箱';

CREATE INDEX `idx_flow_instance_business` ON `flow_instance` (`tenant_id`, `flow_code`, `business_id`, `del_flag`);
CREATE INDEX `idx_flow_instance_status_time` ON `flow_instance` (`tenant_id`, `flow_status`, `update_time`, `id`);
CREATE INDEX `idx_flow_task_status_time` ON `flow_task` (`tenant_id`, `flow_status`, `node_type`, `create_time`, `id`);
CREATE INDEX `idx_flow_user_processed` ON `flow_user` (`tenant_id`, `processed_by`, `type`, `del_flag`, `associated`);
CREATE INDEX `idx_flow_his_instance_time` ON `flow_his_task` (`tenant_id`, `instance_id`, `update_time`, `id`);
