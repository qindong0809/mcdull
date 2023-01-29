package io.gitee.dqcer.mcdull.mdc.client.api;

import io.gitee.dqcer.mcdull.mdc.client.dto.MailClientDTO;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping(GlobalConstant.INNER_API + "/mail/send")
    Result<Boolean> send(@RequestBody @Valid MailClientDTO dto);

}
