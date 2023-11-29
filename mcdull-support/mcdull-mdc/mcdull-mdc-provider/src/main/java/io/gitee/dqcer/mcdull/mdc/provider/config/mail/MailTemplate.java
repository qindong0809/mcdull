package io.gitee.dqcer.mcdull.mdc.provider.config.mail;

/**
 * 邮件通用操作
 *
 * @author dqcer
 * @since 2022/12/20
 */
public interface MailTemplate {

	/**
	 * 发送简单邮件
	 *
	 * @param to      收件人地址
	 * @param subject 主题
	 * @param content 邮件内容
	 */
	void sendSimpleMail(String to, String subject, String content);
}
