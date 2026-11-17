package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.AuthApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.AuthApiHystrix;

@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "AuthApi", fallback = AuthApiHystrix.class)
public interface AuthAPi extends AuthApiDef {
}
