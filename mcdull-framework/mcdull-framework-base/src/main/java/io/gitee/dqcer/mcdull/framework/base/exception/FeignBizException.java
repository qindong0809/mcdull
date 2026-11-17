package io.gitee.dqcer.mcdull.framework.base.exception;


import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;

/**
 * feign业务异常
 *
 * @author dqcer
 * @since  2022/04/22
 */
public class FeignBizException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private InnerResult<?> result;

    public FeignBizException() {
        super();
    }

    public FeignBizException(InnerResult<?> result) {
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

    public InnerResult<?> getResult() {
        return result;
    }

    public void setResult(InnerResult<?> result) {
        this.result = result;
    }
}
