package io.gitee.dqcer.mcdull.mdc.client.api;

import io.gitee.dqcer.mcdull.mdc.client.dto.MailClientDTO;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 邮件服务api
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface MailServiceApi {

    /**
     * 发送
     *
     * @param dto dto
     * @return {@link Result}<{@link Boolean}>
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC + "/mail/send")
    Result<Boolean> send(@RequestBody @Valid MailClientDTO dto);

    /**
     * 发送电子邮件
     *
     * @param sendTo  发送到
     * @param subject 主题
     * @param text    文本
     * @return {@link Result}<{@link Boolean}>
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC + "/mail/send")
    Result<Boolean> sendEmail(
            @RequestParam("sendTo") String sendTo,
            @RequestParam("subject") String subject,
            @RequestParam("text") String text);

}
