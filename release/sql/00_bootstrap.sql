-- ============================================================
-- 启动脚本：创建 SQL 执行台账（可重放）
-- 全量/增量脚本均依赖本表记录版本，禁止业务直接修改台账
-- ============================================================

CREATE TABLE IF NOT EXISTS `sys_sql_log` (
    `system`       varchar(32)  not null comment '系统域：system/workflow/...',
    `scope`        varchar(16)  not null comment '范围：master/tenant',
    `version`      varchar(32)  not null comment '版本：v2.0.0',
    `script_name`  varchar(128) not null comment '脚本名',
    `applied_time` datetime     not null comment '执行时间',
    `applied_by`   varchar(64)  default null comment '执行人',
    primary key (`system`, `scope`, `version`)
) engine=InnoDB default charset=utf8mb4 comment='SQL 脚本执行台账';
