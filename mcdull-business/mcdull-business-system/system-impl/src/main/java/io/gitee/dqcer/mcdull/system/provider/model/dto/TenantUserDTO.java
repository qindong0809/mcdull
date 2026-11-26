package io.gitee.dqcer.mcdull.system.provider.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TenantUserDTO {
    @NotNull(message = "租户ID不能为空")
    private Integer tenantId;
    @NotEmpty(message = "用户不能为空")
    private List<Integer> userIds;
}
