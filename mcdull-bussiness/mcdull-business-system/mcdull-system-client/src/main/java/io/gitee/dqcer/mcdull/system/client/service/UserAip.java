package io.gitee.dqcer.mcdull.system.client.service;

import io.gitee.dqcer.mcdull.system.client.service.def.UserApiDef;
import io.gitee.dqcer.mcdull.system.client.service.hystrix.UserApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${mcdull.feign.uac}", contextId = "UserAip", fallback = UserApiHystrix.class)
public interface UserAip extends UserApiDef {
}
