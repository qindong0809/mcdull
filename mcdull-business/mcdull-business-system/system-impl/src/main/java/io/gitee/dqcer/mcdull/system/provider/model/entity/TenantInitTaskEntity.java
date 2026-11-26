package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 租户初始化任务
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant_init_task")
public class TenantInitTaskEntity extends BaseEntity<Long> {

    private String taskNo;

    private Integer tenantId;

    private Integer datasourceId;

    private String databaseName;

    private Integer status;

    private String currentStepCode;

    private Integer progress;

    private Boolean databaseCreated;

    private Boolean forceFlag;

    private String applicationCodes;

    private String scriptStatuses;

    private String errorCode;

    private String errorMessage;

    private Date startedTime;

    private Date finishedTime;
}
