package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;

import java.util.Date;

/**
 * 用户登录实体
 *
 * @author dqcer
 * @since 2022/12/18
 */
@TableName("sys_user_login")
public class UserLoginDO extends IdDO {

    private static final long serialVersionUID = 1L;
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
     * token
     */
    private String token;


    /**
     * 类型（1/登录 2/注销）
     */
    private Integer type;

    @Override
    public String toString() {
        return "UserLoginDO{" +
                "createdTime=" + createdTime +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", type=" + type +
                ", id=" + id +
                "} " + super.toString();
    }

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
