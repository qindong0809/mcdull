# 数据库规约

设计规约

---

### 1、表结构 
- 主表基础字段
``` sql
-- xxx_xxx: 表示系统名_模块名/业务名称_表的作用
drop table if exists `xxx_xxx`; 
create table if not exists `xxx_xxx` (
    `id` bigint(20) not null comment '主键',
    `created_by` bigint(20) not null comment '创建人',
    `created_time` datetime not null comment '创建时间',
    `updated_by` bigint(20) default null comment '更新人',
    `updated_time` datetime default null comment '更新时间',
    `inactive` bit(1) not null default b'0' comment '状态（true/已失活 false/未失活）',
    `del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
    primary key (`id`)
)  comment='xxxxx';
```

- 中间表基础字段
``` sql
-- rel 是"relational"（关系型）的缩写
drop table if exists `xxx_xxx_rel`;
create table if not exists `xxx_xxx_rel` (
    `id` bigint(20) not null comment '主键',
    `created_time` datetime not null comment '创建时间',
    `updated_time` datetime default null comment '更新时间',
    `del_flag` bit(1) not null default b'0' comment '删除标识（true/已删除 false/未删除）',
    primary key (`id`)
)  comment='xxxxx';
```
- 存储字符串长度固定使用```char```
- `varchar`长度不超过5000， 超过使用`text`, 有必要时独立出来一张表，避免影响其它字段索引效率
- 若表中存在表示类型的字段，必须在字段后注释说明所有类型
``` sql
-- 反例
status char(1) not null comment '状态',   
-- 正例
status char(1) not null comment '状态（1/已提交 2/已完成 3/已关闭）'
```
- 字段设计在符合业务的情况下，建议都设计为 `not null`


### 2、当前用户修改资料
- 接口
``` api
	/SysUser/updateInfo
```
- 参数
``` p 
	{String}	avatar = ""				头像
	{String}	username = ""			昵称
	{int}		sex = 1					性别
	{String}	phone = ""				联系电话
	{String}	introduce = ""			个人介绍
```
- 返回 
@import(res)


--- 
### 3、根据旧密码修改新密码
- 接口：
``` api
	/SysUser/updatePassword
```
- 参数: 
``` p
	{String}	old_pwd		旧密码
	{String}	new_pwd		新密码
```
- 返回 
@import(res)

--- 
### 4、直接修改新密码
- 接口
``` api
	/SysUser/updatePassword2
```
- 参数
``` p
	{String}	new_pwd		新密码
```
- 返回 
@import(res)







