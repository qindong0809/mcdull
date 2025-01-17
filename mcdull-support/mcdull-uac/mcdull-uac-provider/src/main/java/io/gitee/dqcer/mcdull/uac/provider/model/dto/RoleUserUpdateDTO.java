package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色的员工更新
 *
 */
@Data
public class RoleUserUpdateDTO implements DTO {

    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    protected Integer roleId;

    @Schema(description = "员工id集合")
    @NotEmpty(message = "员工id不能为空")
    protected List<Integer> employeeIdList;

}
