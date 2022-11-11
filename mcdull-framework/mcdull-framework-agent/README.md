# mcdull

#### javaagent 探针

入库、统计每个方法的花费时间、对应的堆栈信息、非堆栈以外的信息已经GC回收次数

在VM启动参数中添加以下参数：
``Add VM options``
```shell

mvn clean package

-javaagent:D:\gitee\mcdull\mcdull-framework\mcdull-framework-agent\target\mcdull-framework-agent-1.0-SNAPSHOT.jar
```

```sqlDROP TABLE IF EXISTS `service_log`;
CREATE TABLE `service_log` (
`id` bigint NOT NULL  COMMENT 'id',
`trace_id` varchar(255) NOT NULL  COMMENT '跟踪id',
`class_name` varchar(255)  DEFAULT NULL COMMENT '类名',
`method_name` varchar(255)  DEFAULT NULL COMMENT '方法名称',
`req_args` longtext  DEFAULT NULL COMMENT '服务请求参数',
`result` longtext  COMMENT '服务响应参数',
`cost_time` bigint DEFAULT NULL COMMENT '请求响应耗时，单位毫秒',
`status` char(1)  DEFAULT NULL COMMENT '响应状态，0、成功;1、失败',
`created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`end_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
`before_head_memory_init` bigint DEFAULT NULL COMMENT '执行前：堆内存-初始化大小',
`before_head_memory_used` bigint DEFAULT NULL COMMENT '执行前：堆内存-已使用',
`before_head_memory_committed` bigint DEFAULT NULL COMMENT '执行前：堆内存-提交的内存量保证Java虚拟机可以使用这个内存',
`before_head_memory_max` bigint DEFAULT NULL COMMENT '执行前：堆内存-最大值',
`before_head_rate` bigint DEFAULT NULL COMMENT '执行前：已使用 * 100 / 内存 -百分比',
`before_non_head_memory_init` bigint DEFAULT NULL COMMENT '执行前：非堆内存-初始化大小',
`before_non_head_memory_used` bigint DEFAULT NULL COMMENT '执行前：非堆内存-已使用',
`before_non_head_memory_committed` bigint DEFAULT NULL COMMENT '执行前：非堆内存-提交的内存量保证Java虚拟机可以使用这个内存',
`before_non_head_memory_max` bigint DEFAULT NULL COMMENT '执行前：非堆内存-最大值',
`before_non_head_rate` bigint DEFAULT NULL COMMENT '执行前：非已使用 * 100 / 内存 -百分比',
`cost_head_memory_init` bigint DEFAULT NULL COMMENT '执行后共花费：堆内存-初始化大小',
`cost_head_memory_used` bigint DEFAULT NULL COMMENT '执行后共花费：堆内存-已使用',
`cost_head_memory_committed` bigint DEFAULT NULL COMMENT '执行后共花费：堆内存-提交的内存量保证Java虚拟机可以使用这个内存',
`cost_head_memory_max` bigint DEFAULT NULL COMMENT '执行后共花费：堆内存-最大值',
`cost_head_rate` bigint DEFAULT NULL COMMENT '执行后共花费：已使用 * 100 / 内存 -百分比',
`cost_non_head_memory_init` bigint DEFAULT NULL COMMENT '执行后共花费：非堆内存-初始化大小',
`cost_non_head_memory_used` bigint DEFAULT NULL COMMENT '执行后共花费：非堆内存-已使用',
`cost_non_head_memory_committed` bigint DEFAULT NULL COMMENT '执行后共花费：非堆内存-提交的内存量保证Java虚拟机可以使用这个内存',
`cost_non_head_memory_max` bigint DEFAULT NULL COMMENT '执行后共花费：非堆内存-最大值',
`cost_non_head_rate` bigint DEFAULT NULL COMMENT '执行后共花费：非已使用 * 100 / 内存 -百分比',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='agent 探针表';


DROP TABLE IF EXISTS `service_gc_log`;
CREATE TABLE `service_gc_log` (
`id` bigint NOT NULL  COMMENT 'id',
`service_log_id` bigint NOT NULL  COMMENT '日志主表id',
`name` varchar(255)  DEFAULT NULL COMMENT '类名',
`memory_pool_names` varchar(255)  DEFAULT NULL COMMENT '内存池名称',
`count` longtext  COMMENT 'gc 次数',
`time` longtext  COMMENT 'gc 时间',
`type` int(1) NOT NULL COMMENT '类型 1/之前 2/之后',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='agent 探针GC明细表';

```
#### 待办事项


