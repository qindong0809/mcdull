package io.gitee.dqcer.mcdull.framework.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 中间实体表， 针对中间1对多、多对多关联表
 *
 * @author dqcer
 * @since 2022/01/12
 */
public class RelDO extends IdDO {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected Date createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    protected Date updatedTime;

    /**
     * 已删除标识
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    protected Boolean delFlag;

    @Override
    public String toString() {
        return new StringJoiner(", ", RelDO.class.getSimpleName() + "[", "]")
                .add("createdTime=" + createdTime)
                .add("updatedTime=" + updatedTime)
                .add("delFlag=" + delFlag)
                .add("id=" + id)
                .toString();
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
