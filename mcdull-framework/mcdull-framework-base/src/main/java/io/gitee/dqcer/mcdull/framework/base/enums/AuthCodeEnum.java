package io.gitee.dqcer.mcdull.framework.base.enums;

import io.gitee.dqcer.mcdull.framework.base.wrapper.ICode;

/**
 * 返回码实现
 *
 * @author dqcer
 * @version 2022/01/11
 */
public enum AuthCodeEnum implements ICode, IEnum<Integer> {


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

	AuthCodeEnum(int code, String message) {
		init(code, message);
		this.code = code;
		this.message = message;
	}
}
