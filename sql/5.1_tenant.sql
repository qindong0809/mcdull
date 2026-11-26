-- ============================================================
-- 租户管理表结构（可重放）
--
-- 背景：
--   1. 框架已实现"仅按 tenantId 切库"（DynamicDatasourceInterceptor），
--      但租户身份、数据源配置、授权关系此前无表承载。本脚本补齐 master 库侧管理表。
--   2. 数据源密码不明文存储：password_cipher 存 AES+Base64 密文，
--      密钥独立配置 mcdull.security.datasource.aes-key（环境变量注入），
--      不复用登录密码密钥 mcdull.security.password.aes-key。
--   3. 活跃唯一键沿用 5.0 的"生成列 + 条件唯一索引"模式：
--      仅 del_flag=0 的活跃行参与唯一约束，删除后可重建同名租户/数据源。
--
-- 目标环境：MySQL 5.7+ / 8.0+
-- 执行方式：在 master 库上执行（system 服务的 spring.datasource 指向的库）。
-- 表清单：
--   sys_tenant               租户主表
--   sys_datasource           数据源配置表（下拉来源，密码加密）
--   sys_tenant_datasource    租户-数据源绑定（下拉选择落这里）
--   sys_tenant_user          租户-用户集合
--   sys_tenant_menu          租户-菜单权限（菜单白名单）
-- ============================================================

-- ------------------------------------------------------------
-- 1. sys_tenant：租户主表
--    归属类型 owner_type：1=企业 2=团队 3=个人 4=试用 5=内部
--    隔离模式 isolation_mode：1=独立库(当前唯一支持) 2=共享库-表隔离(预留) 3=共享库-行隔离(预留)
--    初始化状态 init_status：0=未初始化 1=初始化中 2=成功 3=失败
-- ------------------------------------------------------------
create table if not exists `sys_tenant` (
    `id`             int not null auto_increment comment '主键',
    `tenant_name`    varchar(128) not null comment '租户名称',
    `datasource_id`  int not null comment '数据源ID（sys_datasource.id）',
    `database_name`  varchar(64)  default null comment '数据库名称（初始化后自动赋值到该字段，不可修改）生成规则tenant_{tenant_id}',
    `owner_type`     tinyint      not null default 1 comment '归属类型：1=企业 2=团队 3=个人 4=试用 5=内部',
    `isolation_mode` tinyint      not null default 1 comment '隔离模式：1=独立库(当前支持) 2=共享库-行隔离(预留) ',
    `expire_time`    datetime     default null comment '到期时间（试用租户用；NULL=长期有效）',
    `init_status`    tinyint      not null default 0 comment '租户库初始化状态：0=未初始化 1=初始化中 2=成功 3=失败（初始化脚本统一放在指定目录与app一一对应）',
    `application_code`  varchar(64)  default null comment '关联应用code json集合',
    `created_by`     int not null comment '创建人',
    `created_time`   datetime not null comment '创建时间',
    `updated_by`     int default null comment '更新人',
    `updated_time`   datetime default null comment '更新时间',
    `inactive`       tinyint(1) not null default 0 comment '状态（true=已失活 false=未失活）',
    `del_flag`       tinyint(1) not null default 0 comment '删除标识（true=已删除 false=未删除）',
    `remark`         varchar(512) default null comment '备注',
    primary key (`id`)
) comment = '租户主表';

-- ------------------------------------------------------------
-- 2. sys_datasource：数据源配置表（"下拉数据源配置"的来源）
--    datasource_key 同时作为路由 lookupKey（如 tenant_42 / shared_trial）
--    password_cipher 存密文；解密在 system 服务返回 TenantDataSource 前完成，
--    明文不入库、不进日志、不进缓存（缓存 VO 存密文，用时解密）
-- ------------------------------------------------------------
create table if not exists `sys_datasource` (
    `id`               int not null auto_increment comment '主键',
    `datasource_name`  varchar(128) not null comment '数据源名称（下拉显示用）',
    `jdbc_url`         varchar(512) not null comment 'JDBC URL',
    `username`         varchar(64)  not null comment '数据库账号',
    `password_cipher`  varchar(512) not null comment '数据库密码密文（AES+Base64，密钥独立配置）',
    `created_by`       int not null comment '创建人',
    `created_time`     datetime not null comment '创建时间',
    `updated_by`       int default null comment '更新人',
    `updated_time`     datetime default null comment '更新时间',
    `inactive`         tinyint(1) not null default 0 comment '状态（true=已失活 false=未失活）',
    `del_flag`         tinyint(1) not null default 0 comment '删除标识',
    `remark`           varchar(512) default null comment '备注',
    primary key (`id`),
    unique key `uk_datasource_key_active` (`datasource_key_active`)
)  comment = '数据源配置表';



-- ------------------------------------------------------------
-- 4. sys_tenant_user：租户-用户集合（管理端"配置租户对应用户"）
--    一个用户可属于多个租户；登录时据此生成授权租户集合 tenantIds。
-- ------------------------------------------------------------
create table if not exists `sys_tenant_user` (
    `id`           int not null auto_increment comment '主键',
    `tenant_id`    int not null comment '租户ID',
    `user_id`      int not null comment '用户ID（sys_user.id）',
    `created_by`   int not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by`   int default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive`     tinyint(1) not null default 0 comment '状态',
    `del_flag`     tinyint(1) not null default 0 comment '删除标识',
    primary key (`id`),
    unique key `uk_tenant_user` (`tenant_id`, `user_id`)
) comment = '租户-用户关系';

-- ------------------------------------------------------------
-- 5. sys_tenant_menu：租户-菜单权限（菜单白名单）
--    语义：该租户可用的菜单集合（全局菜单树的子集）。
--    注意：与"租户独立库内各自的 sys_role_menu"不冲突——
--    本表控制"租户级菜单白名单"，角色级授权仍在租户库内完成。
-- ------------------------------------------------------------
create table if not exists `sys_tenant_menu` (
    `id`           int not null auto_increment comment '主键',
    `tenant_id`    int not null comment '租户ID',
    `menu_id`      int not null comment '菜单ID（sys_menu.id，master 库全局菜单树）',
    `created_by`   int not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by`   int default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive`     tinyint(1) not null default 0 comment '状态',
    `del_flag`     tinyint(1) not null default 0 comment '删除标识',
    primary key (`id`),
    unique key `uk_tenant_menu` (`tenant_id`, `menu_id`)
)  comment = '租户-菜单白名单';
