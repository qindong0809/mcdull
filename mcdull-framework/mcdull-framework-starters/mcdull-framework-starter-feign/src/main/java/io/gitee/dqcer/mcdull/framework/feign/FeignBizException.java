package io.gitee.dqcer.mcdull.framework.feign;


/**
 * feign业务异常
 *
 * @author dqcer
 * @since  2022/04/22
 */
public class FeignBizException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private ResultApi<?> result;

    public FeignBizException() {
        super();
    }

    public FeignBizException(ResultApi<?> result) {
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

    public ResultApi<?> getResult() {
        return result;
    }

    public void setResult(ResultApi<?> result) {
        this.result = result;
    }
}
