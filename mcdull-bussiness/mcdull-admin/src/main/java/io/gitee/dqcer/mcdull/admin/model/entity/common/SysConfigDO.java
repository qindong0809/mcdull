package io.gitee.dqcer.mcdull.admin.model.entity.common;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;

/**
 * 系统参数配置配置
 *
 * @author dqcer
 * @since 2023/03/11
 */
@TableName("sys_config")
public class SysConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String value;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return "SysConfigDO{" +
                "name='" + name + '\'' +
                ", configKey='" + configKey + '\'' +
                ", value='" + value + '\'' +
                ", configType='" + configType + '\'' +
                ", remark='" + remark + '\'' +
                ", delFlag=" + delFlag +
                ", delBy=" + delBy +
                ", createdBy=" + createdBy +
                ", updatedTime=" + updatedTime +
                ", updatedBy=" + updatedBy +
                ", createdTime=" + createdTime +
                ", id=" + id +
                "} " + super.toString();
    }

    public String getName() {
        return name;
    }

    public SysConfigDO setName(String name) {
        this.name = name;
        return this;
    }

    public String getConfigKey() {
        return configKey;
    }

    public SysConfigDO setConfigKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    public String getValue() {
        return value;
    }

    public SysConfigDO setValue(String value) {
        this.value = value;
        return this;
    }

    public String getConfigType() {
        return configType;
    }

    public SysConfigDO setConfigType(String configType) {
        this.configType = configType;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public SysConfigDO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
