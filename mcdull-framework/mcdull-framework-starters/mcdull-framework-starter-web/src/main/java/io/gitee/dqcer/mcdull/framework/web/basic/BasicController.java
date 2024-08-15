package io.gitee.dqcer.mcdull.framework.web.basic;

import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.web.component.ConcurrentRateLimiter;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Supplier;

/**
 * basic controller
 *
 * @author dqcer
 * @since 2023/05/18
 */
@SuppressWarnings("unused")
public abstract class BasicController {

    @Resource
    private ConcurrentRateLimiter concurrentRateLimiter;

    @Resource
    private DynamicLocaleMessageSource dynamicLocaleMessageSource;

    protected Logger log = LoggerFactory.getLogger(getClass());
    protected HttpServletRequest getRequest() {
        return ServletUtil.getRequest();
    }
    protected HttpServletResponse getResponse() {
        return ServletUtil.getResponse();
    }

    /**
     * 限流
     *
     * @param key      key
     * @param quantity quantity
     * @param seconds  seconds
     * @param function function
     * @param <T>      T
     * @return T
     */
    protected <T> T rateLimiter(String key, int quantity, int seconds, Supplier<T> function) {
        if (concurrentRateLimiter.limiter(key, quantity, seconds)) {
            return function.get();
        }
        LogHelp.warn(log, "rateLimiter. key: {}. quantity: {}. seconds: {}", key, quantity, seconds);
        throw new BusinessException(I18nConstants.SYSTEM_REQUEST_TOO_FREQUENT);
    }

    /**
     * 锁
     *
     * @param key     key
     * @param timeout timeout
     * @param function function
     * @param <T>     T
     * @return T
     * @throws Exception Exception
     */
    protected <T> T locker(String key, long timeout, Supplier<T> function) throws Exception {
        return concurrentRateLimiter.locker(key, timeout, function);
    }
}
