package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.mail.MailUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IEmailSendHistoryService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * 邮件服务
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Service
public class EmailServiceImpl implements IEmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private IEmailSendHistoryService emailSendHistoryService;

    @Override
    public boolean sendEmail(String sendTo, String subject, String text) {
        threadPoolTaskExecutor.submit(() -> {
            LogHelp.info(log, "sendEmail. sendTo: {}, subject: {}", sendTo, subject);
            try {
                emailSendHistoryService.insert(ListUtil.of(sendTo), null, subject, text);
                MailUtil.send(sendTo, subject, text, false);
            } catch (Exception e) {
                LogHelp.error(log, "send error. sendTo: {}, subject: {}", sendTo, subject, e);
            }
        });
        return true;
    }

    @Override
    public boolean sendEmailHtml(String sendTo, String subject, String text) {
        threadPoolTaskExecutor.submit(() -> {
            LogHelp.info(log, "sendEmail. sendTo: {}, subject: {}", sendTo, subject);
            try {
                emailSendHistoryService.insert(ListUtil.of(sendTo), null, subject, text);
                MailUtil.send(sendTo, subject, text, true);
            } catch (Exception e) {
                LogHelp.error(log, "send error. sendTo: {}, subject: {}", sendTo, subject, e);
            }
        });
        return true;
    }


    @Override
    public boolean sendEmailWithBytes(byte[] bytes, String fileName, String sendTo, String subject, String text) {
        threadPoolTaskExecutor.submit(() -> {
            if (log.isInfoEnabled()) {
                log.info("sendEmailWithBytes. sendTo: {}, subject: {}, fileName: {}", sendTo, subject, fileName);
            }
            try {
                // TODO: 2024/3/25 send history save 
                MailUtil.send(sendTo, subject, text, false, FileUtil.writeBytes(bytes, new File(fileName)));
            } catch (Exception e) {
                log.error("send error.", e);
            }
        });
        return true;
    }
}
