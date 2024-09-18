package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.NoticeVisitbleRangeDataTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 通知公告 可见范围数据
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVisibleRangeDTO {

    @SchemaEnum(NoticeVisitbleRangeDataTypeEnum.class)
    private Integer dataType;

    @Schema(description = "员工/部门id")
    @NotNull(message = "员工/部门id不能为空")
    private Integer dataId;
}
