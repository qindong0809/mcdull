package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;

import java.util.Date;

/**
 * 系统角色菜单实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_role_menu")
public class RoleMenuEntity extends IdEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER )
    private Date createdTime;

    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 角色id
     */
    private Integer roleId;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RoleMenuDO{");
        sb.append("createdTime=").append(createdTime);
        sb.append(", menuId=").append(menuId);
        sb.append(", roleId=").append(roleId);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public RoleMenuEntity setMenuId(Integer menuId) {
        this.menuId = menuId;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
