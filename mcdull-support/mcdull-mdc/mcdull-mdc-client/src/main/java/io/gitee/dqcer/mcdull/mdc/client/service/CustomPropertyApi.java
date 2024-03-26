package io.gitee.dqcer.mcdull.mdc.client.service;

import io.gitee.dqcer.mcdull.mdc.client.service.def.CustomPropertyApiDef;
import io.gitee.dqcer.mcdull.mdc.client.service.hystrix.CustomPropertyApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *
 * @author dqcer
 * @since 2022/11/01 22:11:00
 */
@FeignClient(
        value = "${api:mdcName}",
        contextId = "customPropertyClientService",
        fallback = CustomPropertyApiHystrix.class)
public interface CustomPropertyApi extends CustomPropertyApiDef {

}
