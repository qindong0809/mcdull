package io.gitee.dqcer.mcdull.framework.base.exception;


import io.gitee.dqcer.mcdull.framework.base.wrapper.ICode;

/**
 * 数据库操作异常
 *
 * @author dqcer
 * @since  17:49 2021/3/5
 */
public class DatabaseRowException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	protected ICode code;

	public ICode getCode() {
		return code;
	}

	public DatabaseRowException setCode(ICode code) {
		this.code = code;
		return this;
	}

	public DatabaseRowException(ICode iCode) {
		this.code = iCode;
	}


	public DatabaseRowException(String message) {
		super(message);
	}


	public DatabaseRowException(String message, Throwable cause) {
		super(message, cause);
	}


	public DatabaseRowException(Throwable cause) {
		super(cause);
	}

	protected DatabaseRowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
