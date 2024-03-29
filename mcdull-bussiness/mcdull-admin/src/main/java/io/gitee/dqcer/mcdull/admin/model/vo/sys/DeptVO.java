package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.gitee.dqcer.mcdull.admin.framework.transformer.DictTransformer;
import io.gitee.dqcer.mcdull.admin.framework.transformer.UserTransformer;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.support.VO;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 部门视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@ExcelIgnoreUnannotated
public class DeptVO implements VO {

    /**
     * 主键
     */
    @ExcelProperty("主键id")
    private Long deptId;

    /**
     * 父部门id
     */
    @ExcelProperty("父部门id")
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    @ExcelProperty("部门名称")
    private String deptName;

    /**
     * 显示顺序
     */
    @ExcelProperty("排序")
    private Integer orderNum;

    /**
     * 负责人
     */
    private Long leaderId;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private Date createdTime;

    /**
     * 创建人
     */
    private Long createdBy;

    @Transform(from = "createdBy", transformer = UserTransformer.class)
    private String createdByStr;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    private String updatedByStr;

    /**
     * 状态
     *
     * @see StatusEnum
     */
    @ExcelProperty("状态")
    private String status;

    @Transform(from = "status", param = "sys_normal_disable", transformer = DictTransformer.class)
    private String statusStr;


    /**
     * 删除标识（1/正常 2/删除）
     */
    private Integer delFlag;

    /**
     * 删除str
     */
    @Transform(from = "delFlag", param = "del_flag_type", transformer = DictTransformer.class)
    private String delFlagStr;

    @Override
    public String toString() {
        return new StringJoiner(", ", DeptVO.class.getSimpleName() + "[", "]")
                .add("id=" + deptId)
                .add("parentId=" + parentId)
                .add("ancestors='" + ancestors + "'")
                .add("name='" + deptName + "'")
                .add("orderNum=" + orderNum)
                .add("leaderId=" + leaderId)
                .add("createdTime=" + createdTime)
                .add("createdBy=" + createdBy)
                .add("createdByStr='" + createdByStr + "'")
                .add("updatedTime=" + updatedTime)
                .add("updatedBy=" + updatedBy)
                .add("updatedByStr='" + updatedByStr + "'")
                .add("status=" + status)
                .add("statusStr='" + statusStr + "'")
                .add("delFlag=" + delFlag)
                .add("delFlagStr='" + delFlagStr + "'")
                .toString();
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getCreatedByStr() {
        return createdByStr;
    }

    public void setCreatedByStr(String createdByStr) {
        this.createdByStr = createdByStr;
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

    public String getUpdatedByStr() {
        return updatedByStr;
    }

    public void setUpdatedByStr(String updatedByStr) {
        this.updatedByStr = updatedByStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlagStr() {
        return delFlagStr;
    }

    public void setDelFlagStr(String delFlagStr) {
        this.delFlagStr = delFlagStr;
    }
}
