package io.gitee.dqcer.mcdull.workflow.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchStartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BusinessKeyBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.StartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.InstanceStatusVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.StartProcessResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 流程实例契约。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public interface WorkflowInstanceApiDef {

    @PostMapping(WorkflowConstant.BASE_PATH + "/instances/start")
    InnerResult<StartProcessResult> startProcess(@Valid @RequestBody StartProcessDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/instances/batch-start")
    InnerResult<BatchResult<StartProcessResult>> batchStartProcesses(
            @Valid @RequestBody BatchStartProcessDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/instances/batch-get-by-business-key")
    InnerResult<BatchQueryResult<ProcessBusinessKey, InstanceStatusVO>> batchGetInstancesByBusinessKeys(
            @Valid @RequestBody BusinessKeyBatchQueryDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/instances/batch-status")
    InnerResult<BatchQueryResult<ProcessBusinessKey, InstanceStatusVO>> batchGetInstanceStatuses(
            @Valid @RequestBody BusinessKeyBatchQueryDTO dto);
}
