package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class MenuUpdateDTO implements DTO {

    private static final long serialVersionUID = 1L;

    private Integer menuType;
    @NotNull
    private Integer parentId;
    private String title;
    private String name;
    private String path;
    private String component;
    @NotNull
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