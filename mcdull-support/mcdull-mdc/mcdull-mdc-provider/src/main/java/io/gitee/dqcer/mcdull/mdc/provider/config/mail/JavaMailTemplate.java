package io.gitee.dqcer.mcdull.mdc.provider.config.mail;

import cn.hutool.extra.mail.MailUtil;
import org.springframework.stereotype.Service;


/**
 * JavaMail邮件发送者实现类
 *
 * @author dqcer
 * @since 2022/12/20
 */
@Service
public class JavaMailTemplate implements MailTemplate {



	@Override
	public void sendSimpleMail(String to, String subject, String content) {
		MailUtil.send(to, subject, content, false);
	}
}
