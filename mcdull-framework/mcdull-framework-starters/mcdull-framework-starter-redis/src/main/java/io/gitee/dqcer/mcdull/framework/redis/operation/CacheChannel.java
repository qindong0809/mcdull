package io.gitee.dqcer.mcdull.framework.redis.operation;


import io.gitee.dqcer.mcdull.framework.redis.ICache;
import io.gitee.dqcer.mcdull.framework.redis.config.SpringContextHolder;

import javax.annotation.Resource;


/**
 * 缓存通道
 *
 * @author dqcer
 * @since 22:21 2021/4/28
 */
public class CacheChannel implements ICache {

    @Resource
    private CaffeineCache caffeineCache;


    @Override
    public <T> T get(String key, Class<T> type) {
        T t = caffeineCache.get(key, type);
        if (null != t) {
            return t;
        }
        boolean local = SpringContextHolder.isLocal();
        if (local) {
            return null;
        }
        RedissonCache redisCache = SpringContextHolder.getBean(RedissonCache.class);
        t = redisCache.get(key, type);
        if (t == null) {
            return null;
        }
        caffeineCache.put(key, t, 0);
        return t;
    }

    @Override
    public <T> void put(String key, T value, long expire) {
        caffeineCache.put(key, value, expire);
        boolean local = SpringContextHolder.isLocal();
        if (local) {
            return;
        }
        RedissonCache redisCache = SpringContextHolder.getBean(RedissonCache.class);
        redisCache.put(key, value, expire);
    }

    @Override
    public void evict(String... keys) {
        caffeineCache.evict(keys);
        boolean local = SpringContextHolder.isLocal();
        if (local) {
            return;
        }
        RedissonCache redisCache = SpringContextHolder.getBean(RedissonCache.class);
        redisCache.evict(keys);
    }
}
