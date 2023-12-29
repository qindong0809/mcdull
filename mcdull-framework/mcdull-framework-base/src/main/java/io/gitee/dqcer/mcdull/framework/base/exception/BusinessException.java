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

	private String messageCode;

	private Object[] args;

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

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
		super(codeEnum == null ? "" : codeEnum.getMessage());
		this.code = codeEnum;
	}


	public BusinessException(String messageCode) {
		super(messageCode);
		this.messageCode = messageCode;
	}

	public BusinessException(String messageCode, Object[] args) {
		super(messageCode);
		this.args = args;
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
