package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
public class MenuEntity extends BaseEntity<Integer> {

    private String menuName;

    /**
     * {@link io.gitee.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum}
     */
    private Integer menuType;

    private Integer parentId;

    private Integer sort;

    private String path;

    private String component;

    private Integer permsType;

    private String apiPerms;

    private String webPerms;

    private String icon;

    private Integer contextMenuId;

    private Boolean frameFlag;

    private String frameUrl;

    private Boolean cacheFlag;

    private Boolean visibleFlag;
}
