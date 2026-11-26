package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 租户主体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant")
public class TenantEntity extends BaseEntity<Integer> {

    private String tenantName;

    private Integer datasourceId;

    private String databaseName;

    private Integer ownerType;

    private Integer isolationMode;

    private Date expireTime;

    private Integer initStatus;

    private String applicationCode;

    private String remark;
}
