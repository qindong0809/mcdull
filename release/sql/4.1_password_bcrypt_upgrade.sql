-- 口令体系升级到 BCrypt：sys_user.login_pwd 原为 varchar(50)，无法容纳 60 字符的 BCrypt 值。
-- 本脚本为幂等扩列，重复执行不会报错。
ALTER TABLE `sys_user` MODIFY COLUMN `login_pwd` varchar(255) NOT NULL COMMENT '登录密码';

-- administrator_user / app_user 原为 varchar(64)，已可容纳 60 字符 BCrypt，无需扩列。
-- 存量 SHA1 口令不做离线迁移（不可逆），由用户登录时透明升级，或管理员调用 resetPassword 强制重置。
