package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.swagger.SchemaEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.ChangeLogTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 系统更新日志 查询
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ChangeLogQueryDTO extends PagedDTO {

    @SchemaEnum(value = ChangeLogTypeEnum.class, desc = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]")
    @EnumsIntValid(value = ChangeLogTypeEnum.class, required = false, message = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复] 错误")
    private Integer type;

    @Schema(description = "发布日期")
    private LocalDate publicDateBegin;

    @Schema(description = "发布日期")
    private LocalDate publicDateEnd;

    @Schema(description = "创建时间")
    private LocalDate createTime;

    @Schema(description = "跳转链接")
    private String link;

}