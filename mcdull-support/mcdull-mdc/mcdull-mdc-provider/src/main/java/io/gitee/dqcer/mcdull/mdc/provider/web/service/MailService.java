package io.gitee.dqcer.mcdull.mdc.provider.web.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.mail.MailUtil;
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
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public boolean sendEmail(String sendTo, String subject, String text) {
        threadPoolTaskExecutor.submit(() -> {
            if (log.isInfoEnabled()) {
                log.info("sendEmail. sendTo: {}, subject: {}", sendTo, subject);
            }
            try {
                MailUtil.send(sendTo, subject, text, false);
            } catch (Exception e) {
                log.error("send error.", e);
            }
        });
        return true;
    }

    public boolean sendEmailWithBytes(byte[] bytes, String fileName, String sendTo, String subject, String text) {
        threadPoolTaskExecutor.submit(() -> {
            if (log.isInfoEnabled()) {
                log.info("sendEmailWithBytes. sendTo: {}, subject: {}, fileName: {}", sendTo, subject, fileName);
            }
            try {
                MailUtil.send(sendTo, subject, text, false, FileUtil.writeBytes(bytes, new File(fileName)));
            } catch (Exception e) {
                log.error("send error.", e);
            }
        });
        return true;
    }
}
