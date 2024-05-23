package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import com.google.common.collect.Lists;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 缓存服务
 *
 * @author dqcer
 * @since 2024/05/23
 */
@Slf4j
@Service
public class CacheServiceImpl implements ICacheService {

    @Resource
    private CacheManager cacheManager;

    @Override
    public List<String> cacheNames() {
        return Lists.newArrayList(cacheManager.getCacheNames());
    }

    @Override
    public List<String> cacheKey(String cacheName) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
        if (cache == null) {
            return Lists.newArrayList();
        }
        Set<Object> cacheKey = cache.getNativeCache().asMap().keySet();
        return cacheKey.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public void removeCache(String cacheName) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
