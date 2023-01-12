package com.dqcer.framework.base.wrapper;

import com.dqcer.framework.base.enums.IEnum;

/**
 * 返回码实现
 *
 * @author dqcer
 * @version 2022/01/11
 */
public enum CodeEnum implements ICode , IEnum<Integer> {


	/**
	 * 操作成功
	 */
	SUCCESS(200, "操作成功"),

	/**
	 * 服务异常
	 */
	INTERNAL_SERVER_ERROR(500, "未知异常: {0}"),

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
	POWER_CHECK_MODULE(403, "权限不足，此接口需要具备相应的权限才能访问"),

	/**
	 * 无权限
	 */
	UN_AUTHORIZATION(401, "无权限"),

	/**
	 * 异地登录
	 */
	OTHER_LOGIN(402, "异地登录"),

	/**
	 * 登录超时
	 */
	TIMEOUT_LOGIN(403, "token过期"),


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
	ERROR_PARAMETERS(999450, "参数异常: {0}"),

	/**
	 * 数据存在
	 */
	DATA_EXIST(999460, "数据已存在"),

	/**
	 * 数据不存在
	 */
	DATA_NOT_EXIST(999461, "数据不存在"),

	/**
	 * 错误类型
	 */
	ERROR_CONTENT_TYPE(999303, "请求头Content-Type异常"),

	/**
	 * 没有找到对应属性
	 */
	NOT_FIND_PROPERTIES_KEY(999302, "无法找到对应properties文件中对应的key"),

	/**
	 * 错误的转换
	 */
	ERROR_CONVERSION(999300, "参数接收时，类型转换异常"),


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
