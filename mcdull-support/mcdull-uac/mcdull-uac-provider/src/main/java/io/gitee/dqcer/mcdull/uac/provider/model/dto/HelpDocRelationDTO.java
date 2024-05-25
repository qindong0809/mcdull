package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 帮助文档 关联项目
 *
 */
@Data
public class HelpDocRelationDTO implements DTO {

    @Schema(description = "关联名称")
    @NotBlank(message = "关联名称不能为空")
    private String relationName;

    @Schema(description = "关联id")
    @NotNull(message = "关联id不能为空")
    private Integer relationId;
}
