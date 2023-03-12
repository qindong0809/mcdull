package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
public class RoleVO implements VO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private Long createdBy;

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
    private Integer status;

    @Transform(from = "status", param = "status_type")
    private String statusStr;


    /**
     * 删除标识（1/正常 2/删除）
     */
    private Integer delFlag;

    /**
     * 删除str
     */
    @Transform(from = "delFlag", param = "del_flag_type")
    private String delFlagStr;

    /**
     * 账户
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     *  类型（1/自定义 2/内置）
     */
    private Integer type;

    @Transform(from = "type", param = "data_type")
    private String typeStr;

    @Override
    public String toString() {
        return new StringJoiner(", ", RoleVO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
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
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("type=" + type)
                .add("typeStr='" + typeStr + "'")
                .toString();
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public RoleVO setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public String getDelFlagStr() {
        return delFlagStr;
    }

    public void setDelFlagStr(String delFlagStr) {
        this.delFlagStr = delFlagStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
