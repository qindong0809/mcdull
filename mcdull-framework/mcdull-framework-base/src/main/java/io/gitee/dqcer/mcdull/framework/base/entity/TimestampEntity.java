package io.gitee.dqcer.mcdull.framework.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 时间戳实体
 *
 * @author dqcer
 * @since 2024/11/05
 */
public class TimestampEntity<T> extends IdEntity<T> {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected Date createdTime;

    /**
     * 已删除标识
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    protected Boolean delFlag;

    @Override
    public String toString() {
        return new StringJoiner(", ", TimestampEntity.class.getSimpleName() + "[", "]")
                .add("createdTime=" + createdTime)
                .add("delFlag=" + delFlag)
                .add("id=" + id)
                .toString();
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
