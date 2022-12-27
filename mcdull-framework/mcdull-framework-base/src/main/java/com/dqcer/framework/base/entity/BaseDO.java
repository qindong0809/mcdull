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
public class BaseDO extends MiddleDO {

    private static final long serialVersionUID = 1L;

    /**
     * 删除标识，默认FALSE
     * @see com.dqcer.framework.base.enums.DelFlayEnum
     */
    @TableField(fill = FieldFill.INSERT)
    protected Boolean delFlag;


    /**
     * 删除人
     */
    protected Long delBy;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createdBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    protected Date updatedTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    protected Long updatedBy;

    /**
     * 状态 默认启用
     * @see com.dqcer.framework.base.enums.StatusEnum
     */
    @TableField(fill = FieldFill.INSERT)
    protected Integer status;

    @Override
    public String toString() {
        return "BaseDO{" +
                "delFlag=" + delFlag +
                ", delBy=" + delBy +
                ", createdBy=" + createdBy +
                ", updatedTime=" + updatedTime +
                ", updatedBy=" + updatedBy +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", id=" + id +
                "} " + super.toString();
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public BaseDO setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public Long getDelBy() {
        return delBy;
    }

    public BaseDO setDelBy(Long delBy) {
        this.delBy = delBy;
        return this;
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
