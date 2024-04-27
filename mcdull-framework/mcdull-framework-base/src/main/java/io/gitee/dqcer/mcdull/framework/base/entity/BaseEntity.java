package io.gitee.dqcer.mcdull.framework.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.StringJoiner;

/**
 * 基础的实体
 *
 * @author dqcer
 * @since 2022/01/12
 */
public class BaseEntity<T> extends RelEntity<T> {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    protected Integer createdBy;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    protected Integer updatedBy;

    /**
     * 已失活标识
     */
    @TableField(fill = FieldFill.INSERT)
    protected Boolean inactive;

    @Override
    public String toString() {
        return new StringJoiner(", ", BaseEntity.class.getSimpleName() + "[", "]")
                .add("createdBy=" + createdBy)
                .add("updatedBy=" + updatedBy)
                .add("inactive=" + inactive)
                .add("createdTime=" + createdTime)
                .add("updatedTime=" + updatedTime)
                .add("id=" + id)
                .toString();
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }
}
