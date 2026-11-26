package io.gitee.dqcer.mcdull.system.provider.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TenantInitDTO {
    @NotNull(message = "租户ID不能为空")
    private Integer tenantId;
    private Boolean force;
}
