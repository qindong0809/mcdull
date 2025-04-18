ALTER TABLE blaze_talent MODIFY COLUMN work_unit_type int NULL COMMENT '工作单位性质 1、私企 2、国企 3、其它';
ALTER TABLE blaze_talent MODIFY COLUMN social_security_status int NULL COMMENT '社保状态 1、无社保 2、唯一社保可转 3、唯一社保可停 4、国企社保非唯一 5、私企社保非唯一 ';
ALTER TABLE blaze_talent MODIFY COLUMN provinces_code varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '社保所在省';
ALTER TABLE blaze_talent MODIFY COLUMN city_code varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '社保所在市';
ALTER TABLE blaze_talent MODIFY COLUMN gender int NULL COMMENT '性别 0、未知 1、男 2、女 9、不明';
ALTER TABLE blaze_talent MODIFY COLUMN title varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '职称 1/无 2/初级 3/中级 4/高级 5/不限';

UPDATE sys_menu SET sort=1 WHERE id=259;
UPDATE sys_menu SET sort=2 WHERE id=260;
UPDATE sys_menu SET sort=3 WHERE id=280;

UPDATE sys_menu SET sort=1 WHERE id=257;
UPDATE sys_menu SET sort=2 WHERE id=261;
UPDATE sys_menu SET sort=3 WHERE id=281;


insert into `sys_change_log` values (4, 'v1.1.1', 3, '麦兜', sysdate(), 'v1.1.1 版本，更新内容如下：\n\n1.【优化】人才管理将不必要的字段改为选填\n\n2.【优化】调整人才端、企业端子菜单顺序\n\n3.【新增】所有证书级别追加其它选项\n\n4.【修复】证书标题显示异常', '', 0, sysdate(), sysdate());


-- 领导驾驶舱