package io.gitee.dqcer.mcdull.mdc.client.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.mdc.client.service.ConfigurationApi;
import org.springframework.stereotype.Component;

/**
 * @author dqcer
 * @since 2024/03/26
 */
@Component
public class ConfigurationApiHystrix implements ConfigurationApi {

    @Override
    public InnerResult<String> getDomainName() {
        return InnerResult.error("hystrix fall back!");
    }
}
