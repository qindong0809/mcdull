package io.gitee.dqcer.mcdull.framework.base.wrapper;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 返回码实现
 *
 * @author dqcer
 * @since 2022/01/11
 */
public enum CodeEnum implements ICode , IEnum<Integer> {


	/**
	 * 操作成功
	 */
	SUCCESS(200, "Success"),

	/**
	 * 服务异常
	 */
	INTERNAL_SERVER_ERROR(500, "system.internal.server.error"),

	/**
	 * 服务不可用
	 */
	SERVICE_UNAVAILABLE(503, "服务不可用: {0}"),

	/**
	 * 404
	 */
	NOT_FOUND(404, "找不到资源"),

	/**
	 * 客户端已主动退出
	 */
	LOGOUT(405, "客户端已主动退出"),

	/**
	 * 权限不足，此接口需要具备相应的权限才能访问
	 */
	POWER_CHECK_MODULE(403, "system.permission.denied"),

	/**
	 * 无权限
	 */
	UN_AUTHORIZATION(401, "system.authentication.failure"),

	/**
	 * 异地登录
	 */
	OTHER_LOGIN(402, "异地登录"),

	/**
	 * 认证过期
	 */
	TIMEOUT_LOGIN(403, "system.authentication.expired"),


	/**
	 * sql语法错误
	 */
	SQL_SYNTAX_ERROR(999510, "sql异常: {0}"),

	/**
	 * 数据库错误
	 */
	DB_ERROR(999511, "数据库实际影响行数与预期不同"),

	/**
	 * 锁定超时
	 */
	LOCK_TIMEOUT(999512, "锁定超时"),

	/**
	 * feign 调用异常
	 */
	FEIGN_BIZ(999520, "上游服务调用异常： {0}"),


	/**
	 * 请求方法被禁止
	 */
	METHOD_NOT_ALLOWED(999430, "请求方法被禁止"),

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
		init(code, message);
		this.code = code;
		this.message = message;
	}
}
