package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelDO;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 系统菜单实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_menu")
public class MenuDO extends BaseDO {


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
     * @see io.gitee.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum
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
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return new StringJoiner(", ", MenuDO.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("parentId=" + parentId)
                .add("orderNum=" + orderNum)
                .add("path='" + path + "'")
                .add("component='" + component + "'")
                .add("query='" + query + "'")
                .add("isFrame='" + isFrame + "'")
                .add("isCache='" + isCache + "'")
                .add("menuType='" + menuType + "'")
                .add("visible='" + visible + "'")
                .add("status='" + status + "'")
                .add("perms='" + perms + "'")
                .add("icon='" + icon + "'")
                .add("createdBy=" + createdBy)
                .add("updatedTime=" + updatedTime)
                .add("updatedBy=" + updatedBy)
                .add("remark='" + remark + "'")
                .add("createdTime=" + createdTime)
                .add("id=" + id)
                .toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
