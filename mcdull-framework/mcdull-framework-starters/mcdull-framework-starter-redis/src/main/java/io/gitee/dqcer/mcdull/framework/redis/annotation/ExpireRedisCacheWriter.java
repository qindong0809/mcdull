package io.gitee.dqcer.mcdull.framework.redis.annotation;

import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.cache.CacheStatistics;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 含过期时间的RedisCacheWriter
 *
 * @author dqcer
 * @version 2022/10/05
 */
public class ExpireRedisCacheWriter implements RedisCacheWriter {

    private final RedisConnectionFactory connectionFactory;

    private final Duration sleepTime;

    private final CacheStatisticsCollector statistics;

    /**
     * @param connectionFactory must not be {@literal null}.
     */
    public ExpireRedisCacheWriter(RedisConnectionFactory connectionFactory) {
        this(connectionFactory, Duration.ZERO);
    }

    /**
     * @param connectionFactory must not be {@literal null}.
     * @param sleepTime         sleep time between lock request attempts. Must not be {@literal null}. Use {@link Duration#ZERO}
     *                          to disable locking.
     */
    ExpireRedisCacheWriter(RedisConnectionFactory connectionFactory, Duration sleepTime) {
        this(connectionFactory, sleepTime, CacheStatisticsCollector.none());
    }

    /**
     * @param connectionFactory        must not be {@literal null}.
     * @param sleepTime                sleep time between lock request attempts. Must not be {@literal null}. Use {@link Duration#ZERO}
     *                                 to disable locking.
     * @param cacheStatisticsCollector must not be {@literal null}.
     */
    ExpireRedisCacheWriter(RedisConnectionFactory connectionFactory, Duration sleepTime,
                           CacheStatisticsCollector cacheStatisticsCollector) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        Assert.notNull(sleepTime, "SleepTime must not be null!");
        Assert.notNull(cacheStatisticsCollector, "CacheStatisticsCollector must not be null!");

        this.connectionFactory = connectionFactory;
        this.sleepTime = sleepTime;
        this.statistics = cacheStatisticsCollector;
    }

    private static boolean shouldExpireWithin(@Nullable Duration ttl) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

    private static byte[] createCacheLockKey(String name) {
        return (name + "~lock").getBytes(StandardCharsets.UTF_8);
    }


    @Override
    public void put(@NonNull String name, @NonNull byte[] key, byte[] value, @Nullable Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");
        Duration expiredTime = getExpiredTime(ttl);
        execute(name, connection -> {
            if (shouldExpireWithin(expiredTime)) {
                connection.set(key, value, Expiration.from(expiredTime.toMillis(), TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.upsert());
            } else {
                connection.set(key, value);
            }

            return "OK";
        });
        statistics.incPuts(name);
    }

    @Override
    public byte[] get(@NonNull String name, @NonNull byte[] key) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        byte[] result = execute(name, connection -> connection.get(key));
        statistics.incGets(name);
        if (result != null) {
            statistics.incHits(name);
        } else {
            statistics.incMisses(name);
        }
        return result;
    }


    @Override
    public byte[] putIfAbsent(@NonNull String name, @NonNull byte[] key, @NonNull byte[] value, Duration ttl) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");
        Duration expiredTime = getExpiredTime(ttl);
        return execute(name, connection -> {
            if (isLockingCacheWriter()) {
                doLock(name, connection);
            }
            try {
                Boolean put;
                if (shouldExpireWithin(expiredTime)) {
                    put = connection.set(key, value, Expiration.from(expiredTime), RedisStringCommands.SetOption.ifAbsent());
                } else {
                    put = connection.setNX(key, value);
                }

                if (Boolean.TRUE.equals(put)) {
                    statistics.incPuts(name);
                    return null;
                }
                return connection.get(key);
            } finally {
                if (isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }
        });
    }


    @Override
    public void remove(@NonNull String name, @NonNull byte[] key) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        execute(name, connection -> connection.del(key));
        statistics.incDeletes(name);
    }


    @Override
    public void clean(@NonNull String name, @NonNull byte[] pattern) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(pattern, "Pattern must not be null!");
        execute(name, connection -> {
            boolean wasLocked = false;
            try {
                if (isLockingCacheWriter()) {
                    doLock(name, connection);
                    wasLocked = true;
                }
                byte[][] keys = Optional.ofNullable(connection.keys(pattern)).orElse(Collections.emptySet())
                        .toArray(new byte[0][]);
                if (keys.length > 0) {
                    statistics.incDeletesBy(name, keys.length);
                    connection.del(keys);
                }
            } finally {
                if (wasLocked && isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }
            return "OK";
        });
    }


    @Override
    @NonNull
    public CacheStatistics getCacheStatistics(@NonNull String cacheName) {
        return statistics.getCacheStatistics(cacheName);
    }


    @Override
    public void clearStatistics(@NonNull String name) {
        statistics.reset(name);
    }


    @Override
    @NonNull
    public RedisCacheWriter withStatisticsCollector(@NonNull CacheStatisticsCollector cacheStatisticsCollector) {
        return new ExpireRedisCacheWriter(connectionFactory, sleepTime, cacheStatisticsCollector);
    }

    /**
     * Explicitly set a write lock on a cache.
     *
     * @param name the name of the cache to lock.
     */
    void lock(String name) {
        execute(name, connection -> doLock(name, connection));
    }

    /**
     * Explicitly remove a write lock from a cache.
     *
     * @param name the name of the cache to unlock.
     */
    void unlock(String name) {
        executeLockFree(connection -> doUnlock(name, connection));
    }

    private Boolean doLock(String name, RedisConnection connection) {
        return connection.setNX(createCacheLockKey(name), new byte[0]);
    }

    private void doUnlock(String name, RedisConnection connection) {
        connection.del(createCacheLockKey(name));
    }

    boolean doCheckLock(String name, RedisConnection connection) {
        return Boolean.TRUE.equals(connection.exists(createCacheLockKey(name)));
    }

    /**
     * @return {@literal true} if {@link RedisCacheWriter} uses locks.
     */
    private boolean isLockingCacheWriter() {
        return !sleepTime.isZero() && !sleepTime.isNegative();
    }

    private <T> T execute(String name, Function<RedisConnection, T> callback) {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            checkAndPotentiallyWaitUntilUnlocked(name, connection);
            return callback.apply(connection);
        }
    }

    private void executeLockFree(Consumer<RedisConnection> callback) {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            callback.accept(connection);
        }
    }

    private void checkAndPotentiallyWaitUntilUnlocked(String name, RedisConnection connection) {
        if (!isLockingCacheWriter()) {
            return;
        }

        long lockWaitTimeNs = System.nanoTime();
        try {

            while (doCheckLock(name, connection)) {
                Thread.sleep(sleepTime.toMillis());
            }
        } catch (InterruptedException ex) {
            // Re-interrupt current thread, to allow other participants to react.
            Thread.currentThread().interrupt();
            throw new PessimisticLockingFailureException(String.format("Interrupted while waiting to unlock cache %s", name),
                    ex);
        } finally {
            statistics.incLockTime(name, System.nanoTime() - lockWaitTimeNs);
        }
    }

    private Duration getExpiredTime(Duration ttl) {
        return Optional.ofNullable(CacheExpireHolder.get()).map(Duration::ofSeconds).orElse(ttl);
    }
}
