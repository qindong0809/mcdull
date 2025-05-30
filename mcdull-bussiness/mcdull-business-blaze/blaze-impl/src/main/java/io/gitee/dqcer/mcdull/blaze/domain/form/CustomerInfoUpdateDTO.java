package io.gitee.dqcer.mcdull.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 企业信息 更新表单
 *
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */

@Data
public class CustomerInfoUpdateDTO implements DTO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主键 不能为空")
    private Integer id;

    @Schema(description = "企业名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "企业名称 不能为空")
    private String name;

    @Schema(description = "所在地省代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "所在地省代码 不能为空")
    private String provincesCode;

    @Schema(description = "所在市代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "所在市代码 不能为空")
    private String cityCode;

    @Schema(description = "联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "联系人 不能为空")
    private String contactPerson;

    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "联系电话 不能为空")
    private String phoneNumber;

    @Schema(description = "客户类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "客户类型 不能为空")
    private String customerType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态 不能为空")
    private Boolean inactive;

}