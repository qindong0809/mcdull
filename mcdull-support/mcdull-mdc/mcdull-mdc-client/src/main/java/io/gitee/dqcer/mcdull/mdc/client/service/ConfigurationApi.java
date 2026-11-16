package io.gitee.dqcer.mcdull.mdc.client.service;

import io.gitee.dqcer.mcdull.mdc.client.constants.MdcConstant;
import io.gitee.dqcer.mcdull.mdc.client.service.def.ConfigurationApiDef;
import io.gitee.dqcer.mcdull.mdc.client.service.hystrix.ConfigurationApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *
 * @author dqcer
 * @since 2022/11/01 22:11:00
 */
@FeignClient(
        value = MdcConstant.SERVICE_NAME,
        contextId = "configurationApi",
        fallback = ConfigurationApiHystrix.class)
public interface ConfigurationApi extends ConfigurationApiDef {

}
