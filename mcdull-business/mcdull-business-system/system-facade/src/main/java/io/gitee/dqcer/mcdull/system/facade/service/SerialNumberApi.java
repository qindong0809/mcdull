package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.SerialNumberApiDef;

/**
 * 流水号能力对外 RPC 接口
 */
@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "SerialNumberApi")
public interface SerialNumberApi extends SerialNumberApiDef {
}
