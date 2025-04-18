package io.gitee.dqcer.mcdull.system.client.service;

import io.gitee.dqcer.mcdull.system.client.service.def.DeptApiDef;
import io.gitee.dqcer.mcdull.system.client.service.hystrix.ConfigApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${mcdull.feign.uac}", contextId = "ConfigAip", fallback = ConfigApiHystrix.class)
public interface ConfigAip extends DeptApiDef {
}
