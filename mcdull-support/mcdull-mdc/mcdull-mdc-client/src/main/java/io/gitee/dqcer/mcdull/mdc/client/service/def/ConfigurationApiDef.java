package io.gitee.dqcer.mcdull.mdc.client.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.mdc.client.vo.EmailTemplateClientVO;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author dqcer
 * @since 2024/03/26
 */
public interface ConfigurationApiDef {


    /**
     * get
     *
     * @param code 密码
     * @return {@link ResultApi}<{@link EmailTemplateClientVO}>
     */
    @GetMapping({GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC +  "/configuration/domain-name"})
    ResultApi<String> getDomainName();

}
