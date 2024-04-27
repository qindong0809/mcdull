package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统菜单实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_menu")
public class MenuEntity extends BaseEntity<Long> {

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
