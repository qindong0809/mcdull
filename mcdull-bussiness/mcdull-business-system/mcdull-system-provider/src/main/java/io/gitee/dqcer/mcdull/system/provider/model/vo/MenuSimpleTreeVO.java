package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 菜单VO
 *
 * @author dqcer
 * @since 2024/06/03
 */
@Data
public class MenuSimpleTreeVO implements VO {

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
