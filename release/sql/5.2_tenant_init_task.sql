-- ============================================================
-- 5.2 租户初始化任务记录表
-- 依赖：5.1_tenant.sql 已执行（sys_tenant / sys_datasource）
-- 用途：记录异步初始化任务状态、步骤进度与脚本执行结果，
--       支持 SSE 断线续接、失败重试与历史查看。
-- 可重放。
-- ============================================================

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
