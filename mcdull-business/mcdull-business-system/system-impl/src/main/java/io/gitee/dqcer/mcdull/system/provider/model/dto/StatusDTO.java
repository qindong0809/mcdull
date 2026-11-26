package io.gitee.dqcer.mcdull.system.provider.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusDTO {
    @NotNull(message = "ID不能为空")
    private Integer id;
    @NotNull(message = "状态不能为空")
    private Boolean inactive;
}
