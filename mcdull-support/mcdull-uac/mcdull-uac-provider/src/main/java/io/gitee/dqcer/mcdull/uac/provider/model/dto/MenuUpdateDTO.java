package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 菜单 更新Form
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuUpdateDTO extends MenuBaseForm {

    @Schema(description = "菜单ID")
    @NotNull(message = "菜单ID不能为空")
    private Integer menuId;
}
