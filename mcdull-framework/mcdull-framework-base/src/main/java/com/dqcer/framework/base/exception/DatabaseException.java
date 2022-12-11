package com.dqcer.framework.base.exception;


import com.dqcer.framework.base.wrapper.IResultCode;

/**
 * 数据库操作异常
 *
 * @author dqcer
 * @version  17:49 2021/3/5
 */
public class DatabaseException extends RuntimeException {

	protected IResultCode code;

	public void setCode(IResultCode code) {
		this.code = code;
	}

	public IResultCode getCode() {
		return code;
	}

	public DatabaseException() {
		super();
	}

	public DatabaseException(IResultCode codeEnum) {
		super(codeEnum == null ? "" : String.format("code=%d,message=%s", codeEnum.getCode(), codeEnum.getMessage()));
		this.code = codeEnum;
	}


	public DatabaseException(String message) {
		super(message);
	}


	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}


	public DatabaseException(Throwable cause) {
		super(cause);
	}

	protected DatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
