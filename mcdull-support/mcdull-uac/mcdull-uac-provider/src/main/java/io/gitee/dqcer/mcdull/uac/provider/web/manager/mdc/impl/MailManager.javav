package io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc.impl;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.mdc.client.service.EmailApi;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc.IMailManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dqcer
 */
@Service
public class MailManager implements IMailManager {

    @Resource
    private EmailApi mailClientService;

    @Override
    public boolean sendEmail(String sendTo, String subject, String text) {
        if (StrUtil.isBlank(sendTo) || StrUtil.isBlank(subject) || StrUtil.isBlank(text)) {
            throw new IllegalArgumentException("sendTo/subject/text is blank.");
        }
        mailClientService.sendEmail(sendTo, subject, text);
        return false;
    }
}
