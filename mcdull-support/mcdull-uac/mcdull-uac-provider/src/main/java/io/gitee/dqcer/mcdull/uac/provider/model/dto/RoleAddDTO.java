package io.gitee.dqcer.mcdull.uac.provider.model.dto;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

/**
 * 角色 添加表单
 *
 */
@Data
public class RoleAddDTO implements DTO {

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    @NotNull(message = "角色名称不能为空")
    @Length(min = 1, max = 20, message = "角色名称(1-20)个字符")
    private String roleName;

    @Schema(description = "角色编码")
    @NotNull(message = "角色编码 不能为空")
    @Length(min = 1, max = 20, message = "角色编码(1-20)个字符")
    private String roleCode;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    @Length(max = 255, message = "角色描述最多255个字符")
    private String remark;


}
