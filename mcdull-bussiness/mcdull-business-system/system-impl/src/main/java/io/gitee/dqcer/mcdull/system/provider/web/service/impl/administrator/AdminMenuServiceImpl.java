package io.gitee.dqcer.mcdull.system.provider.web.service.impl.administrator;

import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.PermissionBO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.MenuInfoBO;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminMenuService;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "administration.account")
public class AdminMenuServiceImpl implements IAdminMenuService {

    private List<MenuInfoBO> menu;

    private List<PermissionBO> permission;


    @Override
    public List<MenuInfoBO> menuList() {
        return menu;
    }

    @Override
    public List<PermissionBO> permissionList() {
        return permission;
    }
}
