package io.gitee.dqcer.mcdull.system.provider.model.audit;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.audit.AuditDescription;
import lombok.Data;

/**
 * 菜单审核
 *
 * @author dqcer
 * @since 2024/11/04
 */
@Data
public class MenuAudit implements Audit {

    @AuditDescription(label = "菜单名称", sort = 1)
    private String menuName;

    /**
     * {@link io.gitee.dqcer.mcdull.system.provider.model.enums.MenuTypeEnum}
     */
    @AuditDescription(label = "菜单类型", sort = 2)
    private String menuTypeName;

    @AuditDescription(label = "父级菜单", sort = 3)
    private String parentName;

    @AuditDescription(label = "排序", sort = 4)
    private Integer sort;

    @AuditDescription(label = "路由地址", sort = 5)
    private String path;

    @AuditDescription(label = "组件路径", sort = 6)
    private String component;

    @AuditDescription(label = "权限类型", sort = 7)
    private Integer permsType;

    @AuditDescription(label = "api权限", sort = 8)
    private String apiPerms;

    @AuditDescription(label = "web权限", sort = 9)
    private String webPerms;

    @AuditDescription(label = "图标", sort = 10)
    private String icon;

    @AuditDescription(label = "是否为上下文菜单", sort = 11)
    private String contextMenuId;

    @AuditDescription(label = "是否为frame", sort = 12)
    private String frameFlag;

    @AuditDescription(label = "frame地址", sort = 13)
    private String frameUrl;

    @AuditDescription(label = "是否缓存", sort = 14)
    private String cacheFlag;

    @AuditDescription(label = "是否可见", sort = 15)
    private String visibleFlag;
}
