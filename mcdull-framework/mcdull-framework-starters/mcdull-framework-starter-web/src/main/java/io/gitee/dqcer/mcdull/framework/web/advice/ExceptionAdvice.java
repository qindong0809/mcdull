package io.gitee.dqcer.mcdull.framework.web.advice;

import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ICode;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

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

    @Value("${spring.application.name}")
    private String applicationName;

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
        log.error("{}. Exception: ", UserContextHolder.print(), exception);
        StringWriter stringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(stringWriter);
        exception.printStackTrace(pw);

        String errorStack = stringWriter.toString();
        errorStack = errorStack.substring(0, 1000);
        return Result.error(CodeEnum.INTERNAL_SERVER_ERROR, Collections.singletonList(errorStack));
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
        log.error("{}. Business Exception. {}", UserContextHolder.print(), i18nMessage, exception);
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
        log.error("{}. 数据库实际预期执行不同: ", UserContextHolder.print(), exception);
        ICode exceptionCode = exception.getCode();
        if (exceptionCode != null) {
            return Result.error(exceptionCode);
        }
        return Result.error(CodeEnum.DB_ERROR);
    }

    /**
     * 缺少资源异常，无法找到对应properties文件中对应的key
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public Result<?> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        log.error("{}. 请求头Content-Type异常: ", UserContextHolder.print(), exception);
        return Result.error(CodeEnum.ERROR_CONTENT_TYPE);
    }

    /**
     * 缺少资源异常，无法找到对应properties文件中对应的key
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = MissingResourceException.class)
    public Result<?> missingResourceException(MissingResourceException exception) {
        log.error("无法找到对应properties文件中对应的key: ", exception);
        return Result.error(CodeEnum.NOT_FIND_PROPERTIES_KEY);
    }

    /**
     * http消息转换异常，参数接收时，类型转换异常
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result<?> httpMessageConversionException(HttpMessageNotReadableException exception) {
        log.error("参数接收时，类型转换异常: ", exception);
        return Result.error(CodeEnum.ERROR_CONVERSION);
    }


    /**
     * 处理验证异常
     *
     * @param e e
     * @return {@link Result}
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class})
    public Result<String> handleValidatedException(Exception e) {
        String errorMessage = "";
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            ObjectError objectError = allErrors.get(0);
            Object[] arguments = objectError.getArguments();
            if (arguments == null) {
                errorMessage = String.format("appName: %s, %s", applicationName,  objectError.getDefaultMessage());
                log.error("parameter exception: {}", errorMessage);
                return Result.error(CodeEnum.ERROR_PARAMETERS, Collections.singletonList(errorMessage));
            }
            DefaultMessageSourceResolvable a = (DefaultMessageSourceResolvable) arguments[0];
            errorMessage = String.format("appName: %s, %s: %s", applicationName, a.getDefaultMessage(), objectError.getDefaultMessage());
            log.error("parameter exception: {}", errorMessage);
            return Result.error(CodeEnum.ERROR_PARAMETERS, Collections.singletonList(errorMessage));
        }
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) e;
            errorMessage = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; "));
            log.error("parameter exception: {}", errorMessage);
            return Result.error(CodeEnum.ERROR_PARAMETERS, Collections.singletonList(errorMessage));
        }

        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            ObjectError objectError = allErrors.get(0);
            Object[] arguments = objectError.getArguments();
            if (arguments != null) {
                DefaultMessageSourceResolvable a = (DefaultMessageSourceResolvable) arguments[0];
                String fieldName = a.getDefaultMessage();
                errorMessage = String.format("appName: %s, %s: %s",
                        applicationName, fieldName, objectError.getDefaultMessage());
            }
            log.error("parameter exception: {}", errorMessage);
            return Result.error(CodeEnum.ERROR_PARAMETERS, Collections.singletonList(errorMessage));
        }

        if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException ex = (MissingServletRequestParameterException) e;
            String parameterName = ex.getParameterName();

            log.error("parameter exception, parameterName: {}, {}", parameterName,
                    String.format("appName: %s, %s", applicationName, ex.getMessage()));
            return Result.error(CodeEnum.ERROR_PARAMETERS, Collections.singletonList(errorMessage));
        }
        if (e instanceof ValidationException) {
            ValidationException validationException = (ValidationException) e;
            String parameterName = validationException.getMessage();
            log.error("参数异常, parameterName: {}, {}", parameterName,
                    String.format("appName: %s, message:%s", applicationName, validationException.getMessage()));
            return Result.error(CodeEnum.ERROR_PARAMETERS, Collections.singletonList(errorMessage));
        }
        errorMessage = String.format("appName: %s, message:%s", applicationName, e.getMessage());
        log.error("parameter exception: {}", errorMessage);
        return Result.error(CodeEnum.ERROR_PARAMETERS, Collections.singletonList(errorMessage));
    }
}
