package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 文件夹更新 DTO
 *
 * @author dqcer
 * @since 2024/11/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FolderUpdateDTO extends FolderInsertDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Integer id;

}