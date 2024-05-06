package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.swagger.SchemaEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.CodeDeleteEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 代码生成 删除 模型
 *
 */

@Data
public class CodeDelete {

    @Schema(description = "是否支持删除 ")
    @NotNull(message = "4.删除 是否支持删除 不能为空")
    private Boolean isSupportDelete;

    @Schema(description = "是否为物理删除")
    @NotNull(message = "4.删除 是否为物理删除 不能为空")
    private Boolean isPhysicallyDeleted;

    @Schema(description = "删除类型")
    @NotBlank(message = "4.删除 删除类型 不能为空")
    @SchemaEnum(CodeDeleteEnum.class)
    @EnumsStrValid(value = CodeDeleteEnum.class, message = "删除 删除类型 枚举值错误")
    private String deleteEnum;


}
