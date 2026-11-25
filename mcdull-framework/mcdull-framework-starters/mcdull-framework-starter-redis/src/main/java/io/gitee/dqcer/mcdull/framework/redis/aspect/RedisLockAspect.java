package io.gitee.dqcer.mcdull.framework.redis.aspect;


import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.constants.SymbolConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.redis.annotation.RedisLock;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * redis lock Aspect
 *
 * @author dqcer
 * @since 2022/10/05
 */
@Order(GlobalConstant.Order.ASPECT_REDIS_LOCK)
@Aspect
public class RedisLockAspect {

    protected static final Logger log = LoggerFactory.getLogger(RedisLockAspect.class);

    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Resource
    private RedissonClient redissonClient;

    /**
     * 设置方法对应的缓存过期时间
     *
     * @param proceedingJoinPoint 连接点
     * @return {@link Object}
     */
    @Around("@annotation(io.gitee.dqcer.mcdull.framework.redis.annotation.RedisLock)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("分布式锁开始执行");
        }

        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("the Annotation @RedisLock must used on method!");
        }

        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);

        if (redisLock.key() == null || redisLock.key().trim().isEmpty()) {
            return proceedingJoinPoint.proceed();
        }
        String key = calculateValue(redisLock.key(), method, proceedingJoinPoint.getArgs());
        long timeout = redisLock.timeout() < 1 ? 10 : redisLock.timeout();
        RLock lock = redissonClient.getLock(key);
        boolean locked;
        try {
            locked = lock.tryLock(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogHelp.error(log, "Interrupted while acquiring distributed lock. key: {}", key, e);
            throw lockFailure(redisLock, e);
        }
        if (!locked) {
            LogHelp.warn(log, "Failed to acquire distributed lock. key: {}, method: {}",
                    key, methodSignature.toLongString());
            if (redisLock.throwOnFailure()) {
                throw lockFailure(redisLock, null);
            }
            return null;
        }

        try {
            LogHelp.debug(log, "分布式锁成功加锁. key: {}", key);
            try {
                return proceedingJoinPoint.proceed();
            } catch (Exception e) {
                LogHelp.error(log, e.getMessage(), e);
                throw e;
            }
        } finally {
            try {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    LogHelp.debug(log, "分布式锁成功释放锁. key: {}", key);
                }
            } catch (Exception e) {
                LogHelp.warn(log, "Failed to release distributed lock. key: {}", key, e);
            }
        }
    }

    private BusinessException lockFailure(RedisLock redisLock, Throwable cause) {
        String messageCode = redisLock.messageCode();
        if (messageCode == null || messageCode.trim().isEmpty()) {
            messageCode = I18nConstants.SYSTEM_BUSY;
        }
        BusinessException exception = new BusinessException(messageCode);
        if (cause != null) {
            exception.initCause(cause);
        }
        return exception;
    }

    /**
     * spel表达式通过方法入参计算数据源名称
     *
     * @param annoValue annoValue
     * @param method    方法
     * @param arguments 参数
     * @return {@link String}
     */
    private String calculateValue(String annoValue, Method method, Object[] arguments) {
        if(!annoValue.contains(SymbolConstants.JH)) {
            return annoValue;
        }
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, arguments, NAME_DISCOVERER);
        Object value = PARSER.parseExpression(annoValue).getValue(context);
        return value == null ? null : value.toString();
    }
}
