package io.gitee.dqcer.mcdull.mdc.client.service;

import io.gitee.dqcer.mcdull.mdc.client.service.def.ConfigurationApiDef;
import io.gitee.dqcer.mcdull.mdc.client.service.hystrix.CustomPropertyApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *
 * @author dqcer
 * @since 2022/11/01 22:11:00
 */
@FeignClient(
        value = "${api:mdcName}",
        contextId = "configurationApi",
        fallback = CustomPropertyApiHystrix.class)
public interface ConfigurationApi extends ConfigurationApiDef {

}
