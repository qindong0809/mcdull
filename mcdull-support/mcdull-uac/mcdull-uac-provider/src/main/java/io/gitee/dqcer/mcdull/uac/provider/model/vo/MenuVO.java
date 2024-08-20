package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class MenuVO implements VO {

    @Schema(description = "菜单id")
    private Integer menuId;

    @Schema(description = "菜单名称")
    private String menuName;

    /**
     * {@link io.gitee.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum}
     */
    @Schema(description = "菜单类型")
    private Integer menuType;

    @Schema(description = "父菜单id")
    private Integer parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "权限类型")
    private Integer permsType;

    @Schema(description = "api权限")
    private String apiPerms;

    @Schema(description = "web权限")
    private String webPerms;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "是否为上下文菜单")
    private Integer contextMenuId;

    @Schema(description = "是否为frame")
    private Boolean frameFlag;

    @Schema(description = "frame地址")
    private String frameUrl;

    @Schema(description = "是否缓存")
    private Boolean cacheFlag;

    @Schema(description = "是否可见")
    private Boolean visibleFlag;
}
