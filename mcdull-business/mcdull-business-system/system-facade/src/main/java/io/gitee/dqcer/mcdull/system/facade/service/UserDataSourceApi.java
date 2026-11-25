package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.UserDataSourceApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.UserDataSourceApiHystrix;

/**
 * 用户数据源对外 RPC 接口。
 * <p>
 * 单体部署时由 system-impl 的 {@code @Primary @RestController} 本地实现承接；
 * 微服务部署时由 {@code @EnableSystemFeignClients} 激活 Feign 代理走 HTTP。
 *
 * @author dqcer
 */
@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "UserDataSourceApi",
        fallback = UserDataSourceApiHystrix.class, primary = true)
public interface UserDataSourceApi extends UserDataSourceApiDef {
}
