package com.dqcer.mcdull.framework.redis;


import com.dqcer.mcdull.framework.redis.config.RedissonAutoConfiguration;
import com.dqcer.mcdull.framework.redis.operation.CaffeineCache;
import com.dqcer.mcdull.framework.redis.operation.RedisClient;
import com.dqcer.mcdull.framework.redis.operation.RedissonCache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用缓存
 *
 * @author dqcer
 * @version 2021/09/10
 */
@EnableCaching
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import({RedissonAutoConfiguration.class, RedissonCache.class, CaffeineCache.class, RedisClient.class})
public @interface EnableCache {
}
