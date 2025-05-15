package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.system.facade.service.def.UserApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.UserApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${mcdull.feign.uac}", contextId = "UserAip", fallback = UserApiHystrix.class)
public interface UserAip extends UserApiDef {
}
