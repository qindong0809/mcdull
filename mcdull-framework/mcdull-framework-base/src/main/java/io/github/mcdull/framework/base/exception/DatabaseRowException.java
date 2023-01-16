package io.gitee.dqcer.framework.base.exception;


/**
 * 数据库操作异常
 *
 * @author dqcer
 * @version  17:49 2021/3/5
 */
public class DatabaseRowException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public DatabaseRowException() {
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
