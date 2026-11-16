package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.system.facade.service.def.AuthApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.def.DeptApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.AuthApiHystrix;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.ConfigApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${mcdull.feign.uac}", contextId = "AuthApi", fallback = AuthApiHystrix.class)
public interface AuthApi extends AuthApiDef {
}
