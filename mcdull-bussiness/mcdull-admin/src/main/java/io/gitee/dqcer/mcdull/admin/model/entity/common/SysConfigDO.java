package io.gitee.dqcer.mcdull.admin.model.entity.common;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import lombok.Data;

/**
 * 系统参数配置配置
 *
 * @author dqcer
 * @since 2023/03/11
 */
@Data
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
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

    /**
     * 备注
     */
    private String remark;
}
