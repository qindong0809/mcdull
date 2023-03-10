package io.gitee.dqcer.mcdull.framework.base.exception;


import io.gitee.dqcer.mcdull.framework.base.wrapper.ICode;

/**
 * 业务异常
 *
 * @author dqcer
 * @since  17:49 2021/3/5
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected ICode code;

	public void setCode(ICode code) {
		this.code = code;
	}

	public ICode getCode() {
		return code;
	}

	public BusinessException() {
		super();
	}

	public BusinessException(ICode codeEnum) {
		super(codeEnum == null ? "" : String.format("code=%d,message=%s", codeEnum.getCode(), codeEnum.getMessage()));
		this.code = codeEnum;
	}


	public BusinessException(String message) {
		super(message);
	}


	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}


	public BusinessException(Throwable cause) {
		super(cause);
	}

	protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
