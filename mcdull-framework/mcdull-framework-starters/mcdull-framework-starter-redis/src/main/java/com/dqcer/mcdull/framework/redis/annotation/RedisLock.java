package com.dqcer.mcdull.framework.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis锁
 *
 * @author dqcer
 * @version 2022/12/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * 分布式锁key值，支持spel表达式
     *
     * @return {@link String}
     */
    String key() default "";

    /**
     * 等待超时时间，单位秒
     *
     * @return long
     */
    long timeout() default 10;
}
