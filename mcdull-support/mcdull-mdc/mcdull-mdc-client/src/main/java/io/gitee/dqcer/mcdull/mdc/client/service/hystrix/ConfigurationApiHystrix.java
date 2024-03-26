package io.gitee.dqcer.mcdull.mdc.client.service.hystrix;

import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.mdc.client.service.ConfigurationApi;
import org.springframework.stereotype.Component;

/**
 * @author dqcer
 * @since 2024/03/26
 */
@Component
public class ConfigurationApiHystrix implements ConfigurationApi {

    @Override
    public ResultApi<String> getDomainName() {
        return ResultApi.error("hystrix fall back!");
    }
}
