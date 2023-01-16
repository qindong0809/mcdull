package io.gitee.dqcer.framework.base.exception;

/**
 * 服务错误异常
 *
 * @author dqcer
 * @version 2022/04/22
 */
public class FeignServiceErrorException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public FeignServiceErrorException() {
        super();
    }

    public FeignServiceErrorException(String message) {
        super(message);
    }


    public FeignServiceErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeignServiceErrorException(Throwable cause) {
        super(cause);
    }

    protected FeignServiceErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
