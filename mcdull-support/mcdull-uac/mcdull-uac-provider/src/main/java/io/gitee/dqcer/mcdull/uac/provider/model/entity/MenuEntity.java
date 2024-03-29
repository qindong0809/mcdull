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
public class MenuEntity extends BaseEntity {


    private Integer menuType;
    private Integer parentId;
    private String title;
    private String name;
    private String path;
    private String component;
    private Integer rankOrder;
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
