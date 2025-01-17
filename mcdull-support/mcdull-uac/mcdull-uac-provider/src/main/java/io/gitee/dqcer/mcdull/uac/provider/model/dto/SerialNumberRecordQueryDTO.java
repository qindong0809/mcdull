package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * 单据序列号 生成记录 查询
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SerialNumberRecordQueryDTO extends PagedDTO {

    @Schema(description = "单号id")
    @NotNull(message = "单号id不能为空")
    private Integer serialNumberId;
}
