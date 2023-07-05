package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

import java.util.Date;

/**
 * 部门视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class MenuVO implements VO {

    private Integer id;


    /**
     * 名字
     */
    private String name;

    /**
     * 父id
     */
    private Integer parentId;

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
     * 状态（1/正常 2/停用）
     * @see io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum
     */
    private String status;

    /**
     * 权限标识 如sys:user:list
     */
    private String perms;

    /**
     * 图标
     */
    private String icon;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    /**
     * 备注
     */
    private String remark;

}
