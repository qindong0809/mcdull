package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelDO;

import java.util.Date;

/**
 * 系统角色实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_menu")
public class MenuDO extends RelDO {

    /**
     * 名字
     */
    private String name;

    /**
     * 父id
     */
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

    @Override
    public String toString() {
        return "MenuDO{" +
                "name='" + name + '\'' +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", query='" + query + '\'' +
                ", isFrame='" + isFrame + '\'' +
                ", isCache='" + isCache + '\'' +
                ", menuType='" + menuType + '\'' +
                ", visible='" + visible + '\'' +
                ", status=" + status +
                ", perms='" + perms + '\'' +
                ", icon='" + icon + '\'' +
                ", createdBy=" + createdBy +
                ", updatedTime=" + updatedTime +
                ", updatedBy=" + updatedBy +
                ", remark='" + remark + '\'' +
                ", createdTime=" + createdTime +
                ", id=" + id +
                "} " + super.toString();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIsFrame() {
        return isFrame;
    }

    public void setIsFrame(String isFrame) {
        this.isFrame = isFrame;
    }

    public String getIsCache() {
        return isCache;
    }

    public void setIsCache(String isCache) {
        this.isCache = isCache;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
