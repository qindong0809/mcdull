package com.dqcer.mcdull.mdc.provider.web.feign;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.client.api.MailServiceApi;
import com.dqcer.mcdull.mdc.client.dto.MailClientDTO;
import com.dqcer.mcdull.mdc.provider.web.service.MailService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 邮件服务 feign 实现层
 *
 * @author dqcer
 * @date 2022/12/26
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
}
