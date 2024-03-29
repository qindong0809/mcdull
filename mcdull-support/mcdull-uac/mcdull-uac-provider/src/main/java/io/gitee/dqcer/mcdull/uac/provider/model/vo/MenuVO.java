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



    /**
     * 主键
     */
    private Integer id;

    /**
     * 0代表菜单、1代表iframe、2代表外链、3代表按钮
     */
    private Integer menuType;
    private Integer parentId;
    private String title;
    private String name;
    private String path;
    private String component;
    private Integer rank;
    private String redirect;
    private String icon;
    private String extraIcon;
    private String enterTransition;
    private String leaveTransition;
    private String activePath;
    private String auths;
    private String frameSrc;
    private Boolean frameLoading;
    private Boolean keepAlive;
    private Boolean hiddenTag;
    private Boolean showLink;
    private Boolean showParent;

}
