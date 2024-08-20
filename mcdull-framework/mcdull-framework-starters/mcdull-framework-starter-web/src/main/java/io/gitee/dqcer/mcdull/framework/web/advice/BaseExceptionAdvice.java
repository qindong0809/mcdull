package io.gitee.dqcer.mcdull.framework.web.advice;

import cn.dev33.satoken.exception.SaTokenException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * sql异常拦截
 *
 * @author dqcer
 * @since 2022/12/24
 */
@RestControllerAdvice
@Order(0)
public class BaseExceptionAdvice extends AbstractExceptionAdvice{

    @ExceptionHandler(value = SaTokenException.class)
    public Result<?> handlerSaTokenException(SaTokenException exception) {
        LogHelp.error(log, "{}. handlerSaTokenException.", UserContextHolder.print(), exception);
        // 根据不同异常细分状态码返回不同的提示
        if(exception.getCode() == 11016) {
            CodeEnum timeoutLogin = CodeEnum.TIMEOUT_LOGIN;
            String i18nMessage = dynamicLocaleMessageSource.getMessage(timeoutLogin.getMessage());
            return Result.error(timeoutLogin.getCode(), i18nMessage, super.buildExceptionStr(exception));
        }
        if (exception.getCode() == 11051) {
            CodeEnum checkModuleCode = CodeEnum.POWER_CHECK_MODULE;
            String i18nMessage = dynamicLocaleMessageSource.getMessage(checkModuleCode.getMessage());
            return Result.error(checkModuleCode.getCode(), i18nMessage, super.buildExceptionStr(exception));
        }
        // 更多 code 码判断 ...
        CodeEnum unAuthorization = CodeEnum.UN_AUTHORIZATION;
        String i18nMessage = dynamicLocaleMessageSource.getMessage(unAuthorization.getMessage());
        return Result.error(unAuthorization.getCode(), i18nMessage, super.buildExceptionStr(exception));
    }
}
