package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.AuthApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.AuthApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = SystemConstant.SERVICE_NAME, contextId = "AuthApi", fallback = AuthApiHystrix.class)
public interface AuthApi extends AuthApiDef {
}
