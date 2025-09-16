package io.gitee.dqcer.mcdull.framework.base.wrapper;

/**
 * 返回码实现
 *
 * @author dqcer
 * @since 2022/01/11
 */
public enum CodeEnum implements ICode {

	/**
	 * 操作成功
	 */
	SUCCESS(200, "Success"),

	/**
	 * 服务异常
	 */
	INTERNAL_SERVER_ERROR(500, "system.internal.server.error"),

	/**
	 * 权限不足，此接口需要具备相应的权限才能访问
	 */
	POWER_CHECK_MODULE(403, "system.permission.denied"),

	/**
	 * 无权限
	 */
	UN_AUTHORIZATION(401, "system.authentication.failure"),

	/**
	 * 认证过期
	 */
	TIMEOUT_LOGIN(403, "system.authentication.expired"),

	/**
	 * 参数异常
	 */
	ERROR_PARAMETERS(999450, "system.validation.failed"),


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

	CodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
