package com.dqcer.mcdull.mdc;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.client.dto.MailClientDTO;
import com.dqcer.mcdull.mdc.provider.MetaDataContentApplication;
import com.dqcer.mcdull.mdc.provider.config.mail.MailTemplate;
import com.dqcer.mcdull.mdc.provider.web.service.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@ActiveProfiles("dev")
@SpringBootTest(classes = {MetaDataContentApplication.class})
public class MailTest {

    @Resource
    private MailTemplate mailTemplate;

    @Resource
    private MailService mailService;

    @Test
    void testMailTemplateError() {
        Assertions.assertThrows(MailException.class,  () -> {
            mailTemplate.sendSimpleMail("", "test", "hello word mail");
        });
    }

    @Test
    void testHtml() throws MessagingException {
        String s = "hello word:     content";
        s = s.replaceAll("\\u0020", "&nbsp;");
        s = s.replaceAll("\\u3000", "&nbsp;");
        s = s.replaceAll("\\u00A0", "&nbsp;");
        mailTemplate.sendHtmlMail("dqcer@sina.com", "subject", s);
    }

    @Test
    void testSendMailService() {
        MailClientDTO dto = new MailClientDTO()
                .setTo("dqcer@sina.com").setSubject("subject").setContent("hello word:     content");
        Result<Boolean> result = mailService.send(dto);
        Assertions.assertTrue(result.getData());
    }
}
