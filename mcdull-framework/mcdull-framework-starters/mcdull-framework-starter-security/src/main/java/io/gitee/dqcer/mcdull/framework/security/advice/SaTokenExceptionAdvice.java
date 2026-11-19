package io.gitee.dqcer.mcdull.framework.security.advice;

import cn.dev33.satoken.exception.SaTokenException;
import io.gitee.dqcer.mcdull.framework.base.help.ExceptionHelp;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.spi.DynamicLocaleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * sa-token 异常拦截。
 * <p>
 * 原先挂在 starter-web 的 {@code BaseExceptionAdvice} 上，属于鉴权能力，随鉴权闸门一起搬回 security，
 * 使 starter-web 不再编译期依赖 sa-token。
 *
 * @author dqcer
 * @since 2026-11-19
 */
@RestControllerAdvice
@Order(0)
public class SaTokenExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(SaTokenExceptionAdvice.class);

    /**
     * 文案取值是可选能力：未装配 starter-web 时回落为文案 key 本身
     */
    private final DynamicLocaleMessageSource dynamicLocaleMessageSource;

    public SaTokenExceptionAdvice(ObjectProvider<DynamicLocaleMessageSource> messageSourceProvider) {
        this.dynamicLocaleMessageSource = messageSourceProvider.getIfAvailable();
    }

    @ExceptionHandler(SaTokenException.class)
    public Result<?> handlerSaTokenException(SaTokenException exception) {
        return toResult(exception);
    }

    /**
     * 按 sa-token 的异常状态码细分提示语
     *
     * @param exception sa-token 异常
     * @return {@link Result}
     */
    public Result<?> toResult(SaTokenException exception) {
        LogHelp.error(log, "{}. handlerSaTokenException.", UserContextHolder.print(), exception);
        CodeEnum codeEnum = switch (exception.getCode()) {
            case 11016 -> CodeEnum.TIMEOUT_LOGIN;
            case 11051 -> CodeEnum.POWER_CHECK_MODULE;
            default -> CodeEnum.UN_AUTHORIZATION;
        };
        return Result.error(codeEnum.getCode(), message(codeEnum.getMessage()), ExceptionHelp.buildStackStr(exception));
    }

    private String message(String code) {
        return dynamicLocaleMessageSource == null ? code : dynamicLocaleMessageSource.getMessage(code);
    }
}
