package io.gitee.dqcer.mcdull.mdc.client.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.mdc.client.service.EmailTemplateApi;
import io.gitee.dqcer.mcdull.mdc.client.vo.EmailTemplateClientVO;
import org.springframework.stereotype.Component;

/**
 * @author dqcer
 * @since 2024/03/26
 */
@Component
public class EmailTemplateApiHystrix implements EmailTemplateApi {

    @Override
    public InnerResult<EmailTemplateClientVO> get(String code) {
        return InnerResult.error("hystrix fall back!");
    }
}
