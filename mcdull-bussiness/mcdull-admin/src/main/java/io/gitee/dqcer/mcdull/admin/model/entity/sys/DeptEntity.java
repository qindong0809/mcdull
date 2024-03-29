package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;

import java.util.StringJoiner;

/**
 * 系统部门实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_dept")
public class DeptEntity extends BaseEntity {

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 祖先所有id集合
     */
    private String ancestors;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private Long leaderId;

    /**
     * 状态
     * @see io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum
     */
    private String status;

    @Override
    public String toString() {
        return new StringJoiner(", ", DeptEntity.class.getSimpleName() + "[", "]")
                .add("parentId=" + parentId)
                .add("ancestors='" + ancestors + "'")
                .add("name='" + name + "'")
                .add("orderNum=" + orderNum)
                .add("leaderId=" + leaderId)
                .add("status=" + status)
                .add("delFlag=" + delFlag)
                .add("createdBy=" + createdBy)
                .add("updatedTime=" + updatedTime)
                .add("updatedBy=" + updatedBy)
                .add("createdTime=" + createdTime)
                .add("id=" + id)
                .toString();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
