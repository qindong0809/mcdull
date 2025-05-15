package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统角色菜单实体
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_role_menu")
@Getter
@Setter
public class RoleMenuEntity extends RelEntity<Integer> {

    private Integer menuId;

    private Integer roleId;

}
