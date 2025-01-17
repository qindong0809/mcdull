package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 配置updatedto
 * 配置更新表单
 *
 * @author dqcer
 * @date 2024/04/29
 */
@Data
public class ConfigUpdateDTO extends ConfigAddDTO {

    @Schema(description = "configId")
    @NotNull(message = "configId不能为空")
    private Integer configId;
}
