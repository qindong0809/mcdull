package io.gitee.dqcer.mcdull.mdc.provider.web.feign;

import io.gitee.dqcer.mcdull.mdc.client.dto.MailClientDTO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.client.api.MailServiceApi;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.MailService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 邮件服务 feign 实现层
 *
 * @author dqcer
 * @since 2022/12/26
 */
@RestController
public class MailServerFeign implements MailServiceApi {

    @Resource
    private MailService mailService;


    /**
     * 发送
     *
     * @param dto dto
     * @return {@link Result}<{@link Boolean}>
     */
    @Override
    public Result<Boolean> send(MailClientDTO dto) {
        return mailService.send(dto);
    }

    @Override
    public Result<Boolean> sendEmail(String sendTo, String subject, String text) {
        return Result.ok(mailService.sendEmail(sendTo, subject, text));
    }
}
