package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.system.provider.model.enums.OperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务审计查询 DTO
 *
 * @author dqcer
 * @since 2024/10/29
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class BizAuditQueryDTO extends PagedDTO {

    @Schema(description = "业务类型码")
    private String bizTypeCode;

    @Schema(description = "业务类型")
    @SchemaEnum(value = OperationTypeEnum.class)
    @EnumsIntValid(value = OperationTypeEnum.class, required = false, message = "业务类型")
    private Integer operation;

    @Schema(description = "业务索引")
    private String bizIndex;

    @Schema(description = "操作人")
    private String operator;


}
