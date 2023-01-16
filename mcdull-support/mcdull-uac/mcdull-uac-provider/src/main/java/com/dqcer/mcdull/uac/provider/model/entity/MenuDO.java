package com.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dqcer.framework.base.entity.BaseDO;

/**
 * 系统角色实体
 *
 * @author dqcer
 * @version 2022/11/07
 */
@TableName("sys_menu")
public class MenuDO extends BaseDO {


    /**
     * 父id
     */
    private Long parentId;

    /**
     * 名字
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 模块code 如sys:user:list
     */
    private String resCode;
    /**
     * 路径
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * 类型 (menu/菜单、button/按钮)
     */
    private String type;

    @Override
    public String toString() {
        return "MenuDO{" +
                "parentId=" + parentId +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", sort=" + sort +
                ", resCode='" + resCode + '\'' +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", type='" + type + '\'' +
                ", delBy=" + delBy +
                ", createdBy=" + createdBy +
                ", updatedTime=" + updatedTime +
                ", updatedBy=" + updatedBy +
                ", createdTime=" + createdTime +
                ", delFlag=" + delFlag +
                ", id=" + id +
                "} " + super.toString();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
