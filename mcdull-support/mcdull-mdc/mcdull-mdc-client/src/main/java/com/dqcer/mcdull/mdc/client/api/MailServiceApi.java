package com.dqcer.mcdull.mdc.client.api;

import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.client.dto.MailClientDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 邮件服务api
 *
 * @author dqcer
 * @version 2022/10/28
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
