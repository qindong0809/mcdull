package com.dqcer.framework.base.exception;


import com.dqcer.framework.base.wrapper.Result;

/**
 * feign业务异常
 *
 * @author dqcer
 * @version  2022/04/22
 */
public class FeignBizException extends RuntimeException{

    private Result result;

    public FeignBizException() {
        super();
    }

    public FeignBizException(Result result) {
        this.result = result;
    }

    public FeignBizException(String message) {
        super(message);
    }

    public FeignBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeignBizException(Throwable cause) {
        super(cause);
    }

    protected FeignBizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}