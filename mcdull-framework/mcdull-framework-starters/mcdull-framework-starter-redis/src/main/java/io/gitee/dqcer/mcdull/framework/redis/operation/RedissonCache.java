package io.gitee.dqcer.mcdull.framework.redis.operation;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.redis.ICache;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
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

    public <T> T getOrSet(String key, Class<T> type, Supplier<T> supplier, long expire) {
        String jsonStr = this.get(key, String.class);
        if (StrUtil.isNotBlank(jsonStr)) {
            return JSONUtil.toBean(jsonStr, type);
        }
        T t = supplier.get();
        this.put(key, t, expire);
        return t;
    }

    public <T> List<T> getListOrSet(String key, Class<T> type, Supplier<List<T>> supplier, long expire) {
        String jsonStr = this.get(key, String.class);
        if (CharSequenceUtil.isNotBlank(jsonStr)) {
            LogHelp.info(log, "redis缓存 key={} 缓存已命中 value:{}", key, jsonStr);
            System.out.println(JSONUtil.isTypeJSON(jsonStr));
            JSONObject jsonArray1 = JSONUtil.parseObj("[*:*:*]");
            JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
            return JSONUtil.toList(jsonArray, type);
//            return JSONUtil.toList(jsonStr, type);
        }
        List<T> t = supplier.get();
        this.put(key, t, expire);
        return t;
    }

    public static void main(String[] args) {
//        JSONArray jsonArray = JSONUtil.parseArray("[\"*:*:*\"]");
        JSONObject jsonArray = JSONUtil.parseObj("[*:*:*]");
        System.out.println(jsonArray);
    }


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

