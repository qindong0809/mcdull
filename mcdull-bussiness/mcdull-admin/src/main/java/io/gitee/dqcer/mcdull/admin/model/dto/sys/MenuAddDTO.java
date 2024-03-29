package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 资源 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class MenuAddDTO implements DTO {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    @NotBlank
    private String name;

    /**
     * 状态（1/正常 2/停用）
     */
    @NotNull
    @EnumsStrValid(required = true, value = StatusEnum.class)
    private String status;

    /**
     * 父id
     */
    @NotNull
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 是否为外链（0是 1否）
     */
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    private String isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     * @see io.gitee.dqcer.mcdull.admin.model.enums.MenuTypeEnum
     */
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private String visible;


    /**
     * 权限标识 如sys:user:list
     */
    private String perms;

    /**
     * 图标
     */
    private String icon;


    /**
     * 备注
     */
    private String remark;

}