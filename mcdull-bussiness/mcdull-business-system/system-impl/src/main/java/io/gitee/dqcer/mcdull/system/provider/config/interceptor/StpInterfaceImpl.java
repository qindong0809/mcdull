package io.gitee.dqcer.mcdull.system.provider.config.interceptor;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
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
    @Resource
    private RedissonCache redissonCache;

    @Override
    protected List<String> permissionList(Object userId) {
        Integer id = Convert.toInt(userId);
        String cacheKeyFormat = StrUtil.format( "current:user:{}:permission:list", id);
        return redissonCache.getListOrSet(cacheKeyFormat, String.class, () -> loginService.getPermissionList(id), 60);
    }

    @Override
    protected List<String> roleList(Object userId) {
        Integer id = Convert.toInt(userId);
        String cacheKeyFormat = StrUtil.format( "current:user:{}:role:list", id);
        return redissonCache.getListOrSet(cacheKeyFormat, String.class, () -> loginService.getRoleList(id), 60);
    }

}
