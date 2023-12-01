package io.gitee.dqcer.mcdull.uac.provider.config.interceptor;

import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.security.AbstractUserDetailsService;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.UserService;
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
    private UserService userService;

    @Override
    protected List<String> permissionList(Long userId) {
        Result<List<UserPowerVO>> listResult = userService.queryResourceModules(userId);
        if (listResult.isOk()) {
            List<UserPowerVO> list = listResult.getData();
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().flatMap(i -> i.getModules().stream()).distinct().collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
    protected List<String> roleList(Long userId) {
        Result<List<UserPowerVO>> listResult = userService.queryResourceModules(userId);
        if (listResult.isOk()) {
            List<UserPowerVO> list = listResult.getData();
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().map(UserPowerVO::getCode).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

}