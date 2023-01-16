package com.dqcer.mcdull.framework.web.advice;

import com.dqcer.mcdull.framework.base.wrapper.Result;
import com.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Collections;

/**
 * sql异常拦截
 *
 * @author dqcer
 * @version 2022/12/24
 */
@RestControllerAdvice
@Order(0)
public class BaseExceptionAdvice {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 异常
     *
     * @param exception 异常
     * @return {@link Result}
     */
    @ExceptionHandler(value = SQLException.class)
    public Result<?> exception(SQLException exception) {
        log.error("sql异常: ", exception);
        return Result.error(CodeEnum.SQL_SYNTAX_ERROR, Collections.singletonList(exception.getMessage()));
    }
}
