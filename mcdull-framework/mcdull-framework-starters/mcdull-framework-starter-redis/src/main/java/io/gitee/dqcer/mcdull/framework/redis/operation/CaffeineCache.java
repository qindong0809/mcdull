package io.gitee.dqcer.mcdull.framework.redis.operation;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.gitee.dqcer.mcdull.framework.redis.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine缓存
 *
 * @author dqcer
 * @since  22:21 2021/4/28
 */
@Component
public class CaffeineCache implements ICache {

    protected static final Logger log = LoggerFactory.getLogger(CaffeineCache.class);

    private final Cache<String, Object> cache;

    public CaffeineCache() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        Caffeine<Object, Object> builder = caffeine.maximumSize(10000);
        builder.initialCapacity(3000);
        //  基于引用回收
        //  软引用：如果一个对象只具有软引用，则内存空间足够，垃圾回收器就不会回收它；如果内存空间不足了，就会回收这些对象的内存。
        //  弱引用：弱引用的对象拥有更短暂的生命周期。在垃圾回收器线程扫描它所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存
        builder.softValues();
        // 到期时间默认10s
        int expire = 3;
        //  回收策略 每写入一次重新计算一次缓存的有效时间
        builder.expireAfterWrite(expire, TimeUnit.SECONDS);

        cache = caffeine.build();
    }

    public static Cache<Object, Object> tokenCache = Caffeine.newBuilder()
            // 初始的缓存空间大小
            .initialCapacity(5000)
            // 缓存的最大条数
            .maximumSize(1000)
            .expireAfterWrite(60 * 60 * 24 * 7, TimeUnit.SECONDS)
            .recordStats().build();
            //设置缓存的移除通知;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object o = cache.getIfPresent(key);
        if (null != o) {
            if (!type.isInstance(o)) {
                throw new IllegalArgumentException("缓存值的类型不能是" + type.getName());
            }
            if (log.isDebugEnabled()) {
                log.debug("caffeine缓存 key={} 缓存已命中", key);
            }
            return (T) o;
        }
         o = tokenCache.getIfPresent(key);
        if (null != o) {
            if (!type.isInstance(o)) {
                throw new IllegalArgumentException("缓存值的类型不能是" + type.getName());
            }
            if (log.isDebugEnabled()) {
                log.debug("caffeine缓存 key={} 缓存已命中", key);
            }
            return (T) o;
        }
        return null;
    }

    @Override
    public <T> void put(String key, T value, long expire) {
        if (log.isDebugEnabled()) {
            log.debug("caffeine缓存 key={} 缓存已存入", key);
        }
        if (expire == 0) {
            cache.put(key, value);
            return;
        }
        tokenCache.put(key, value);
    }

    @Override
    public void evict(String... keys) {
        if (log.isDebugEnabled()) {
            for (String key : keys) {
                log.debug("caffeine缓存 key={} 缓存已删除", key);
            }
        }
        cache.invalidateAll(Arrays.asList(keys));

        tokenCache.invalidateAll(Arrays.asList(keys));
    }
}
