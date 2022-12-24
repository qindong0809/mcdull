package com.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dqcer.framework.base.entity.BaseDO;

/**
 * 系统角色实体
 *
 * @author dqcer
 * @version 2022/11/07
 */
@TableName("sys_role")
public class RoleDO extends BaseDO {

    /**
     * 删除标识（1/正常 2/删除）
     */
    private Integer delFlag;

    /**
     * 昵称
     */
    private String name;

    /**
     * 账户
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     *  类型（1/自定义 2/内置）
     */
    private Integer type;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RoleDO{");
        sb.append("delFlag=").append(delFlag);
        sb.append(", name='").append(name).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", type=").append(type);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", status=").append(status);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
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
}
