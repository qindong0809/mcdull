package io.gitee.dqcer.mcdull.framework.web.advice;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ICode;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 全局异常处理程序
 *
 * @author dqcer
 * @since  2021/08/17
 */
@RestControllerAdvice
@Order(1)
public class ExceptionAdvice {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private DynamicLocaleMessageSource dynamicLocaleMessageSource;

    /**
     * 异常
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> exception(Exception exception) {
        LogHelp.error(log, "{}. Exception: ", UserContextHolder.print(), exception);
        CodeEnum codeEnum = CodeEnum.INTERNAL_SERVER_ERROR;
        String message = dynamicLocaleMessageSource.getMessage(codeEnum.getMessage());
        return Result.error(codeEnum.getCode(), message, this.buildExceptionStr(exception));
    }

    private String buildExceptionStr(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(stringWriter);
        exception.printStackTrace(pw);
        String errorStack = stringWriter.toString();
        if (StrUtil.isNotBlank(errorStack)) {
            if (errorStack.length() > 1000) {
                return errorStack.substring(0, 1000);
            }
        }
        return StrUtil.EMPTY;
    }


    /**
     * 业务异常
     *
     * @param exception 异常
     * @return Result&lt;?&gt;
     */
    @ExceptionHandler(value = BusinessException.class)
    public Result<?> businessException(BusinessException exception) {
        String i18nMessage = dynamicLocaleMessageSource.getMessage(exception.getMessageCode(), exception.getArgs());
        LogHelp.error(log, "{}. Business Exception. {}", UserContextHolder.print(), i18nMessage, exception);
        return Result.error(i18nMessage);
    }

    /**
     * 数据库异常
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = DatabaseRowException.class)
    public Result<?> databaseRowException(DatabaseRowException exception) {
        LogHelp.error(log, "{}. 数据库实际预期执行不同: ", UserContextHolder.print(), exception);
        ICode exceptionCode = exception.getCode();
        if (exceptionCode != null) {
            return Result.error(exceptionCode);
        }
        return Result.error(CodeEnum.DB_ERROR);
    }

    /**
     * 处理验证异常
     *
     * @param e e
     * @return {@link Result}
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result<String> handleValidatedException(Exception e) {
        MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        List<String> errorList = new ArrayList<>();
        for (ObjectError error : allErrors) {
            String field = ((FieldError) error).getField();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            errorList.add(StrUtil.format("{}. {}:[{}] message: {}", error.getObjectName(),
                    field, rejectedValue, error.getDefaultMessage()));
        }
        Collections.sort(errorList);
        String args = StrUtil.join(StrUtil.COMMA + StrUtil.SPACE, errorList);
        LogHelp.error(log, "Parameter exception: {}", args);
        CodeEnum codeEnum = CodeEnum.ERROR_PARAMETERS;
        return Result.error(codeEnum.getCode(),
                dynamicLocaleMessageSource.getMessage(codeEnum.getMessage(), new Object[]{args}), this.buildExceptionStr(e));
    }

}
