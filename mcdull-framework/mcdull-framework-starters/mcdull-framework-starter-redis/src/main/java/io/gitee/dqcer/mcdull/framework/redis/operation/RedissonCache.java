package io.gitee.dqcer.mcdull.framework.redis.operation;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.redis.cache.ICache;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * redisson对象
 *
 * @author dqcer
 * @since 2021/09/10
 */
@Component
//@ConditionalOnProperty
public class RedissonCache implements ICache {

    protected static final Logger log = LoggerFactory.getLogger(RedissonCache.class);

    /**
     * 回源加锁的最大等待时间（秒）。超时则直接回源，避免缓存失效时把请求全部堵在锁上。
     */
    private static final long LOAD_LOCK_WAIT_SECONDS = 10;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private McdullProperties mcdullProperties;


    private String prefix() {
        return mcdullProperties.getApplicationName() + ":";
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        key = prefix() + key;
        Object o = redissonClient.getBucket(key).get();
        if (null != o) {
            if (!type.isInstance(o)) {
                throw new IllegalArgumentException("缓存值的类型不能是" + type.getName());
            }
            if (log.isDebugEnabled()) {
                log.debug("redis缓存 key={} 缓存已命中", key);
            }
            return (T) o;
        }
        return null;
    }

    @Override
    public <T> void put(String key, T value, long expire) {
        key = prefix() + key;
        if (log.isDebugEnabled()) {
            log.debug("redis缓存 key={} 缓存已存入", key);
        }
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, expire, TimeUnit.SECONDS);
    }

    @Override
    public <T> T getOrSet(String key, Class<T> type, Supplier<T> supplier, long expire) {
        return loadWithLock(key, () -> this.get(key, type), supplier, value -> this.put(key, value, expire));
    }

    @Override
    public <T> List<T> getListOrSet(String key, Class<T> type, Supplier<List<T>> supplier, long expire) {
        // Jackson 的 defaultTyping 已保留元素类型，无需再按 type 手动反序列化
        @SuppressWarnings("unchecked")
        Supplier<List<T>> reader = () -> (List<T>) this.get(key, Object.class);
        return loadWithLock(key, reader, supplier, value -> {
            LogHelp.info(log, "redis缓存 key={} 回源写入", key);
            this.put(key, value, expire);
        });
    }

    /**
     * 缓存击穿防护：未命中时加分布式锁，仅一个线程回源，其余线程在锁内二次读缓存。
     * 锁等待超时或被中断时降级为直接回源（不写缓存），保证可用性。
     */
    private <T> T loadWithLock(String key, Supplier<T> reader, Supplier<T> loader, Consumer<T> writer) {
        T value = reader.get();
        if (value != null) {
            return value;
        }

        String lockKey = prefix() + "cache:load:lock:" + key;
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(LOAD_LOCK_WAIT_SECONDS, -1, TimeUnit.SECONDS);
            if (!locked) {
                LogHelp.warn(log, "redis缓存 key={} 回源锁等待超时，降级直接回源", key);
                return loader.get();
            }
            value = reader.get();
            if (value != null) {
                return value;
            }
            value = loader.get();
            if (value != null) {
                writer.accept(value);
            }
            return value;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogHelp.warn(log, "redis缓存 key={} 回源被中断，降级直接回源", key);
            return loader.get();
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    @Override
    public <T> void putIfExists(String key, T value) {
        key = prefix() + key;
        if (log.isDebugEnabled()) {
            log.debug("redis缓存 key={} 缓存已存入", key);
        }
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.setIfExists(value);
    }

    @Override
    public void evict(String... keys) {
        for (String key : keys) {
            key = prefix() + key;
            if (log.isDebugEnabled()) {
                log.debug("redis缓存 key={} 缓存已删除", key);
            }

            RBucket<Object> bucket = redissonClient.getBucket(key);
            bucket.delete();
        }
    }
}

