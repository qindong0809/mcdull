package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.system.facade.service.def.DeptApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.ConfigApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${mcdull.feign.uac}", contextId = "ConfigAip", fallback = ConfigApiHystrix.class)
public interface ConfigAip extends DeptApiDef {
}
