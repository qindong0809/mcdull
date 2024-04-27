package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class MenuVO implements VO {

    private String menuName;

    /**
     * {@link io.gitee.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum}
     */
    private Integer menuType;

    private Long parentId;

    private Integer sort;

    private String path;

    private String component;

    private String permsType;

    private String apiPerms;

    private String webPerms;

    private String icon;

    private Long contextMenuId;

    private Boolean frameFlag;

    private String frameUrl;

    private Boolean cacheFlag;

    private Boolean visibleFlag;
}
