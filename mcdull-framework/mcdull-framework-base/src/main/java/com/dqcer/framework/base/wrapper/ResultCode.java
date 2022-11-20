package com.dqcer.framework.base.wrapper;

import com.dqcer.framework.base.dict.IDict;
import org.springframework.http.HttpStatus;

/**
 * 返回码实现
 *
 * @author dqcer
 * @version 2022/01/11
 */
public enum ResultCode implements IResultCode , IDict<Integer> {


	/**
	 * 操作成功
	 */
	SUCCESS(0, "操作成功"),

	/**
	 * 服务异常
	 */
	ERROR_UNKNOWN(999500, "未知异常"),

	/**
	 * sql语法错误
	 */
	SQL_SYNTAX_ERROR(999510, "sql语法异常"),

	/**
	 * 无权限
	 */
	UN_AUTHORIZATION(999401, "无权限"),

	/**
	 * 异地登录
	 */
	OTHER_LOGIN(999402, "异地登录"),

	/**
	 * 登录超时
	 */
	TIMEOUT_LOGIN(999403, "token过期"),

	/**
	 * 404
	 */
	NOT_FOUND(999404, "找不到资源"),

	/**
	 * 没有traceId
	 */
	NOT_TRACE_ID(999415, "没有traceId"),

	/**
	 * 请求方法被禁止
	 */
	METHOD_NOT_ALLOWED(999405, "请求方法被禁止"),

	/**
	 * 参数异常
	 */
	ERROR_PARAMETERS(999450, "参数异常"),

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

	ResultCode(int code, String message) {
		init(code, message);
		this.code = code;
		this.message = message;
	}
}
