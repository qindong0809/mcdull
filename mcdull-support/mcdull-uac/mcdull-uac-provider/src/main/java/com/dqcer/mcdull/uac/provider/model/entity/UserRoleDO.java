package com.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dqcer.framework.base.entity.IdDO;

import java.util.Date;

/**
 * 系统角色实体
 *
 * @author dqcer
 * @version 2022/11/07
 */
@TableName("sys_user_role")
public class UserRoleDO extends IdDO {

    private static final long serialVersionUID = 8275559175477580456L;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER )
    private Date createdTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
