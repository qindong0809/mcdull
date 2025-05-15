package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.system.facade.service.def.DeptApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.DeptApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${mcdull.feign.uac}", contextId = "DeptAip", fallback = DeptApiHystrix.class)
public interface DeptAip extends DeptApiDef {
}
