package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.List;

@Data
public class AdminMenuVO implements VO {

    private Integer id;
    private Integer parentId;

    private String title;
    /** 1=目录 2=菜单 */
    private Integer type;

    private String path;
    private String name;
    private String component;
    private String icon;

    private Boolean isExternal = false;
    private Boolean isCache = false;
    private Boolean isHidden = false;
    private Integer sort;

    /** 权限标识，如 system:notice:view */
    private String permission;
    /** 子菜单，递归 */
    private List<AdminMenuVO> children;
}
