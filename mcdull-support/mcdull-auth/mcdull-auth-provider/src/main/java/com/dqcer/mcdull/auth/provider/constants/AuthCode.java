package com.dqcer.mcdull.auth.provider.constants;

import com.dqcer.framework.base.dict.IDict;
import com.dqcer.framework.base.wrapper.IResultCode;

/**
 * 返回码实现
 *
 * @author dqcer
 * @date 2022/01/11
 */
public enum AuthCode implements IResultCode, IDict<Integer> {


	/**
	 * 账号/密码错误
	 */
	NOT_EXIST(9999440, "账号/密码错误"),

	/**
	 * 账号已停用
	 */
	DISABLE(999441, "账号已停用"),

	;
	/**
	 * 状态码
	 */
	final int code;

	/**
	 * 消息
	 */
	final String message;

	@Override
	public Integer getCode() {
		return code;
	}

	/**
	 * message
	 *
	 * @return {@link String}
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

	AuthCode(int code, String message) {
		init(code, message);
		this.code = code;
		this.message = message;
	}
}
