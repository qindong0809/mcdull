package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * system config
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_config")
public class ConfigEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private String configName;

    private String configKey;

    private String configValue;

    private String remark;
}
