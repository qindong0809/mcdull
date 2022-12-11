package com.dqcer.framework.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

/**
 * 基础的实体
 *
 * @author dqcer
 * @version 2022/01/12
 */
@SuppressWarnings("unused")
public abstract class BaseDO extends IdDO {

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected Date createdTime;

    /**
     * 创建人
     */
    protected Long createdBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    protected Date updatedTime;

    /**
     * 更新人
     */
    protected Long updatedBy;

    /**
     * 状态
     * @see com.dqcer.framework.base.enums.StatusEnum
     */
    protected Integer status;


    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
