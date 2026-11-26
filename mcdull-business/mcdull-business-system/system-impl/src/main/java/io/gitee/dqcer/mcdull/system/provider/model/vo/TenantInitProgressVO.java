package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;

@Data
public class TenantInitProgressVO {
    private Integer tenantId;
    private String taskNo;
    private String taskId;
    private String stepCode;
    private String stepName;
    private Integer progress;
    private String message;
    private String databaseName;
    private Boolean databaseCreated;
    private String applicationCode;
    private Integer scriptIndex;
    private String errorCode;
    private String failedStepCode;
    private Long timestamp;
}
