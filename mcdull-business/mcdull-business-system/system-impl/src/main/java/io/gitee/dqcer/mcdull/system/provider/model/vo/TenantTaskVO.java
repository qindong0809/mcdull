package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;

@Data
public class TenantTaskVO {
    private String taskNo;
    private Integer tenantId;
    private String databaseName;
    private Integer initStatus;
    private String currentStepCode;
}
