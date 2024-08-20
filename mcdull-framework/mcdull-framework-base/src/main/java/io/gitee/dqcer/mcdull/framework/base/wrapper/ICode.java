package io.gitee.dqcer.mcdull.framework.base.wrapper;

/**
 * 返回码接口，业务系统自定义实现，翻译也可统一由客户端进行维护
 *
 * @author dqcer
 * @since 2022/07/26
 */
public interface ICode {

	/**
	 * 返回码
	 *
	 * @return int
	 */
	Integer getCode();

	/**
	 * message
	 *
	 * @return {@link String}
	 */
	String getMessage();
}
