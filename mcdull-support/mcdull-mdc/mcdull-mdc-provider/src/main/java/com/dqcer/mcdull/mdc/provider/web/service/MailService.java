package com.dqcer.mcdull.mdc.provider.web.service;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.client.dto.MailClientDTO;
import com.dqcer.mcdull.mdc.provider.config.mail.MailTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Resource
    private MailTemplate mailTemplate;


    /**
     * 发送
     *
     * @param dto dto
     * @return {@link Result}<{@link Boolean}>
     */
    public Result<Boolean> send(MailClientDTO dto) {
        log.info("发送 E-mail DTO: {}", dto);
        try {
            mailTemplate.sendSimpleMail(dto.getTo(), dto.getSubject(), dto.getContent());
            return Result.ok(Boolean.TRUE);
        } catch (Exception e) {
            log.error("邮件发送失败： dto： {}", dto);
        }
        return Result.ok(Boolean.FALSE);
    }
}