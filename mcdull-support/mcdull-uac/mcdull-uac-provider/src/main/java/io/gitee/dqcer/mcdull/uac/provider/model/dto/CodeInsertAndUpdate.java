package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.CodeGeneratorPageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 代码生成 增加、修改 模型
 *
 */

@Data
public class CodeInsertAndUpdate {

    @NotNull(message = "3.增加、修改 是否支持增加、修改 不能为空")
    private Boolean isSupportInsertAndUpdate;

    @SchemaEnum(CodeGeneratorPageTypeEnum.class)
    @EnumsStrValid(value = CodeGeneratorPageTypeEnum.class, message = "3.增加、修改 增加、修改 页面类型 枚举值错误")
    private String pageType;

    @Schema(description = "宽度")
    private String width;

    @NotNull(message = "3.增加、修改 每行字段数量 不能为空")
    @Schema(description = "每行字段数量")
    private Integer countPerLine;

    @Schema(description = "字段列表")
    private List<CodeInsertAndUpdateField> fieldList;

}
