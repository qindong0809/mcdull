package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 更新 帮助文档
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HelpDocUpdateDTO extends HelpDocAddDTO {

    @Schema(description = "id")
    @NotNull(message = "通知id不能为空")
    private Integer helpDocId;

}
