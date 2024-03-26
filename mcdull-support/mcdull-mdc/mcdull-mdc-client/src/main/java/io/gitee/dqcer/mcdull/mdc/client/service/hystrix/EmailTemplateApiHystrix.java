package io.gitee.dqcer.mcdull.mdc.client.service.hystrix;

import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
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
    public ResultApi<EmailTemplateClientVO> get(String code) {
        return ResultApi.error("hystrix fall back!");
    }
}
