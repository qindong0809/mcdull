package com.dqcer.mcdull.framework.web.advice;

import com.dqcer.framework.base.exception.DatabaseException;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.dqcer.mcdull.framework.web.util.IpUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

/**
 * 全局异常处理程序
 *
 * @author dqcer
 * @version  2021/08/17
 */
@RestControllerAdvice
@Order(1)
public class ExceptionAdvice {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 异常
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> exception(Exception exception) {
        log.error("系统异常: ", exception);
        return Result.error(ResultCode.ERROR_UNKNOWN);
    }

    /**
     * 数据库异常
     *
     * @param exception 异常
     * @return {@link Result}<{@link ?}>
     */
    @ExceptionHandler(value = DatabaseException.class)
    public Result<?> databaseException(DatabaseException exception) {
        log.error("数据库操作异常: ", exception);
        return Result.error(exception.getCode());
    }

    /**
     * 缺少资源异常，无法找到对应properties文件中对应的key
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public Result<?> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        log.error("请求头Content-Type异常: ", exception);
        return Result.error(ResultCode.ERROR_CONTENT_TYPE);
    }

    /**
     * 缺少资源异常，无法找到对应properties文件中对应的key
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = MissingResourceException.class)
    public Result<?> missingResourceException(Exception exception) {
        log.error("无法找到对应properties文件中对应的key: ", exception);
        return Result.error(ResultCode.NOT_FIND_PROPERTIES_KEY);
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
        return Result.error(ResultCode.ERROR_CONVERSION);
    }


    /**
     * 处理验证异常
     *
     * @param e e
     * @return {@link Result}
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    public Result<String> handleValidatedException(Exception e, HttpServletRequest request) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            ObjectError objectError = allErrors.get(0);
            Object[] arguments = objectError.getArguments();
            if (arguments == null) {
                String errorMessage = String.format("appName=%s, clientIp=%s, requestURI=%s,，错误提示：%s", applicationName, IpUtil.getIpAddr(request), request.getRequestURI(), objectError.getDefaultMessage());
                log.error("参数异常: {}", errorMessage);
                return Result.error(ResultCode.ERROR_PARAMETERS);
            }
            DefaultMessageSourceResolvable a = (DefaultMessageSourceResolvable) arguments[0];
            String errorMessage = String.format("appName=%s, clientIp=%s, requestURI=%s,字段名称：%s，错误提示：%s", applicationName, IpUtil.getIpAddr(request), request.getRequestURI(), a.getDefaultMessage(), objectError.getDefaultMessage());
            log.error("参数异常: {}", errorMessage);
            return Result.error(ResultCode.ERROR_PARAMETERS);
        }
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) e;
            log.error("参数异常: {}", ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; ")));
            return Result.error(ResultCode.ERROR_PARAMETERS);
        }

        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            ObjectError objectError = allErrors.get(0);
            Object[] arguments = objectError.getArguments();
            DefaultMessageSourceResolvable a = (DefaultMessageSourceResolvable) arguments[0];
            String fieldName = a.getDefaultMessage();
            String errorMessage = String.format("appName=%s, clientIp=%s, requestURI=%s,字段名称：%s，错误提示：%s", applicationName, IpUtil.getIpAddr(request), request.getRequestURI(), fieldName, objectError.getDefaultMessage());
            log.error("参数异常: {}", errorMessage);
            return Result.error(ResultCode.ERROR_PARAMETERS);
        }

        if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException ex = (MissingServletRequestParameterException) e;
            String parameterName = ex.getParameterName();

            log.error("参数异常, parameterName: {}, {}", parameterName, String.format("appName=%s, clientIp=%s, requestURI=%s,message:%s", applicationName, IpUtil.getIpAddr(request), request.getRequestURI(), ex.getMessage()));
            return Result.error(ResultCode.ERROR_PARAMETERS);
        }
        log.error("参数异常: {}", String.format("appName=%s, clientIp=%s, requestURI=%s,message:%s", applicationName, IpUtil.getIpAddr(request), request.getRequestURI(), e.getMessage()));
        return Result.error(ResultCode.ERROR_PARAMETERS);
    }
}
