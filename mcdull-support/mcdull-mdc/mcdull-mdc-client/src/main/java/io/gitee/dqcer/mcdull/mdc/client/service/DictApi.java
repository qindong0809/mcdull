package io.gitee.dqcer.mcdull.mdc.client.service;

import io.gitee.dqcer.mcdull.mdc.client.service.def.DictApiDef;
import io.gitee.dqcer.mcdull.mdc.client.service.hystrix.DictClientApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * sys dict客户服务
 *
 * @author dqcer
 * @since 2022/11/01 22:11:00
 */
@FeignClient(
        value = "${api:mdcName}",
        contextId = "dictClientService",
        fallback = DictClientApiHystrix.class)
public interface DictApi extends DictApiDef {

}
