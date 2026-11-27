-- ============================================================
-- v2.0.0 全量脚本 - workflow
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

CREATE TABLE `flow_definition`
(
  `id`              bigint          NOT NULL COMMENT '主键id',
  `flow_code`       varchar(40)     NOT NULL COMMENT '流程编码',
  `flow_name`       varchar(100)    NOT NULL COMMENT '流程名称',
  `model_value`            varchar(40)     NOT NULL DEFAULT 'CLASSICS' COMMENT '设计器模型（CLASSICS经典模型 MIMIC仿钉钉模型）',
  `category`        varchar(100)             DEFAULT NULL COMMENT '流程类别',
  `version`         varchar(20)     NOT NULL COMMENT '流程版本',
  `is_publish`      tinyint(1)      NOT NULL DEFAULT '0' COMMENT '是否发布（0未发布 1已发布 9失效）',
  `form_custom`     char(1)                  DEFAULT 'N' COMMENT '审批表单是否自定义（Y是 N否）',
  `form_path`       varchar(100)             DEFAULT NULL COMMENT '审批表单路径',
  `activity_status` tinyint(1)      NOT NULL DEFAULT '1' COMMENT '流程激活状态（0挂起 1激活）',
  `listener_type`   varchar(100)             DEFAULT NULL COMMENT '监听器类型',
  `listener_path`   varchar(400)             DEFAULT NULL COMMENT '监听器路径',
  `ext`             varchar(500)             DEFAULT NULL COMMENT '业务详情 存业务表对象json字符串',
  `create_time`     datetime                 DEFAULT NULL COMMENT '创建时间',
  `create_by`       varchar(64)          DEFAULT '' COMMENT '创建人',
  `update_time`     datetime                 DEFAULT NULL COMMENT '更新时间',
  `update_by`       varchar(64)          DEFAULT '' COMMENT '更新人',
  `del_flag`        char(1)                  DEFAULT '0' COMMENT '删除标志',
  `tenant_id`       varchar(40)              DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
)  COMMENT ='流程定义表';

CREATE TABLE `flow_node`
(
  `id`              bigint        NOT NULL COMMENT '主键id',
  `node_type`       tinyint(1)      NOT NULL COMMENT '节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `definition_id`   bigint          NOT NULL COMMENT '流程定义id',
  `node_code`       varchar(100)    NOT NULL COMMENT '流程节点编码',
  `node_name`       varchar(100)  DEFAULT NULL COMMENT '流程节点名称',
  `permission_flag` varchar(200)  DEFAULT NULL COMMENT '权限标识（权限类型:权限标识，可以多个，用@@隔开)',
  `node_ratio`      varchar(200) DEFAULT NULL COMMENT '流程签署比例值',
  `coordinate`      varchar(100)  DEFAULT NULL COMMENT '坐标',
  `any_node_skip`   varchar(100)  DEFAULT NULL COMMENT '任意结点跳转',
  `listener_type`   varchar(100)  DEFAULT NULL COMMENT '监听器类型',
  `listener_path`   varchar(400)  DEFAULT NULL COMMENT '监听器路径',
  `form_custom`     char(1)       DEFAULT 'N' COMMENT '审批表单是否自定义（Y是 N否）',
  `form_path`       varchar(100)  DEFAULT NULL COMMENT '审批表单路径',
  `version`         varchar(20)     NOT NULL COMMENT '版本',
  `create_time`     datetime      DEFAULT NULL COMMENT '创建时间',
  `create_by`       varchar(64)          DEFAULT '' COMMENT '创建人',
  `update_time`     datetime      DEFAULT NULL COMMENT '更新时间',
  `update_by`       varchar(64)          DEFAULT '' COMMENT '更新人',
  `ext`             text          COMMENT '节点扩展属性',
  `del_flag`        char(1)       DEFAULT '0' COMMENT '删除标志',
  `tenant_id`       varchar(40)   DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
)  COMMENT ='流程节点表';

CREATE TABLE `flow_skip`
(
  `id`             bigint       NOT NULL COMMENT '主键id',
  `definition_id`  bigint          NOT NULL COMMENT '流程定义id',
  `now_node_code`  varchar(100)    NOT NULL COMMENT '当前流程节点的编码',
  `now_node_type`  tinyint(1)   DEFAULT NULL COMMENT '当前节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `next_node_code` varchar(100)    NOT NULL COMMENT '下一个流程节点的编码',
  `next_node_type` tinyint(1)   DEFAULT NULL COMMENT '下一个节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `skip_name`      varchar(100) DEFAULT NULL COMMENT '跳转名称',
  `skip_type`      varchar(40)  DEFAULT NULL COMMENT '跳转类型（PASS审批通过 REJECT退回）',
  `skip_condition` varchar(200) DEFAULT NULL COMMENT '跳转条件',
  `coordinate`     varchar(100) DEFAULT NULL COMMENT '坐标',
  `create_time`    datetime     DEFAULT NULL COMMENT '创建时间',
  `create_by`       varchar(64)          DEFAULT '' COMMENT '创建人',
  `update_time`    datetime     DEFAULT NULL COMMENT '更新时间',
  `update_by`       varchar(64)          DEFAULT '' COMMENT '更新人',
  `del_flag`       char(1)      DEFAULT '0' COMMENT '删除标志',
  `tenant_id`      varchar(40)  DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
)  COMMENT ='节点跳转关联表';

CREATE TABLE `flow_instance`
(
  `id`              bigint      NOT NULL COMMENT '主键id',
  `definition_id`   bigint      NOT NULL COMMENT '对应flow_definition表的id',
  `business_id`     varchar(40) NOT NULL COMMENT '业务id',
  `node_type`       tinyint(1)  NOT NULL COMMENT '节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `node_code`       varchar(40) NOT NULL COMMENT '流程节点编码',
  `node_name`       varchar(100)         DEFAULT NULL COMMENT '流程节点名称',
  `variable`        text COMMENT '任务变量',
  `flow_status`     varchar(20) NOT NULL COMMENT '流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
  `activity_status` tinyint(1)  NOT NULL DEFAULT '1' COMMENT '流程激活状态（0挂起 1激活）',
  `def_json`        text COMMENT '流程定义json',
  `create_time`     datetime             DEFAULT NULL COMMENT '创建时间',
  `create_by`       varchar(64)          DEFAULT '' COMMENT '创建人',
  `update_time`     datetime             DEFAULT NULL COMMENT '更新时间',
  `update_by`       varchar(64)          DEFAULT '' COMMENT '更新人',
  `ext`             varchar(500)         DEFAULT NULL COMMENT '扩展字段，预留给业务系统使用',
  `del_flag`        char(1)              DEFAULT '0' COMMENT '删除标志',
  `tenant_id`       varchar(40)          DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
)  COMMENT ='流程实例表';

CREATE TABLE `flow_task`
(
  `id`            bigint       NOT NULL COMMENT '主键id',
  `definition_id` bigint       NOT NULL COMMENT '对应flow_definition表的id',
  `instance_id`   bigint       NOT NULL COMMENT '对应flow_instance表的id',
  `node_code`     varchar(100) NOT NULL COMMENT '节点编码',
  `node_name`     varchar(100) DEFAULT NULL COMMENT '节点名称',
  `node_type`     tinyint(1)   NOT NULL COMMENT '节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `flow_status`     varchar(20) NOT NULL COMMENT '流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
  `form_custom`   char(1)      DEFAULT 'N' COMMENT '审批表单是否自定义（Y是 N否）',
  `form_path`     varchar(100) DEFAULT NULL COMMENT '审批表单路径',
  `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
  `create_by`       varchar(64)          DEFAULT '' COMMENT '创建人',
  `update_time`   datetime     DEFAULT NULL COMMENT '更新时间',
  `update_by`       varchar(64)          DEFAULT '' COMMENT '更新人',
  `del_flag`      char(1)      DEFAULT '0' COMMENT '删除标志',
  `tenant_id`     varchar(40)  DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
)  COMMENT ='待办任务表';

CREATE TABLE `flow_his_task`
(
  `id`               bigint(20)                   NOT NULL COMMENT '主键id',
  `definition_id`    bigint(20)                   NOT NULL COMMENT '对应flow_definition表的id',
  `instance_id`      bigint(20)                   NOT NULL COMMENT '对应flow_instance表的id',
  `task_id`          bigint(20)                   NOT NULL COMMENT '对应flow_task表的id',
  `node_code`        varchar(100)                 DEFAULT NULL COMMENT '开始节点编码',
  `node_name`        varchar(100)                 DEFAULT NULL COMMENT '开始节点名称',
  `node_type`        tinyint(1)                   DEFAULT NULL COMMENT '开始节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
  `target_node_code` varchar(200)                 DEFAULT NULL COMMENT '目标节点编码',
  `target_node_name` varchar(200)                 DEFAULT NULL COMMENT '结束节点名称',
  `approver`         varchar(40)                  DEFAULT NULL COMMENT '审批人',
  `cooperate_type`   tinyint(1)                   NOT NULL DEFAULT '0' COMMENT '协作方式(1审批 2转办 3委派 4会签 5票签 6加签 7减签)',
  `collaborator`     varchar(500)                  DEFAULT NULL COMMENT '协作人',
  `skip_type`        varchar(10)                  NOT NULL COMMENT '流转类型（PASS通过 REJECT退回 NONE无动作）',
  `flow_status`      varchar(20)                  NOT NULL COMMENT '流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
  `form_custom`      char(1)                      DEFAULT 'N' COMMENT '审批表单是否自定义（Y是 N否）',
  `form_path`        varchar(100)                 DEFAULT NULL COMMENT '审批表单路径',
  `message`          varchar(500)                 DEFAULT NULL COMMENT '审批意见',
  `variable`         TEXT                         DEFAULT NULL COMMENT '任务变量',
  `ext`              TEXT                         DEFAULT NULL COMMENT '业务详情 存业务表对象json字符串',
  `create_time`      datetime                     DEFAULT NULL COMMENT '任务开始时间',
  `update_time`      datetime                     DEFAULT NULL COMMENT '审批完成时间',
  `del_flag`         char(1)                      DEFAULT '0' COMMENT '删除标志',
  `tenant_id`        varchar(40)                  DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
)  COMMENT ='历史任务记录表';


CREATE TABLE `flow_user`
(
  `id`           bigint      NOT NULL COMMENT '主键id',
  `type`         char(1)         NOT NULL COMMENT '人员类型（1待办任务的审批人权限 2待办任务的转办人权限 3待办任务的委托人权限）',
  `processed_by` varchar(80) DEFAULT NULL COMMENT '权限人',
  `associated`   bigint          NOT NULL COMMENT '任务表id',
  `create_time`  datetime    DEFAULT NULL COMMENT '创建时间',
  `create_by`    varchar(80) DEFAULT NULL COMMENT '创建人',
  `update_time`  datetime    DEFAULT NULL COMMENT '更新时间',
  `update_by`       varchar(64)          DEFAULT '' COMMENT '创建人',
  `del_flag`     char(1)     DEFAULT '0' COMMENT '删除标志',
  `tenant_id`    varchar(40) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_processed_type` (`processed_by`, `type`),
  KEY `user_associated` (`associated`) USING BTREE
)  COMMENT ='流程用户表';

CREATE TABLE `flow_idempotency`
(
    `id`             bigint       NOT NULL COMMENT '主键id',
    `tenant_id`      varchar(40)  NOT NULL COMMENT '租户id',
    `idempotency_key` varchar(100) NOT NULL COMMENT '幂等键',
    `request_hash`   varchar(64)  NOT NULL COMMENT '请求内容摘要',
    `status`         varchar(20)  NOT NULL COMMENT 'PROCESSING/SUCCESS/FAILED',
    `response_json`  text         COMMENT '成功响应快照',
    `error_code`     int          DEFAULT NULL COMMENT '失败错误码',
    `error_message`  varchar(500) DEFAULT NULL COMMENT '失败原因',
    `expire_time`    datetime     DEFAULT NULL COMMENT '过期时间',
    `create_time`    datetime     DEFAULT NULL,
    `update_time`    datetime     DEFAULT NULL,
    `del_flag`       char(1)      DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_flow_idempotency` (`tenant_id`, `idempotency_key`) USING BTREE
)  COMMENT ='工作流服务间幂等记录表';

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
)  COMMENT ='工作流事件发件箱';


INSERT IGNORE INTO sys_sql_log(system, scope, version, script_name, applied_time)
       VALUES ('workflow', 'master', 'v2.0.0', 'v2.0.0_workflow.full', NOW());
