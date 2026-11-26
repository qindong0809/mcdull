package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TenantQueryDTO extends PagedDTO {
    private String tenantName;
    private Integer ownerType;
    private Integer initStatus;
    private Integer datasourceId;
    private String applicationCode;
    private Boolean inactive;
}
