package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.RoleApiDef;

/**
 * 角色能力对外 RPC 接口
 *
 * @author dqcer
 * @since 2026-11-20
 */
@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "RoleApi")
public interface RoleApi extends RoleApiDef {
}
