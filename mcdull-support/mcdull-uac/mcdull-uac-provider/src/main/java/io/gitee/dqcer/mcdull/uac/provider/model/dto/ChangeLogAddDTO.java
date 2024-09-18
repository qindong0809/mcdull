package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.ChangeLogTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 系统更新日志 新建表单
 *
 */

@Data
public class ChangeLogAddDTO implements DTO {

    @Schema(description = "版本")
    @NotBlank(message = "版本 不能为空")
    private String version;

    @SchemaEnum(value = ChangeLogTypeEnum.class, desc = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]")
    @EnumsIntValid(value = ChangeLogTypeEnum.class, message = "更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复] 错误")
    private Integer type;

    @Schema(description = "发布人")
    @NotBlank(message = "发布人 不能为空")
    private String publishAuthor;

    @Schema(description = "发布日期")
    @NotNull(message = "发布日期 不能为空")
    private LocalDate publicDate;

    @Schema(description = "更新内容")
    @NotBlank(message = "更新内容 不能为空")
    private String content;

    @Schema(description = "跳转链接")
    private String link;

}