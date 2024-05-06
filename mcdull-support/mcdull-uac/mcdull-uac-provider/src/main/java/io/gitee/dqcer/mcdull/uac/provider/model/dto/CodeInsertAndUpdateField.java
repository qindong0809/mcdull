package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.swagger.SchemaEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.CodeFrontComponentEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.CodeGeneratorPageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 代码生成 增加、修改的字段 模型
 *
 */

@Data
public class CodeInsertAndUpdateField {

    @NotBlank(message = "3.增加、修改 列名 不能为空")
    @Schema(description = "列名")
    private String columnName;

    @NotNull(message = "3.增加、修改  必须 不能为空")
    @Schema(description = "必须")
    private Boolean requiredFlag;

    @NotNull(message = "3.增加、修改  插入标识 不能为空")
    @Schema(description = "插入标识")
    private Boolean insertFlag;

    @NotNull(message = "3.增加、修改  更新标识 不能为空")
    @Schema(description = "更新标识")
    private Boolean updateFlag;

    @SchemaEnum(value = CodeGeneratorPageTypeEnum.class)
    @EnumsStrValid(value = CodeFrontComponentEnum.class, message = "3.增加、修改  增加、修改 组件类型 枚举值错误", required = true)
    private String frontComponent;

}
