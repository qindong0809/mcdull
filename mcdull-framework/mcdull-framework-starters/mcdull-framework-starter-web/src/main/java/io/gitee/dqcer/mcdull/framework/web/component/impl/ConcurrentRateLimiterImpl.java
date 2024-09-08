package io.gitee.dqcer.mcdull.framework.web.component.impl;

import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedisClient;
import io.gitee.dqcer.mcdull.framework.web.component.ConcurrentRateLimiter;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * concurrent rate limiter
 *
 * @author dqcer
 * @since 2024/8/14 14:43
 */
public class ConcurrentRateLimiterImpl implements ConcurrentRateLimiter {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private RedisClient redisClient;

    @Override
    public boolean limiter(String key, int quantity, int time) {
        LogHelp.info(log, "RateLimiter. key:{}, quantity:{}, time:{}", key, quantity, time);
        return redisClient.rateLimit(key, quantity, time);
    }

    @Override
    public <T> T locker(String key, long timeout, Supplier<T> function) {
        LogHelp.info(log, "Locker. key:{}, timeout:{}", key, timeout);
        RLock lock = redisClient.getLock(key);
        boolean locked = false;
        try {
            locked = lock.tryLock(timeout, TimeUnit.SECONDS);
        } catch (Exception  e) {
            LogHelp.error(log, "Locker InterruptedException error. key: {}, timeout:{} ", key, timeout, e);
        }
        if (!locked) {
            throw new BusinessException(I18nConstants.SYSTEM_BUSY);
        }
        try {
            return function.get();
        } catch (Exception e) {
            LogHelp.error(log, "Locker error. key: {}, timeout:{} ", key, timeout);
            throw e;
        } finally {
            lock.unlock();
            LogHelp.debug(log, "分布式锁成功释放锁. key: {}", key);
        }
    }
}
