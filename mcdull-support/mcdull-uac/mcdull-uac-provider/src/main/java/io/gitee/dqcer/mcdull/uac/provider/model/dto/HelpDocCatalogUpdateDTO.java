package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 帮助文档 目录
 *
 */
@Data
public class HelpDocCatalogUpdateDTO extends HelpDocCatalogAddDTO {

    @Schema(description = "id")
    @NotNull(message = "id")
    private Integer helpDocCatalogId;
}
