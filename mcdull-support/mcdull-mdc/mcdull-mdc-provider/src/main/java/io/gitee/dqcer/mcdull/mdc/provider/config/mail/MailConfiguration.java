package io.gitee.dqcer.mcdull.mdc.provider.config.mail;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

/**
 * 邮件配置
 *
 * @author dqcer
 * @since 2022/12/20
 */
@Configuration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailConfiguration {

	@Resource
	private JavaMailSender mailSender;

	@Resource
	private MailProperties mailProperties;

	@Bean
	@ConditionalOnBean({MailProperties.class, JavaMailSender.class})
	public MailTemplate mailTemplate() {
		return new JavaMailTemplate(mailSender,mailProperties);
	}
}
