package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminRoleEntity;

import java.util.List;

/**
 * 管理端角色只读查询契约。
 *
 * <p>供 AdminUserService 补齐角色展示数据，避免反向注入 IAdminRoleService 形成循环。
 */
public interface IAdminRoleQueryManager {

    List<AdminRoleEntity> listByIds(List<Integer> ids);
}
