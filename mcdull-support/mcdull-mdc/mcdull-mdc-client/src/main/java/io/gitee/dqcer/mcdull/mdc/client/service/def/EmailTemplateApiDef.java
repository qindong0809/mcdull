package io.gitee.dqcer.mcdull.mdc.client.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.mdc.client.vo.EmailTemplateClientVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 电子邮件模板服务
 *
 * @author dqcer
 * @since 2024/03/26
 */
public interface EmailTemplateApiDef {


    /**
     * get
     *
     * @param code 密码
     * @return {@link ResultApi}<{@link EmailTemplateClientVO}>
     */
    @PostMapping({GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC +  "/email-template/detail"})
    ResultApi<EmailTemplateClientVO> get(@RequestParam("code") String code);

}
