package com.dqcer.framework.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

/**
 * 中间实体表， 针对中间1对多、多对多关联表
 *
 * @author dqcer
 * @date 2022/01/12
 */
@SuppressWarnings("unused")
public abstract class MiddleEntity extends SuperId {

    /**
     * 创建时间，方便增量同步
     */
    @TableField(fill = FieldFill.INSERT)
    protected Date createdTime;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
