package io.gitee.dqcer.mcdull.framework.redis.annotation;

import java.lang.annotation.*;

/**
 * redis锁
 *
 * @author dqcer
 * @since 2022/12/13
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
     * 等待超时时间，单位秒。未指定租约时间，由 Redisson 看门狗自动续期。
     *
     * @return long
     */
    long timeout() default 10;

    /**
     * 获取锁失败时是否抛出业务异常。默认 true，避免业务方法被静默跳过而返回 null；
     * 幂等补偿等允许降级的场景可显式设为 false。
     *
     * @return boolean
     */
    boolean throwOnFailure() default true;

    /**
     * 获取锁失败时使用的 i18n messageCode，为空时默认使用 system.busy。
     *
     * @return String
     */
    String messageCode() default "";
}
