package io.gitee.dqcer.mcdull.mdc.client.service;

import io.gitee.dqcer.mcdull.mdc.client.service.def.DictTypeApiDef;
import io.gitee.dqcer.mcdull.mdc.client.service.hystrix.DictClientTypeApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * sys dict客户服务
 *
 * @author dqcer
 * @since 2022/11/01 22:11:00
 */
@FeignClient(
        value = "${api:mdcName}",
        contextId = "dictApi",
        fallback = DictClientTypeApiHystrix.class)
public interface DictTypeApi extends DictTypeApiDef {

}
