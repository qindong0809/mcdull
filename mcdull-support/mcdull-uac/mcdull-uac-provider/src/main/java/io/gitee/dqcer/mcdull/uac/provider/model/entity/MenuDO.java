package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.DO;

/**
 * 系统菜单实体
 *
 * @author dqcer
 * @version 2022/11/07
 */
@TableName("sys_menu")
public class MenuDO implements DO {


    /**
     * code 如sys:user:list
     */
    private String code;

    /**
     * 父 code
     */
    private String parentCode;

    /**
     * 名字
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 路径
     */
    private String path;

    /**
     * 类型 (menu/菜单、button/按钮)
     * @see io.gitee.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum
     */
    private String type;

    /**
     * 状态
     * @see io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum
     */
    private Integer status;

    @Override
    public String toString() {
        return "MenuDO{" +
                "code='" + code + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }

    public String getCode() {
        return code;
    }

    public MenuDO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getParentCode() {
        return parentCode;
    }

    public MenuDO setParentCode(String parentCode) {
        this.parentCode = parentCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuDO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getSort() {
        return sort;
    }

    public MenuDO setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public String getPath() {
        return path;
    }

    public MenuDO setPath(String path) {
        this.path = path;
        return this;
    }

    public String getType() {
        return type;
    }

    public MenuDO setType(String type) {
        this.type = type;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MenuDO setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
