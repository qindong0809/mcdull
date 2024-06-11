package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedisClient;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CacheQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
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
    private CaffeineCacheManager cacheManager;

    @Resource
    private RedisClient redisClient;

    @Override
    public PagedVO<KeyValueVO<String, String>> cacheNames(CacheQueryDTO dto) {
        List<KeyValueVO<String, String>> cacheNameList = new ArrayList<>();
        Cache caffeineCache = cacheManager.getCache(GlobalConstant.CAFFEINE_CACHE);
        if (caffeineCache instanceof CaffeineCache) {
            CaffeineCache cache = (CaffeineCache) caffeineCache;
            Set<Object> keySet = cache.getNativeCache().asMap().keySet();
            if (CollUtil.isNotEmpty(keySet)) {
                for (Object objKey : keySet) {
                    cacheNameList.add(new KeyValueVO<>(Convert.toStr(objKey), "caffeine"));
                }
            }
        }
        Collection<String> redisCacheList = redisClient.getAllKey();
        if (CollUtil.isNotEmpty(redisCacheList)) {
            for (String key : redisCacheList) {
                cacheNameList.add(new KeyValueVO<>(key, "redis"));
            }
        }

        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            keyword = keyword.trim();
            String finalKeyword = keyword;
            if (keyword.contains("*")) {
                AntPathMatcher matcher = new AntPathMatcher();
                cacheNameList = cacheNameList.stream().filter(item -> matcher.match(finalKeyword, item.getKey()))
                        .collect(Collectors.toList());
            } else {
                cacheNameList = cacheNameList.stream()
                        .filter(item -> item.getKey().toLowerCase().contains(finalKeyword.toLowerCase()))
                        .collect(Collectors.toList());
            }

        }
        return PageUtil.ofSub(cacheNameList, dto);
    }

    @Override
    public Object cacheKey(Boolean caffeineCacheFlag, String cacheName) {
        if (BooleanUtil.isTrue(caffeineCacheFlag)) {
            CaffeineCache cache = (CaffeineCache) cacheManager.getCache(GlobalConstant.CAFFEINE_CACHE);
            if (cache != null) {
                Cache.ValueWrapper wrapper = cache.get(cacheName);
                if (wrapper != null) {
                    return wrapper.get();
                }
            }
            return null;
        }
        return redisClient.get(cacheName);
    }

    @Override
    public void removeCache(Boolean caffeineCacheFlag, String cacheName) {
        if (BooleanUtil.isTrue(caffeineCacheFlag)) {
            CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
            return;
        }
        redisClient.delete(cacheName);

    }
}
