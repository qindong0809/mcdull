package io.gitee.dqcer.mcdull.framework.web.advice;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletResponse;
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
public class ExceptionAdvice extends AbstractExceptionAdvice{

    /**
     * 客户端中断连接异常（如浏览器关闭、请求取消等），无需返回响应体
     *
     * @param exception 异常
     * @param response  响应
     */
    @ExceptionHandler(value = org.apache.catalina.connector.ClientAbortException.class)
    public void clientAbortException(Exception exception, HttpServletResponse response) {
        LogHelp.debug(log, "{}. ClientAbortException (client disconnected): {}",
                UserContextHolder.print(), exception.getMessage());
    }

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


    /**
     * 业务异常
     *
     * @param exception 异常
     * @return Result&lt;?&gt;
     */
    @ExceptionHandler(value = BusinessException.class)
    public Result<?> businessException(BusinessException exception) {
        String i18nMessage = dynamicLocaleMessageSource
            .getMessage(exception.getMessageCode(), exception.getArgs());
        if (exception.getCode() != null) {
            return Result.error(exception.getCode().getCode(), i18nMessage, this.buildExceptionStr(exception));
        }
        LogHelp.error(log, "{}. Business Exception. {}",
                UserContextHolder.print(), i18nMessage, exception);
        return Result.error(i18nMessage);
    }

    /**
     * 处理 @Valid / @Validated 触发的 Bean Validation 异常
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
            if (error instanceof FieldError fieldError) {
                errorList.add(StrUtil.format("{}. {}:[{}] message: {}",
                        error.getObjectName(),
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        error.getDefaultMessage()));
            } else {
                errorList.add(StrUtil.format("{}. message: {}",
                        error.getObjectName(), error.getDefaultMessage()));
            }
        }
        return this.validationErrorResult(errorList, e);
    }

    /**
     * 处理类级别 @Validated 对方法参数的约束异常（如 @PathVariable / @RequestParam 上的约束）
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public Result<String> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errorList = new ArrayList<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errorList.add(StrUtil.format("{}. {}:[{}] message: {}",
                    violation.getRootBeanClass().getSimpleName(),
                    violation.getPropertyPath(),
                    violation.getInvalidValue(),
                    violation.getMessage()));
        }
        return this.validationErrorResult(errorList, e);
    }

    /**
     * 处理 Spring 6.1+ 内置方法校验抛出的异常
     */
    @ExceptionHandler(value = {HandlerMethodValidationException.class})
    public Result<String> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        List<String> errorList = new ArrayList<>();
        for (ParameterValidationResult result : e.getAllValidationResults()) {
            String parameter = result.getMethodParameter().getParameterName();
            for (MessageSourceResolvable error : result.getResolvableErrors()) {
                errorList.add(StrUtil.format("{}. message: {}", parameter, error.getDefaultMessage()));
            }
        }
        return this.validationErrorResult(errorList, e);
    }

    private Result<String> validationErrorResult(List<String> errorList, Exception e) {
        Collections.sort(errorList);
        String args = StrUtil.join(StrUtil.COMMA + StrUtil.SPACE, errorList);
        LogHelp.error(log, "{}. Parameter exception. args:{}", UserContextHolder.print(), args, e);
        CodeEnum codeEnum = CodeEnum.ERROR_PARAMETERS;
        return Result.error(codeEnum.getCode(),
                dynamicLocaleMessageSource.getMessage(codeEnum.getMessage(), new Object[]{args}), this.buildExceptionStr(e));
    }

    /**
     * 处理资源未找到异常
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    public Result<?> noResourceFoundException(NoResourceFoundException exception) {
        LogHelp.error(log, "{}. NoResourceFoundException: {}", UserContextHolder.print(), exception.getMessage(), exception);
        CodeEnum codeEnum = CodeEnum.NOT_FOUND;
        String message = dynamicLocaleMessageSource.getMessage(codeEnum.getMessage());
        return Result.error(codeEnum.getCode(), message, this.buildExceptionStr(exception));
    }

}
