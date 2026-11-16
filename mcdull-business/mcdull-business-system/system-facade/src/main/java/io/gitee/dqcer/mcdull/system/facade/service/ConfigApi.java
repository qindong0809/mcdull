package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.ConfigApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.ConfigApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = SystemConstant.SERVICE_NAME, contextId = "ConfigApi", fallback = ConfigApiHystrix.class)
public interface ConfigApi extends ConfigApiDef {
}
