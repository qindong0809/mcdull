package io.gitee.dqcer.mcdull.system.provider.config.interceptor;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.security.AbstractUserDetailsService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ILoginService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dqcer
 */
@Component
public class StpInterfaceImpl extends AbstractUserDetailsService {

    @Resource
    private ILoginService loginService;

    @Override
    protected List<String> permissionList(Object userId) {
        Integer id = Convert.toInt(userId);
        return loginService.getPermissionList(id);
    }

    @Override
    protected List<String> roleList(Object userId) {
        Integer id = Convert.toInt(userId);
        return loginService.getRoleList(id);
    }

}
