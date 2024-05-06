package io.gitee.dqcer.mcdull.uac.provider.config.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.security.AbstractUserDetailsService;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dqcer
 */
@Component
public class StpInterfaceImpl extends AbstractUserDetailsService {

    @Resource
    private ILoginService loginService;

    @Override
    protected List<String> permissionList(Object userId) {
        // FIXME: 2024/4/27 魔法值， 不能用 UserContextH... 因为还没有初始化
        Boolean administratorFlag = StpUtil.getSession().get(GlobalConstant.ADMINISTRATOR_FLAG, false);

        if (administratorFlag) {
            return ListUtil.of("*:*:*");
        }
        Long id = Convert.toLong(userId);
        return loginService.getPermissionList(id);
    }

    @Override
    protected List<String> roleList(Object userId) {
        Long id = Convert.toLong(userId);
        return loginService.getRoleList(id);
    }

}