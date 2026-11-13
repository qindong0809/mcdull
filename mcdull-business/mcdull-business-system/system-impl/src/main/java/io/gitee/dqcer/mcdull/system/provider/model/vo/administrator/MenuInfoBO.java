package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import io.gitee.dqcer.mcdull.framework.base.support.BO;
import lombok.Data;

import java.util.List;


@Data
public class MenuInfoBO implements BO {
    private Integer id;
    private Integer parentId;

    private String title;
    /** 1=目录 2=菜单 */
    private Integer type;

    private String path;
    private String name;
    private String component;
    private String icon;
    private String redirect;


    private Boolean isExternal;
    private Boolean isCache;
    private Boolean isHidden;
    private Integer sort;

    /** 权限标识，如 system:notice:view */
    private String permission;

    private List<MenuInfoBO> children;
}
