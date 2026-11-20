-- demo 模块初始化脚本：MySQL 8.x
CREATE DATABASE IF NOT EXISTS demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE demo;

DROP TABLE IF EXISTS demo_task;
CREATE TABLE demo_task (
    id            INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    title         VARCHAR(64)  NOT NULL COMMENT '任务标题',
    content       VARCHAR(2000) DEFAULT NULL COMMENT '任务内容',
    status        TINYINT      NOT NULL DEFAULT 0 COMMENT '状态：0待办 1进行中 2已完成 3已取消',
    priority      TINYINT      NOT NULL DEFAULT 2 COMMENT '优先级：1低 2中 3高',
    due_time      DATETIME     DEFAULT NULL COMMENT '截止时间',
    created_by    INT          DEFAULT NULL COMMENT '创建人',
    updated_by    INT          DEFAULT NULL COMMENT '更新人',
    inactive      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '失活标识',
    created_time  DATETIME     DEFAULT NULL COMMENT '创建时间',
    updated_time  DATETIME     DEFAULT NULL COMMENT '更新时间',
    del_flag      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (id),
    KEY idx_demo_task_status (status),
    KEY idx_demo_task_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='演示任务表';

INSERT INTO demo_task (title, content, status, priority, due_time, created_by, updated_by, inactive, created_time, updated_time, del_flag)
VALUES
    ('阅读架构说明', '熟悉 facade / provider 分层与四层调用链', 2, 2, DATE_ADD(NOW(), INTERVAL 1 DAY), 0, 0, 0, NOW(), NOW(), 0),
    ('运行 demo 服务', '初始化数据库后启动 demo-bootstrap', 0, 3, DATE_ADD(NOW(), INTERVAL 2 DAY), 0, 0, 0, NOW(), NOW(), 0);
