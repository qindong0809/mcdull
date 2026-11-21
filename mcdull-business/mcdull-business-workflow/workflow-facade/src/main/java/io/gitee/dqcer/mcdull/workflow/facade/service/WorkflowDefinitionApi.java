package io.gitee.dqcer.mcdull.workflow.facade.service;

import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
import io.gitee.dqcer.mcdull.workflow.facade.service.def.WorkflowDefinitionApiDef;

/**
 * 流程定义对外 RPC 入口。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@RpcClient(value = WorkflowConstant.SERVICE_NAME, contextId = "WorkflowDefinitionApi")
public interface WorkflowDefinitionApi extends WorkflowDefinitionApiDef {
}
