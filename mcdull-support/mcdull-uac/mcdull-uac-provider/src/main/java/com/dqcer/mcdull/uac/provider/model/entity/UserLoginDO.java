package com.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dqcer.framework.base.entity.IdDO;

import java.util.Date;

/**
 * 用户登录实体
 *
 * @author dqcer
 * @version 2022/12/18
 */
@TableName("sys_user_login")
public class UserLoginDO extends IdDO {

    private static final long serialVersionUID = 8275559175477580456L;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER )
    private Date createdTime;

    /**
     * 菜单id
     */
    private Long userId;

    /**
     * token
     */
    private String token;


    /**
     * 类型（1/登录 2/注销）
     */
    private Integer type;

    public Date getCreatedTime() {
        return createdTime;
    }

    public UserLoginDO setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public UserLoginDO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserLoginDO setToken(String token) {
        this.token = token;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public UserLoginDO setType(Integer type) {
        this.type = type;
        return this;
    }
}