package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.system.facade.service.def.RoleApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.RoleApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "${mcdull.feign.uac}", contextId = "RoleAip", fallback = RoleApiHystrix.class)
public interface RoleAip extends RoleApiDef {
}
