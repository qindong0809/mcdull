package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;

/**
 * 系统角色实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_role")
public class RoleEntity extends BaseEntity {

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     *  类型（1/自定义 2/内置）
     *  {@link io.gitee.dqcer.mcdull.admin.model.enums.UserTypeEnum}
     */
    private Integer type;

    private String status;

    @Override
    public String toString() {
        return "RoleDO{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", createdBy=" + createdBy +
                ", updatedTime=" + updatedTime +
                ", updatedBy=" + updatedBy +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", delFlag=" + delFlag +
                ", id=" + id +
                "} " + super.toString();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
