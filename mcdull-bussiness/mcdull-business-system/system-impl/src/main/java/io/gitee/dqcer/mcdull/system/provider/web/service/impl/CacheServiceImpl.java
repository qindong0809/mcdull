package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedisClient;
import io.gitee.dqcer.mcdull.framework.web.basic.GenericLogic;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CacheQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICacheService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 缓存服务
 *
 * @author dqcer
 * @since 2024/05/23
 */
@Slf4j
@Service
public class CacheServiceImpl extends GenericLogic implements ICacheService {

    @Resource
    private CaffeineCacheManager cacheManager;
    @Resource
    private RedisClient redisClient;
    @Resource
    private ICommonManager commonManager;

    @Override
    public PagedVO<KeyValueVO<String, String>> queryPage(CacheQueryDTO dto) {
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
                if (!key.startsWith(GlobalConstant.RATE_LIMITER) && !key.startsWith("{" + GlobalConstant.RATE_LIMITER)) {
                    cacheNameList.add(new KeyValueVO<>(key, "redis"));
                }
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
    public boolean exportData(CacheQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList());
        return true;
    }

    private List<Pair<String, Func1<KeyValueVO<String, String>, ?>>> getTitleList() {
        return Arrays.asList(
                new Pair<>("缓存名称", KeyValueVO::getKey),
                new Pair<>("缓存值", KeyValueVO::getValue)
        );
    }

    @Override
    public boolean removeCache(Boolean caffeineCacheFlag, String cacheName) {
        LogHelp.warn(log, "removeCache", "cacheName:{}", cacheName);
        if (BooleanUtil.isTrue(caffeineCacheFlag)) {
            CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        } else {
            redisClient.delete(cacheName);
        }
        return true;
    }
}
