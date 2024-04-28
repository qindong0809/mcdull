package io.gitee.dqcer.mcdull.uac.provider.config.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.security.AbstractUserDetailsService;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
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
    private IUserService userService;

    @Override
    protected List<String> permissionList(Object userId) {
        // FIXME: 2024/4/27 魔法值， 不能用 UserContextH... 因为还没有初始化
        Boolean administratorFlag = StpUtil.getSession().get(GlobalConstant.ADMINISTRATOR_FLAG, false);

        if (administratorFlag) {
            return ListUtil.of("*:*:*");
        }
        List<UserPowerVO> list = userService.getResourceModuleList(Convert.toLong(userId));
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().flatMap(i -> i.getModules().stream()).distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    protected List<String> roleList(Object userId) {
        List<UserPowerVO> list = userService.getResourceModuleList(Convert.toLong(userId));
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(UserPowerVO::getCode).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}