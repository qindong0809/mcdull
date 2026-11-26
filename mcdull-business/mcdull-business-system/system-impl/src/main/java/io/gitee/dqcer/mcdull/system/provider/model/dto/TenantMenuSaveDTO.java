package io.gitee.dqcer.mcdull.system.provider.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TenantMenuSaveDTO {
    @NotNull(message = "租户ID不能为空")
    private Integer tenantId;
    private List<Integer> menuIds;
}
