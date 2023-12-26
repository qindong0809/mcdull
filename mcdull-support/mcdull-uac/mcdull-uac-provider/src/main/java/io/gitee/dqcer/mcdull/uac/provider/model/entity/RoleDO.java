package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;

import java.util.StringJoiner;

/**
 * 系统角色实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_role")
public class RoleDO extends BaseDO {

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
     * 类型（1/自定义 2/内置）
     */
    private Integer type;


    private String status;

    @Override
    public String toString() {
        return new StringJoiner(", ", RoleDO.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("description='" + description + "'")
                .add("type=" + type)
                .add("status='" + status + "'")
                .add("delFlag=" + delFlag)
                .add("createdBy=" + createdBy)
                .add("updatedTime=" + updatedTime)
                .add("updatedBy=" + updatedBy)
                .add("createdTime=" + createdTime)
                .add("id=" + id)
                .toString();
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}