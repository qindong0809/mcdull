package io.gitee.dqcer.mcdull.framework.redis.operation;

import cn.hutool.core.collection.IterUtil;
import org.redisson.api.*;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.lang.NonNull;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Redis工具类
 * 包含常用操作及获取锁
 *
 * @author dqcer
 * @since 2022/11/07
 */
@SuppressWarnings("all")
public final class RedisClient {

    /**
     * 使用 RedissonClient 替换 redisTemplate
     */
    @Deprecated
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    /**
     * rateLimit
     *
     * @param key     key
     * @param quantity quantity
     * @param time
     * @return true / false
     */
    public boolean rateLimit(String key, int quantity, int time) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, quantity, time, RateIntervalUnit.SECONDS);
        return rateLimiter.tryAcquire(1);
    }

    /**
     * 指定缓存失效时间
     * (命令EXPIRE)
     *
     * @param key  键
     * @param time 时间（秒）
     * @return true / false
     */
    public Boolean expire(String key, int time) {
        if (time > 0) {
            return redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        return false;
    }

    public Set<String> getAllKey(){
        RKeys keys = redissonClient.getKeys();
        Iterable<String> iterable = keys.getKeysByPattern("*");
        Set<String> result = new HashSet<>();
        if (IterUtil.isNotEmpty(iterable)) {
            for (String s : iterable) {
                result.add(s);
            }
        }
        return result;
    }

    public Set<String> keysByPrefix(String prefix){
        Set<String> result = new HashSet<>();
        if (prefix != null && !"*".equals(prefix)) {
            Set<String> keys = redisTemplate.keys(prefix + "*");
            if (keys != null && keys.size() > 0) {
                for (Object key : keys) {
                    if (key != null) {
                        result.add(key.toString());
                    }
                }
            }
        }
        return result;
    }


    /**
     * 指定缓存失效时间
     * (命令EXPIRE)
     *
     * @param key      键
     * @param time     时间（秒）
     * @param timeUnit 时间单位
     * @return true / false
     */
    public Boolean expire(String key, long time, TimeUnit timeUnit) {
        if (time > 0) {
            return redisTemplate.expire(key, time, timeUnit);
        }
        return false;
    }

    /**
     * 根据 key 获取过期时间
     * (命令TTL)
     *
     * @param key 键
     * @return 秒
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key 是否存在
     * (命令EXISTS)
     *
     * @param key 键
     * @return true / false
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     * 不存在key不会抛异常
     *
     * @param keys 集合
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }


    /**
     * 删除缓存
     * (命令DEL)
     *
     * @param key 键（一个或者多个）
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            RKeys keys = redissonClient.getKeys();
            keys.delete(key);
        }
    }

    /**
     * 按表达式删除缓存
     *
     * @param pattern 表达式
     */
    public void deleteAll(String pattern) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("参数不正确");
        }
        redisTemplate.delete(scan(pattern));
    }

    /**
     * 查询所有满足表达式的key
     * （命令KEYS，实际是利用SCAN命令实现，不会产生阻塞）
     *
     * @param pattern 表达式
     * @return key
     */
    @SuppressWarnings("all")
    @NonNull
    public Set<String> scan(String pattern) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("参数不正确");
        }
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
                    Set<String> keys = new HashSet<>();
                    Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(1000).build());
                    while (cursor.hasNext()) {
                        keys.add(new String(cursor.next()));
                    }
                    return keys;
                }
        );
    }


//    ============================== String ==============================

    /**
     * 放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取缓存（string数据结构）
     * <p>
     * 通过泛型T指定缓存数据类型
     *
     * @param key 键
     * @return 值
     * @see #getMap
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }


    /**
     * 缓存普通键值对，并设置失效时间
     *
     * @param key     键
     * @param value   值
     * @param seconds 时间（秒），如果 time <= 0 则不设置失效时间
     */
    public void set(String key, Object value, int seconds) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 当key不存在时放入键值对
     * 如果已经存在key返回false
     *
     * @param key   键
     * @param value 值
     * @return true/false
     */
    public Boolean setNx(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 当key不存在时放入键值对，并在指定时间后自动删除
     * 如果已经存在key返回false
     *
     * @param key   键
     * @param value 值
     * @return true/false
     */
    public Boolean setNx(String key, Object value, int time) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 递增
     * 如果不存在key将自动创建
     * (命令INCR)
     *
     * @param key       键
     * @param increment 递增大小
     * @return 递增后的值
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> T increment(String key, T increment) {
        return (T) redisTemplate.opsForValue().increment(key, increment.doubleValue());
    }

    /**
     * 递增1
     * 如果不存在key将自动创建
     * (命令INCR)
     *
     * @param key 键
     * @return 递增后的值
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    /**
     * 递减
     * 如果不存在key将自动创建
     * (命令DECR)
     *
     * @param key       键
     * @param decrement 递减大小
     * @return 递减后的值
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> T decrement(String key, T decrement) {
        return (T) redisTemplate.opsForValue().increment(key, -decrement.longValue());
    }

    /**
     * 递减1
     * 如果不存在key将自动创建
     * (命令DECR)
     *
     * @param key 键
     * @return 递减后的值
     */
    public long decrement(String key) {
        return decrement(key, 1L);
    }

//    ============================== Map ==============================

    /**
     * 往指定HashMap中添加一对键值对，key不存在将自动创建
     * (命令HMSET)
     *
     * @param name  HashMap的名字
     * @param key   添加的键
     * @param value 添加的值
     */
    public void putMap(String name, String key, Object value) {
        redisTemplate.opsForHash().put(name, key, value);
    }

    /**
     * 往指定HashMap中添加另一个map
     * (命令HMSET)
     *
     * @param name HashMap的名字
     * @param map  值
     */
    public void putMap(String name, Map<String, ?> map) {
        redisTemplate.opsForHash().putAll(name, map);
    }

    /**
     * 从指定HashMap中获取指定key的值
     * （命令HGET）
     *
     * @param mapName HashMap的名字（no null）
     * @param key     HashMap中的键（no null）
     * @param <T>     根据实际类型自定义
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T getMapItem(String mapName, String key) {
        return (T) redisTemplate.opsForHash().get(mapName, key);
    }

    /**
     * 从指定HashMap中获取多个key的值
     * （命令HMGET）
     *
     * @param mapName HashMap的名字（no null）
     * @param key     HashMap中的键，传多个（no null）
     * @param <T>     根据实际类型自定义
     * @return list
     */
    public <T> List<T> getMapItems(String mapName, String... key) {
        return redisTemplate.<String, T>opsForHash().multiGet(mapName, Arrays.asList(key));
    }

    /**
     * 获取指定的HashMap
     * （命令HGETALL）
     *
     * @param mapName HashMap的名字（no null）
     * @return HashMap
     */
    public <T> Map<String, T> getMap(String mapName) {
        return redisTemplate.<String, T>opsForHash().entries(mapName);
    }


    /**
     * 删除 HashMap 中的值
     * (命令HDEL)
     *
     * @param mapName HashMap的名字
     * @param keys    HashMap中的key（可以多个，no null）
     */
    public void deleteHash(String mapName, Object... keys) {
        redisTemplate.opsForHash().delete(mapName, keys);
    }

    /**
     * 判断指定 HashMap 中是否含有指定key
     *
     * @param hashMapName HashMap的名字（no null）
     * @param key         HashMap中的key（no null）
     * @return true / false
     */
    public Boolean exists(String hashMapName, String key) {
        return redisTemplate.opsForHash().hasKey(hashMapName, key);
    }

    /**
     * map 递增指定值
     *
     * @param hashMapName HashMap的名字（no null）
     * @param key         HashMap中的key（no null）
     * @return true / false
     */
    public Long incrementMap(String hashMapName, String key, long delta) {
        return redisTemplate.opsForHash().increment(hashMapName, key, delta);
    }

    /**
     * 是否存在hashkey
     *
     * @param hashMapName HashMap的名字（no null）
     * @param hashKey     HashMap中的key（no null）
     * @return true / false
     */
    public Boolean hasMapKey(String hashMapName, Object hashKey) {
        return redisTemplate.opsForHash().hasKey(hashMapName, hashKey);
    }


    //    ============================== zset ==============================

    /**
     * 添加zset元素 有则覆盖
     *
     * @param key   key
     * @param value     value
     * @param score 分数
     * @return {@link Boolean}
     */
    public <T> Boolean zAdd(String key, T value, long score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 移除指定位置的zset元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zRemove(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * z删除
     *
     * @param key key
     * @param vs  vs
     * @return {@link Long}
     */
    public Long zRemove(String key, Object... vs) {
        return redisTemplate.opsForZSet().remove(key, vs);
    }

    /**
     * zset 元素个数
     *
     * @param key
     * @return
     */
    public Long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 如果不存在则添加zset元素
     *
     * @param key   关键
     * @param v     v
     * @param score 分数
     * @return {@link Boolean}
     */
    public <T> Boolean zAddIfNotExist(String key, T v, long score) {
        return redisTemplate.opsForZSet().addIfAbsent(key, v, score);
    }

    /**
     * zset中是否存在对应元素
     *
     * @param key
     * @param v
     * @return
     */
    public <T> Boolean zExist(String key, T v) {
        return redisTemplate.opsForZSet().rank(key, v) != null;
    }

    /**
     * 自增zset score
     *
     * @param key
     * @param v
     * @param delta
     * @return
     */
    public <T> Double zIncrementScore(String key, T v, Double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, v, delta);
    }

    /**
     * 获取指定key的所有元素
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> LinkedHashSet<T> zAll(String key) {
        return (LinkedHashSet<T>) redisTemplate.opsForZSet().range(key, 0, -1);
    }

    /**
     * 获取指定位置元素
     *
     * @param key
     * @param index
     * @param <T>
     * @return
     */
    public <T> T zGet(String key, int index) {
        Set<Object> objects = redisTemplate.opsForZSet().range(key, index, index + 1);
        return (T) objects.iterator().next();
    }

    /**
     * zset删除指定元素
     *
     * @param key
     * @param v
     * @return
     */
    public Long zDel(String key, Object v) {
        return redisTemplate.opsForZSet().remove(key, v);
    }

    //    ============================== set ==============================

    /**
     * 新增set 元素
     *
     * @param key
     * @param v
     * @return
     */
    public <T> Long sAdd(String key, T v) {
        return redisTemplate.opsForSet().add(key, v);
    }

    /**
     * 获取set元素
     *
     * @param key
     * @return
     */
    public <T> Set<T> sGet(String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }

    /**
     * 新增set元素
     *
     * @param key
     * @param values
     */
    public <T> void sAdd(String key, T... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 移除set元素
     *
     * @param key
     * @param v
     */
    public <T> void srm(String key, T v) {
        redisTemplate.opsForSet().remove(key, v);
    }

    //    ============================== list ==============================

    /**
     * 查询list中指定位置元素
     *
     * @param key   key
     * @param index 位置
     */
    public <T> T index(String key, long index) {
        return (T) redisTemplate.opsForList().index(key, index);
    }

    /**
     * 从左边塞入元素
     *
     * @param key
     * @param v
     */
    public <T> Long lPush(String key, T v) {
        return redisTemplate.opsForList().leftPush(key, v);
    }

    /**
     * 从左边塞入元素
     *
     * @param key
     * @param values
     */
    public Long lPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 从右边取出元素
     *
     * @param key
     * @param start
     * @param end
     * @param <T>
     * @return
     */
    public <T> List<T> range(String key, long start, long end) {
        return (List<T>) redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 做左边拉元素
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T lPop(String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }


    /**
     * 从右边批量塞入元素
     *
     * @param key
     * @param vs
     */
    public <T> Long rPushAll(String key, T... vs) {
        return redisTemplate.opsForList().rightPushAll(key, vs);
    }

    /**
     * 从右边批量塞入元素
     *
     * @param key
     * @param vs
     */
    public <T> Long rPush(String key, T t) {
        return redisTemplate.opsForList().rightPush(key, t);
    }

    /**
     * 删除右边的元素
     *
     * @param key
     * @param removeCount
     */
    public void rightRemove(String key, Long removeCount) {
        redisTemplate.opsForList().trim(key, 0, listSize(key) - removeCount);
    }

    /**
     * list集合中元素个数
     *
     * @param key
     */
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 移除list集合中元素
     *
     * @param k
     * @param v
     */
    public Long remove(String k, Object v) {
        return redisTemplate.opsForList().remove(k, 0, v);
    }

    /**
     * 批量查询
     *
     * @param key
     */
    public <T> List<T> list(String key) {
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    //    ============================== Lock ==============================

    /**
     * 获取Redis分布式锁
     *
     * @param key 锁的key
     * @return RedisLock
     */
    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }


    /**
     * 管道
     *
     * @param consumer
     * @param <T>
     * @return大家好，我是xxx1663397196596
     */
    public <T> List<T> pipelined(Consumer<RedisOperations<String, Object>> consumer) {
        return (List<T>) redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                consumer.accept(operations);
                return null;
            }
        });
    }

    public void hyperLogLogAdd(String key, Object value) {
        redisTemplate.opsForHyperLogLog().add(key, value);
    }

    public Long hyperLogLogSize(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

    public <T> Set<T> zReverseRangeByScore(String key, double min, double max, long offset, long count) {
        return (Set<T>) redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    public <T> Set<ZSetOperations.TypedTuple<T>> zReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return ((ZSetOperations<String, T>) redisTemplate.opsForZSet()).reverseRangeByScoreWithScores(key, min, max, offset, count);
    }
}

