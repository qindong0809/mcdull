package io.gitee.dqcer.mcdull.framework.redis.aspect;


import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.redis.annotation.CacheExpire;
import io.gitee.dqcer.mcdull.framework.redis.annotation.CacheExpireHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;


/**
 * 缓存失效时间Aspect
 *
 * @author dqcer
 * @since 2022/10/05
 */
@Order(GlobalConstant.Order.ASPECT_CACHE_EXPIRE)
@Aspect
public class CacheExpireAspect {

    protected static final Logger log = LoggerFactory.getLogger(CacheExpireAspect.class);

    /**
     * 设置方法对应的缓存过期时间
     *
     * @param joinPoint 连接点
     * @return {@link Object}
     */
    @Around("@annotation(io.gitee.dqcer.mcdull.framework.redis.annotation.CacheExpire)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CacheExpire cacheExpire = method.getAnnotation(CacheExpire.class);
        // 过期时间（毫秒）
        long cacheExpiredMilliseconds = 1000L * cacheExpire.value();
        // 上下浮动范围（毫秒） (数据类型)(最小值+Math.random()*(最大值-最小值+1))
        long floatRangeMilliseconds = (long) (1 + Math.random() * (cacheExpire.floatRange() * 1000 - 1 + 1));
        //  设置失效时间，加上随机浮动值防止缓存穿透
        CacheExpireHolder.set(cacheExpiredMilliseconds + floatRangeMilliseconds);
        LogHelp.info(log, "缓存切面，设置过期时间{} ms", cacheExpiredMilliseconds + floatRangeMilliseconds);
        try {
            return joinPoint.proceed();
        }catch (Exception e){
            LogHelp.error(log, e.getMessage(), e);
            throw e;
        } finally {
            //清除对象
            CacheExpireHolder.remove();
        }
    }
}
