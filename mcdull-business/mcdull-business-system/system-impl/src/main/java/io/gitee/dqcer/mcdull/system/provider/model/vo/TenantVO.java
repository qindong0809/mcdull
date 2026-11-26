package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TenantVO {
    private Integer id;
    private String tenantName;
    private Integer ownerType;
    private Integer isolationMode;
    private Integer datasourceId;
    private String datasourceName;
    private String databaseName;
    private List<String> applicationCode;
    private Date expireTime;
    private Integer initStatus;
    private Boolean inactive;
    private String remark;
    private Date createdTime;
}
