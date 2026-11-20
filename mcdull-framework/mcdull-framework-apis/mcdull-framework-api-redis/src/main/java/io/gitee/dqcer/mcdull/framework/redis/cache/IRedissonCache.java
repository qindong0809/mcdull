package io.gitee.dqcer.mcdull.framework.redis.cache;

import java.util.List;
import java.util.function.Supplier;

/**
 * Redis 对象缓存契约。
 * <p>
 * 业务代码只依赖该接口；Redisson 客户端、序列化与配置前缀等运行时细节
 * 由 mcdull-framework-starter-redis 中的 RedissonCache 实现。
 */
public interface IRedissonCache {

    /**
     * 读取缓存。
     *
     * @param key  缓存 key
     * @param type 值类型
     * @param <T>  值泛型
     * @return 缓存值，不存在返回 null
     */
    <T> T get(String key, Class<T> type);

    /**
     * 写入缓存。
     *
     * @param key    缓存 key
     * @param value  值
     * @param expire 过期时间（秒）
     * @param <T>    值泛型
     */
    <T> void put(String key, T value, long expire);

    /**
     * 读缓存，未命中时回源并回填。
     *
     * @param key      缓存 key
     * @param type     值类型
     * @param supplier 回源逻辑
     * @param expire   过期时间（秒）
     * @param <T>      值泛型
     * @return 缓存值
     */
    <T> T getOrSet(String key, Class<T> type, Supplier<T> supplier, long expire);

    /**
     * 读列表缓存，未命中时回源并回填。
     *
     * @param key      缓存 key
     * @param type     列表元素类型
     * @param supplier 回源逻辑
     * @param expire   过期时间（秒）
     * @param <T>      列表元素泛型
     * @return 缓存列表
     */
    <T> List<T> getListOrSet(String key, Class<T> type, Supplier<List<T>> supplier, long expire);

    /**
     * 仅当 key 已存在时更新值。
     *
     * @param key   缓存 key
     * @param value 值
     * @param <T>   值泛型
     */
    <T> void putIfExists(String key, T value);

    /**
     * 删除缓存。
     *
     * @param keys 缓存 key
     */
    void evict(String... keys);
}
