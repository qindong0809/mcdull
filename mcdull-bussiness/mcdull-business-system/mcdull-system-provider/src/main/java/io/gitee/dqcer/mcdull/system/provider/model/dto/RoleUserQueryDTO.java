package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色的员工查询
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleUserQueryDTO extends PagedDTO {

    @Schema(description = "关键字")
    private String keywords;

    @Schema(description = "角色id")
    private Integer roleId;
}
