package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.web.basic.GenericLogic;
import io.gitee.dqcer.mcdull.uac.provider.model.bo.EmailConfigBO;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.EmailTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IEmailSendHistoryService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IEmailService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ISysInfoService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * Email Service
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Service
public class EmailServiceImpl
        extends GenericLogic implements IEmailService {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private IEmailSendHistoryService emailSendHistoryService;

    @Resource
    private ISysInfoService sysInfoService;

    @Override
    public boolean sendEmail(EmailTypeEnum typeEnum, String sendTo, String subject, String text) {
        MailAccount mailAccount = this.getMailAccount();
        threadPoolTaskExecutor.submit(() -> {
            LogHelp.info(log, "sendEmail. sendTo: {}, subject: {}", sendTo, subject);
            try {
                MailUtil.send(mailAccount, sendTo, subject, text, false);
            } catch (Exception e) {
                LogHelp.error(log, "send error. sendTo: {}, subject: {}", sendTo, subject, e);
            }finally {
                emailSendHistoryService.insert(typeEnum, sendTo, subject, text);
            }
        });
        return true;
    }

    public MailAccount getMailAccount() {
        EmailConfigBO emailConfigBO = sysInfoService.getEmailConfig();
        if (emailConfigBO == null) {
            throw new RuntimeException(I18nConstants.DATA_NOT_EXIST);
        }
        return new MailAccount()
                .setHost(emailConfigBO.getHost())
                .setAuth(true)
                .setFrom(StrUtil.format("<{}>", emailConfigBO.getUsername()))
                .setUser(emailConfigBO.getUsername())
                .setPass(emailConfigBO.getPassword())
                .setSslEnable(true);
    }

    @Override
    public void sendEmailHtml(EmailTypeEnum emailTypeEnum, String sendTo, String subject, String text) {
        MailAccount mailAccount = this.getMailAccount();
        threadPoolTaskExecutor.submit(() -> {
            LogHelp.info(log, "sendEmail. sendTo: {}, subject: {}", sendTo, subject);
            try {
                MailUtil.send(mailAccount, sendTo, subject, text, true);
            } catch (Exception e) {
                LogHelp.error(log, "send error. sendTo: {}, subject: {}", sendTo, subject, e);
            }finally {
                emailSendHistoryService.insert(emailTypeEnum, sendTo, subject, text);
            }
        });
    }


    @Override
    public boolean sendEmailWithBytes(byte[] bytes, String fileName, String sendTo, String subject, String text) {
        threadPoolTaskExecutor.submit(() -> {
            LogHelp.info(log, "sendEmailWithBytes. sendTo: {}, subject: {}, fileName: {}", sendTo, subject, fileName);
            try {
                // TODO: 2024/3/25 send history save 
                MailUtil.send(sendTo, subject, text, false, FileUtil.writeBytes(bytes, new File(fileName)));
            } catch (Exception e) {
                LogHelp.error(log, "send error. sendTo: {}, subject: {}, fileName: {}", sendTo, subject, fileName, e);
            }
        });
        return true;
    }
}
