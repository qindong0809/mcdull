package io.gitee.dqcer.mcdull.workflow.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchStartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BusinessKeyBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.StartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.InstanceStatusVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.StartProcessResult;
import io.gitee.dqcer.mcdull.workflow.facade.service.WorkflowInstanceApi;
import io.gitee.dqcer.mcdull.workflow.provider.application.WorkflowApplicationService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程实例 provider。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Primary
@RestController
public class WorkflowInstanceApiDefImpl implements WorkflowInstanceApi {

    @Resource
    private WorkflowApplicationService workflowApplicationService;

    @Override
    public InnerResult<StartProcessResult> startProcess(StartProcessDTO dto) {
        return InnerResult.success(workflowApplicationService.startProcess(dto));
    }

    @Override
    public InnerResult<BatchResult<StartProcessResult>> batchStartProcesses(BatchStartProcessDTO dto) {
        return InnerResult.success(workflowApplicationService.batchStartProcesses(dto));
    }

    @Override
    public InnerResult<BatchQueryResult<ProcessBusinessKey, InstanceStatusVO>> batchGetInstancesByBusinessKeys(
            BusinessKeyBatchQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetInstancesByBusinessKeys(dto));
    }

    @Override
    public InnerResult<BatchQueryResult<ProcessBusinessKey, InstanceStatusVO>> batchGetInstanceStatuses(
            BusinessKeyBatchQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetInstanceStatuses(dto));
    }
}
