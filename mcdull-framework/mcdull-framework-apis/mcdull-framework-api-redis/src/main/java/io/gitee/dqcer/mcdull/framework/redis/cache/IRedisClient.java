package io.gitee.dqcer.mcdull.framework.redis.cache;

import java.util.Set;

/**
 * Redis 基础操作契约。
 * <p>
 * 仅暴露业务代码实际使用的方法；完整实现与 Redisson/Spring Data Redis 依赖
 * 留在 mcdull-framework-starter-redis 中。
 */
public interface IRedisClient {

    /**
     * 查询全部 key。
     *
     * @return key 集合
     */
    Set<String> getAllKey();

    /**
     * 写入并设置过期时间。
     *
     * @param key     缓存 key
     * @param value   值
     * @param seconds 过期时间（秒）
     */
    void set(String key, Object value, int seconds);

    /**
     * 读取值。
     *
     * @param key 缓存 key
     * @param <T> 值泛型
     * @return 缓存值
     */
    <T> T get(String key);

    /**
     * 删除缓存。
     *
     * @param key 缓存 key
     */
    void delete(String... key);
}
