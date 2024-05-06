package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 简易的菜单VO
 *
 */
@Data
public class MenuSimpleTreeVO {

    @Schema(description = "菜单ID")
    private Integer menuId;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "功能点关联菜单ID")
    private Integer contextMenuId;

    @Schema(description = "父级菜单ID")
    private Integer parentId;

    @Schema(description = "菜单类型")
    private Integer menuType;

    @Schema(description = "子菜单")
    private List<MenuSimpleTreeVO> children;
}
