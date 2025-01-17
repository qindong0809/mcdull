package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * OA企业模块编辑
 *
 */
@Data
public class EnterpriseUpdateDTO extends EnterpriseAddDTO {

    @Schema(description = "企业ID")
    @NotNull(message = "企业ID不能为空")
    private Integer enterpriseId;
}
