package com.dqcer.mcdull.framework.web.advice;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.framework.base.wrapper.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

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
        log.error("sql语法异常: ", exception);
        return Result.error(ResultCode.SQL_SYNTAX_ERROR);
    }
}
