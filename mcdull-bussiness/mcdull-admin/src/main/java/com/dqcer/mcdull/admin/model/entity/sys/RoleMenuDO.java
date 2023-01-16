package io.gitee.dqcer.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.framework.base.entity.IdDO;

import java.util.Date;

/**
 * 系统角色菜单实体
 *
 * @author dqcer
 * @version 2022/11/07
 */
@TableName("sys_role_menu")
public class RoleMenuDO extends IdDO {

    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER )
    private Date createdTime;

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 角色id
     */
    private Long roleId;

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

    public Long getMenuId() {
        return menuId;
    }

    public RoleMenuDO setMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
