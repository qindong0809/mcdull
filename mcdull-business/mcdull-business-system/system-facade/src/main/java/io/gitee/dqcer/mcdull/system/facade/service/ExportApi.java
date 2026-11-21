package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.def.ExportApiDef;

/**
 * 导出上下文对外 RPC 接口
 */
@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "ExportApi")
public interface ExportApi extends ExportApiDef {
}
