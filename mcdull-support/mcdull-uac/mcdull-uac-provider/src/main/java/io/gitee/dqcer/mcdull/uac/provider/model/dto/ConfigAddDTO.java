package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

/**
 * 添加配置表单
 *
 * @author dqcer
 * @since 2024/04/29
 */
@Data
public class ConfigAddDTO implements DTO {

    @Schema(description = "参数key")
    @NotBlank(message = "参数key不能为空")
    @Length(max = 255, message = "参数key最多255个字符")
    private String configKey;

    @Schema(description = "参数的值")
    @NotBlank(message = "参数的值不能为空")
    @Length(max = 60000, message = "参数的值最多60000个字符")
    private String configValue;

    @Schema(description = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Length(max = 255, message = "参数名称最多255个字符")
    private String configName;

    @Schema(description = "备注")
    @Length(max = 255, message = "备注最多255个字符")
    private String remark;
}
