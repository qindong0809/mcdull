package io.gitee.dqcer.mcdull.system.provider.web.service.administrator;

import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.MenuBO;

import java.util.List;

public interface IAdminMenuService {

    List<MenuBO> getMenuList();

   List<MenuBO> getPermissionList();
}
