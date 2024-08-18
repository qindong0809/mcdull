package io.gitee.dqcer.mcdull.framework.web.advice;

import cn.dev33.satoken.exception.SaTokenException;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Collections;

/**
 * sql异常拦截
 *
 * @author dqcer
 * @since 2022/12/24
 */
@RestControllerAdvice
@Order(0)
public class BaseExceptionAdvice {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private DynamicLocaleMessageSource dynamicLocaleMessageSource;

    /**
     * 异常
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = SQLException.class)
    public Result<?> exception(SQLException exception) {
        LogHelp.error(log, "sql异常: ", exception);
        return Result.error(CodeEnum.SQL_SYNTAX_ERROR, Collections.singletonList(exception.getMessage()));
    }

    @ExceptionHandler(value = SaTokenException.class)
    public Result<?> handlerSaTokenException(SaTokenException exception) {
        LogHelp.error(log, "认证授权异常: ", exception);
        // 根据不同异常细分状态码返回不同的提示
        if(exception.getCode() == 11016) {
            return Result.error(CodeEnum.TIMEOUT_LOGIN, Collections.emptyList());
        }
        if (exception.getCode() == 11051) {
            String i18nMessage = dynamicLocaleMessageSource.getMessage(I18nConstants.PERMISSION_DENIED, null);
            LogHelp.error(log, "{}. Business Exception. {}", UserContextHolder.print(), i18nMessage, exception);
            return Result.error(i18nMessage);
        }
        // 更多 code 码判断 ...
        CodeEnum codeEnum = CodeEnum.UN_AUTHORIZATION;
        return Result.error(codeEnum.getCode(), exception.getMessage(), null);
    }
}
