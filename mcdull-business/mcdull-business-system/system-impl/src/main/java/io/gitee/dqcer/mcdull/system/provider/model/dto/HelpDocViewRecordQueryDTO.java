package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 查阅记录 查询
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HelpDocViewRecordQueryDTO extends PagedDTO {

    @Schema(description = "帮助文档id")
    @NotNull(message = "帮助文档id不能为空")
    private Integer helpDocId;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "关键字")
    private String keywords;


}
