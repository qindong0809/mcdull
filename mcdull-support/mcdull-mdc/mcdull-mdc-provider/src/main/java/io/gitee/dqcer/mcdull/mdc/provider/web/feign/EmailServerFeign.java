package io.gitee.dqcer.mcdull.mdc.provider.web.feign;

import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.mdc.client.service.def.EmailApiDef;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.IEmailService;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * 邮件服务 feign 实现层
 *
 * @author dqcer
 * @since 2022/12/26
 */
@RestController
public class EmailServerFeign implements EmailApiDef {

    @Resource
    private IEmailService emailService;

    @Override
    public ResultApi<Boolean> sendEmail(String sendTo, String subject, String text) {
        return ResultApi.success(emailService.sendEmail(sendTo, subject, text));
    }

    @Override
    public ResultApi<Boolean> sendEmailWithBytes(byte[] bytes, String fileName, String sendTo, String subject,
                                              String text) {
        return ResultApi.success(emailService.sendEmailWithBytes(bytes, fileName, sendTo, subject, text));
    }
}
