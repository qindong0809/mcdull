package io.gitee.dqcer.mcdull.mdc;

import cn.hutool.core.io.FileUtil;
import io.gitee.dqcer.mcdull.framework.web.config.ThreadPoolConfig;
import io.gitee.dqcer.mcdull.mdc.provider.MetaDataContentApplication;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import jakarta.annotation.Resource;

//@ContextConfiguration(classes = ThreadPoolConfig.class)
//@ActiveProfiles("dev")
//@SpringBootTest(classes = {MetaDataContentApplication.class})
public class MailTest {


    @Resource
    private EmailServiceImpl emailServiceImpl;

//    @Test
    void testMailTemplateError() {
        Assertions.assertTrue(emailServiceImpl.sendEmail("xxx@sina.com", "test", "hello word mail"));
    }

//    @Test
    void testSendMailService() {
        byte[] bytes = FileUtil.readBytes("D:\\var\\log\\mcdull-mdc-provider\\2023-11-29\\info\\info-0.log");
        boolean isOk = emailServiceImpl.sendEmailWithBytes(bytes, "info.log", "derrek@snapmail.cc", "subject", "content");
        Assertions.assertTrue(isOk);
    }
}
