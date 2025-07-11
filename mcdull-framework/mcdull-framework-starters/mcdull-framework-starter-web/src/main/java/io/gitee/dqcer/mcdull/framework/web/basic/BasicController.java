package io.gitee.dqcer.mcdull.framework.web.basic;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.web.component.ConcurrentRateLimiter;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Consumer;
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
    protected ConcurrentRateLimiter concurrentRateLimiter;

    @Resource
    protected DynamicLocaleMessageSource dynamicLocaleMessageSource;

    @Resource
    protected McdullProperties mcdullProperties;

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
        key = this.buildKeyName(key);
        if (concurrentRateLimiter.limiter(key, quantity, seconds)) {
            return function.get();
        }
        throw new BusinessException(I18nConstants.SYSTEM_REQUEST_TOO_FREQUENT);
    }

    private String buildKeyName(String key) {
        key = mcdullProperties.getApplicationName() + ":" + GlobalConstant.RATE_LIMITER + key;
        return key;
    }

    protected <T> void rateLimiter(String key, int quantity, int seconds, Consumer<T> function) {
        key = this.buildKeyName(key);
        if (concurrentRateLimiter.limiter(key, quantity, seconds)) {
            function.accept(null);
        }
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
     */
    protected <T> T locker(String key, long timeout, Supplier<T> function) {
        String applicationName = mcdullProperties.getApplicationName();
        final String logKey = StrUtil.format("{}_{}_{}:{}", GlobalConstant.ROOT_PREFIX, applicationName, key);
        return concurrentRateLimiter.locker(key, timeout, function);
    }

    protected <T> T locker1(String key, Supplier<T> function) {
        UnifySession session = UserContextHolder.getSession();
        return concurrentRateLimiter.locker(key, 0L, function);
    }

    protected <T> T locker(String key, Supplier<T> function) {
        key = Convert.toStr(key, "blank");
        UnifySession session = UserContextHolder.getSession();
        String permissionCode = session.getPermissionCode();
        String loginName = session.getLoginName();
        String bizKey = StrUtil.format("{}_{}_{}", loginName, permissionCode, key);
        return concurrentRateLimiter.locker(bizKey, 0L, function);
    }

    protected <T, S> T locker(String key, Supplier<S> function, T defaultValue) {
        concurrentRateLimiter.locker(key, 0L, function);
        return defaultValue;
    }
}
