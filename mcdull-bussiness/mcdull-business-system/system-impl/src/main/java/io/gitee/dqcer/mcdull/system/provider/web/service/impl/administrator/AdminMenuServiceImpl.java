package io.gitee.dqcer.mcdull.system.provider.web.service.impl.administrator;

import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.MenuBO;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminMenuService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@ConfigurationProperties(prefix = "administration")
@Service
public class AdminMenuServiceImpl implements IAdminMenuService {

    private List<MenuBO> menu;

    private List<MenuBO> permission;


    @Override
    public List<MenuBO> getMenuList() {
        return menu;
    }

    @Override
    public List<MenuBO> getPermissionList() {
        return permission;
    }
}
