package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据源配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_datasource")
public class DatasourceEntity extends BaseEntity<Integer> {

    private String datasourceName;

    private String jdbcUrl;

    private String username;

    private String passwordCipher;

    private String remark;
}
