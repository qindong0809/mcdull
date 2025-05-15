package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色的菜单更新
 */
@Data
public class RoleMenuUpdateDTO implements DTO {

    /**
     * 角色id
     */
    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    private Integer roleId;

    /**
     * 菜单ID 集合
     */
    @Schema(description = "菜单ID集合")
    @NotNull(message = "菜单ID不能为空")
    private List<Integer> menuIdList;

}
