package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.UserApiDef;

/**
 * 用户能力对外 RPC 接口
 * <p>
 * 微服务模式下由消费方通过 {@link io.gitee.dqcer.mcdull.system.facade.EnableSystemFeignClients}
 * 激活 Feign 代理；单体合并部署时直接使用 {@code UserApiDefImpl} 的本地实现。
 *
 * @author dqcer
 * @since 2026-11-20
 */
@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "UserApi")
public interface UserApi extends UserApiDef {
}
