package io.gitee.dqcer.mcdull.framework.redis.cache;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 缓存契约：对象缓存 + 常用 KV 操作。
 * <p>
 * 业务代码只依赖本接口；Redis 客户端、序列化与 key 前缀等运行时细节
 * 由 mcdull-framework-starter-redis 中的 RedissonCache 实现。
 * <p>
 * 两类方法的 key 语义不同：
 * <ul>
 *     <li>对象缓存方法（*Prefixed 后缀）由实现自动加应用名前缀；</li>
 *     <li>KV 方法（get/set/delete/getAllKey/keysByPrefix 等无后缀方法）直接操作原始 key，不做前缀改写；</li>
 *     <li>带 Prefixed 后缀的是特例，由实现自动加应用名前缀。</li>
 * </ul>
 */
public interface ICache {

    // ============ 对象缓存（方法名带 Prefixed 后缀，key 自动加应用名前缀）============

    /**
     * 读取缓存。
     *
     * @param key  缓存 key
     * @param type 值类型
     * @param <T>  值泛型
     * @return 缓存值，不存在返回 null
     */
    <T> T getPrefixed(String key, Class<T> type);

    /**
     * 写入缓存。
     *
     * @param key    缓存 key
     * @param value  值
     * @param expire 过期时间（秒）
     * @param <T>    值泛型
     */
    <T> void putPrefixed(String key, T value, long expire);

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
    <T> T getOrSetPrefixed(String key, Class<T> type, Supplier<T> supplier, long expire);

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
    <T> List<T> getListOrSetPrefixed(String key, Class<T> type, Supplier<List<T>> supplier, long expire);

    /**
     * 读列表缓存，未命中时回源并回填，可控制是否缓存空结果。
     * <p>
     * 默认方法 {@link #getListOrSetPrefixed(String, Class, Supplier, long)} 等价于 cacheNull=true。
     * 对于权限、角色等「空结果必须尽快对新数据可见」的场景，应传 false 关闭空值缓存，
     * 避免短暂的数据延迟（如刚授予角色后仍被判定无权限）。
     *
     * @param key       缓存 key
     * @param type      列表元素类型
     * @param supplier  回源逻辑
     * @param expire    过期时间（秒）
     * @param cacheNull 回源结果为空时是否写入空值哨兵（哨兵 TTL 固定为短周期，不使用 expire）
     * @param <T>       列表元素泛型
     * @return 缓存列表
     */
    <T> List<T> getListOrSetPrefixed(String key, Class<T> type, Supplier<List<T>> supplier,
                                     long expire, boolean cacheNull);

    /**
     * 仅当 key 已存在时更新值。
     *
     * @param key   缓存 key
     * @param value 值
     * @param <T>   值泛型
     */
    <T> void putIfExistsPrefixed(String key, T value);

    /**
     * 删除缓存。
     *
     * @param keys 缓存 key
     */
    void evictPrefixed(String... keys);

    // ============ 常用 KV 操作（无后缀 = 直接操作原始 key，不加前缀）============

    /**
     * 写入键值并设置过期时间。
     *
     * @param key     原始 key
     * @param value   值
     * @param seconds 过期时间（秒），小于等于 0 表示永不过期
     */
    void set(String key, Object value, long seconds);

    /**
     * 读取键值。
     *
     * @param key 原始 key
     * @param <T> 值泛型
     * @return 缓存值，不存在返回 null
     */
    <T> T get(String key);

    /**
     * 删除键值。
     *
     * @param keys 原始 key，可多个
     */
    void delete(String... keys);

    /**
     * 查询全部 key。
     *
     * @return key 集合
     */
    Set<String> getAllKey();

    /**
     * 按前缀查询 key。
     *
     * @param prefix key 前缀
     * @return key 集合
     */
    Set<String> keysByPrefix(String prefix);

    /**
     * 判断 key 是否存在。
     *
     * @param key 原始 key
     * @return true 存在
     */
    boolean exists(String key);

    /**
     * 设置 key 的过期时间。
     *
     * @param key     原始 key
     * @param seconds 过期时间（秒）
     * @return true 设置成功
     */
    boolean expire(String key, long seconds);

    /**
     * 当 key 不存在时写入。
     *
     * @param key     原始 key
     * @param value   值
     * @param seconds 过期时间（秒）
     * @return true 写入成功
     */
    boolean setIfAbsent(String key, Object value, long seconds);

    /**
     * 自增 1。
     *
     * @param key 原始 key
     * @return 自增后的值
     */
    long increment(String key);

    /**
     * 按指定步长自增。
     *
     * @param key   原始 key
     * @param delta 步长
     * @return 自增后的值
     */
    long increment(String key, long delta);
}
