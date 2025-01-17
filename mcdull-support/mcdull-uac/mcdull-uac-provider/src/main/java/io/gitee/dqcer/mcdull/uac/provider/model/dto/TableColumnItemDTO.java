package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 自定义表格列
 *
 */
@Data
public class TableColumnItemDTO implements DTO {

    @NotEmpty(message = "列不能为空")
    @Schema(description = "字段")
    private String columnKey;

    @Schema(description = "宽度")
    private Integer width;

    @NotNull(message = "显示不能为空")
    @Schema(description = "是否显示")
    private Boolean showFlag;

    @NotNull(message = "排序不能为空")
    @Schema(description = "排序")
    private Integer sort;


}
