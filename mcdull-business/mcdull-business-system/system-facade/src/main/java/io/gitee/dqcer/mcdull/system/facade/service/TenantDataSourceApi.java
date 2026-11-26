package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.TenantDataSourceApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.TenantDataSourceApiHystrix;

/**
 * 租户数据源能力对外 RPC 接口。
 *
 * @author dqcer
 */
@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "TenantDataSourceApi", fallback = TenantDataSourceApiHystrix.class)
public interface TenantDataSourceApi extends TenantDataSourceApiDef {
}
