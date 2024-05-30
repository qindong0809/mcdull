package io.gitee.dqcer.mcdull.framework.redis.operation;


import io.gitee.dqcer.mcdull.framework.redis.ICache;

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

    @Resource
    private RedissonCache redisCache;


    @Override
    public <T> T get(String key, Class<T> type) {
        T t = caffeineCache.get(key, type);
        if (null != t) {
            return t;
        }
//        boolean local = SpringContextHolder.isLocal();
//        if (local) {
//            return null;
//        }
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
        redisCache.put(key, value, expire);
    }

    @Override
    public void evict(String... keys) {
        caffeineCache.evict(keys);
        redisCache.evict(keys);
    }
}
