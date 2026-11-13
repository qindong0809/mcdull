package io.gitee.dqcer.mcdull.system.provider.web.service.administrator;

import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.PermissionBO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.MenuInfoBO;

import java.util.List;

public interface IAdminMenuService {

    List<MenuInfoBO> menuList();

   List<PermissionBO> permissionList();
}
