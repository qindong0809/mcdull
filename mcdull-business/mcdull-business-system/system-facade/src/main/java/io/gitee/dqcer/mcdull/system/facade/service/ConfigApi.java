package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.ConfigApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.ConfigApiHystrix;

/**
 * 配置能力对外 RPC 接口
 *
 * @author dqcer
 * @since 2026-11-20
 */
@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "ConfigApi", fallback = ConfigApiHystrix.class)
public interface ConfigApi extends ConfigApiDef {
}
