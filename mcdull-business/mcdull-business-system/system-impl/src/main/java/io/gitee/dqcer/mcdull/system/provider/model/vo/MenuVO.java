package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

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

    @Schema(description = "归属应用编码")
    private String applicationCode;

    /**
     * {@link io.gitee.dqcer.mcdull.system.provider.model.enums.MenuTypeEnum}
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

    // 以下 7 个字段原先由 MenuEntity 继承而来，MenuConvert.toVO 会逐个赋值，
    // 已属于对外响应契约；显式声明是为了切断「视图对象继承持久化对象」，
    // 字段本身要裁剪需另开一次前后端契约变更。
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "创建时间")
    private Date createdTime;

    @Schema(description = "更新时间")
    private Date updatedTime;

    @Schema(description = "删除标记")
    private Boolean delFlag;

    @Schema(description = "创建人")
    private Integer createdBy;

    @Schema(description = "更新人")
    private Integer updatedBy;

    @Schema(description = "有效标记")
    private Boolean inactive;
}
