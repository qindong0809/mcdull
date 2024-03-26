package io.gitee.dqcer.mcdull.mdc.client.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 邮件服务api
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface EmailApiDef {

    /**
     * 发送电子邮件
     *
     * @param sendTo  发送到
     * @param subject 主题
     * @param text    文本
     * @return {@link Result}<{@link Boolean}>
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC + "/mail/send")
    ResultApi<Boolean> sendEmail(@RequestParam("sendTo") String sendTo,
                                 @RequestParam("subject") String subject,
                                 @RequestParam("text") String text);

    /**
     * 用字节发送电子邮件
     *
     * @param bytes    bytes
     * @param fileName fileName
     * @param sendTo   sendTo
     * @param subject  subject
     * @param text     text
     * @return {@link Result}<{@link Boolean}>
     */
    @PostMapping({GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC +  "/mail/send-with-bytes"})
    ResultApi<Boolean> sendEmailWithBytes(@RequestBody byte[] bytes,
                                       @RequestParam("fileName") String fileName,
                                       @RequestParam("sendTo") String sendTo,
                                       @RequestParam("subject") String subject,
                                       @RequestParam("text") String text);

}
