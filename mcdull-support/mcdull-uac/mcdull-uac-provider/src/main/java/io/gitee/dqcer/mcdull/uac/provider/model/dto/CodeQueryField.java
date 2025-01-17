package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.CodeQueryFieldQueryTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 代码生成 查询条件 模型
 *
 */

@Data
public class CodeQueryField {

    @NotBlank(message = "5、查询条件 条件名称 不能为空")
    @Schema(description = "条件名称")
    private String label;

    @NotBlank(message = "5、查询条件 字段名 不能为空")
    @Schema(description = "字段名")
    private String fieldName;

    @SchemaEnum(CodeQueryFieldQueryTypeEnum.class)
    @EnumsStrValid(value = CodeQueryFieldQueryTypeEnum.class, message = "5、查询条件 查询条件 查询类型 枚举值错误")
    private String queryTypeEnum;

    @NotEmpty(message = "5、查询条件 列 不能为空")
    @Schema(description = "列")
    private List<String> columnNameList;

    @NotBlank(message = "5、查询条件 宽度 不能为空")
    @Schema(description = "宽度")
    private String width;


}
