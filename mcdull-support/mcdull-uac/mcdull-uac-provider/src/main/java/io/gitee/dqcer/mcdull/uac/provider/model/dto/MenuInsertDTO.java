package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Getter
@Setter
@ToString
public class MenuInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;


    private String menuName;

    /**
     * {@link io.gitee.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum}
     */
    private Integer menuType;

    private Integer parentId;

    private Integer sort;

    private String path;

    private String component;

    private String permsType;

    private String apiPerms;

    private String webPerms;

    private String icon;

    private Integer contextMenuId;

    private Boolean frameFlag;

    private String frameUrl;

    private Boolean cacheFlag;

    private Boolean visibleFlag;

   
}