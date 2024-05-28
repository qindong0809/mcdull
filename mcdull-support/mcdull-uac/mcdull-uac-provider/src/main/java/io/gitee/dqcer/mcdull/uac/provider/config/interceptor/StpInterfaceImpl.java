package io.gitee.dqcer.mcdull.uac.provider.config.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.security.AbstractUserDetailsService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
        // 不能用 UserContextH... 因为还没有初始化
        Boolean administratorFlag = StpUtil.getSession().get(GlobalConstant.ADMINISTRATOR_FLAG, false);

        if (administratorFlag) {
            return ListUtil.of("*:*:*");
        }
        Integer id = Convert.toInt(userId);
        return loginService.getPermissionList(id);
    }

    @Override
    protected List<String> roleList(Object userId) {
        Integer id = Convert.toInt(userId);
        return loginService.getRoleList(id);
    }

}