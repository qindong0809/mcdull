package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.framework.redis.cache.ICache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TenantCacheEvictManager {

    @Resource
    private ICache cache;

    public void clearTenant(Integer tenantId, Collection<Integer> userIds) {
        if (tenantId != null) {
            cache.delete("tenant:ds:" + tenantId, "tenant:init:" + tenantId);
        }
        if (userIds != null) {
            userIds.forEach(userId -> cache.delete("tenant:user:" + userId));
        }
    }

    public void clearUser(Integer userId) {
        if (userId != null) {
            cache.delete("tenant:user:" + userId);
        }
    }

    public void clearTask(String taskNo) {
        if (taskNo != null) {
            cache.delete("tenant:init:" + taskNo);
        }
    }
}
