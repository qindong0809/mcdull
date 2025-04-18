package io.gitee.dqcer.mcdull.system.client.service;

import io.gitee.dqcer.mcdull.system.client.service.def.DeptApiDef;
import io.gitee.dqcer.mcdull.system.client.service.hystrix.DeptApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${mcdull.feign.uac}", contextId = "DeptAip", fallback = DeptApiHystrix.class)
public interface DeptAip extends DeptApiDef {
}
