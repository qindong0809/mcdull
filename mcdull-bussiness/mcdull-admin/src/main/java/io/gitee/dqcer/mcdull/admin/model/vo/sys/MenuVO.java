package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;

import java.util.Date;

/**
 * 部门视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
public class MenuVO implements VO {

    private Integer menuId;


    /**
     * 名字
     */
    private String menuName;

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

    public Date getCreatedTime() {
        return createdTime;
    }

    public MenuVO setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }


    public String getMenuName() {
        return menuName;
    }

    public MenuVO setMenuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public MenuVO setMenuId(Integer menuId) {
        this.menuId = menuId;
        return this;
    }

    public Integer getParentId() {
        return parentId;
    }

    public MenuVO setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public MenuVO setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public String getPath() {
        return path;
    }

    public MenuVO setPath(String path) {
        this.path = path;
        return this;
    }

    public String getComponent() {
        return component;
    }

    public MenuVO setComponent(String component) {
        this.component = component;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public MenuVO setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getIsFrame() {
        return isFrame;
    }

    public MenuVO setIsFrame(String isFrame) {
        this.isFrame = isFrame;
        return this;
    }

    public String getIsCache() {
        return isCache;
    }

    public MenuVO setIsCache(String isCache) {
        this.isCache = isCache;
        return this;
    }

    public String getMenuType() {
        return menuType;
    }

    public MenuVO setMenuType(String menuType) {
        this.menuType = menuType;
        return this;
    }

    public String getVisible() {
        return visible;
    }

    public MenuVO setVisible(String visible) {
        this.visible = visible;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public MenuVO setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getPerms() {
        return perms;
    }

    public MenuVO setPerms(String perms) {
        this.perms = perms;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public MenuVO setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public MenuVO setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public MenuVO setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public MenuVO setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MenuVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
