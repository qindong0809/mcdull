package io.gitee.dqcer.mcdull.workflow.web.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.enums.TaskActionEnum;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.CursorPageVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalActionDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalDiagramQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalHistoryQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalStatusQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.CancelApprovalDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionVersionBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BusinessKeyBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DiagramQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.StartApprovalDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.StartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCursorQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.ApprovalDiagramVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.ApprovalStatusVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVersionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DiagramVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.HistoryVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.LatestApprovalVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.StartProcessResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskHandleResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskVO;
import io.gitee.dqcer.mcdull.workflow.facade.service.WorkflowApprovalApi;
import io.gitee.dqcer.mcdull.workflow.provider.application.WorkflowApplicationService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 审批场景 provider。
 *
 * <p>所有场景接口统一委托 {@link WorkflowApplicationService}，保持逻辑单一来源。</p>
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Primary
@RestController
public class WorkflowApprovalApiDefImpl implements WorkflowApprovalApi {

    @Resource
    private WorkflowApplicationService workflowApplicationService;

    @Override
    public InnerResult<List<DefinitionVO>> batchGetDefinitions(DefinitionQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetDefinitions(dto));
    }

    @Override
    public InnerResult<BatchQueryResult<String, DefinitionVersionVO>> batchGetDefinitionVersions(
            DefinitionVersionBatchDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetDefinitionVersions(dto));
    }

    @Override
    public InnerResult<List<DiagramVO>> batchGetDefinitionDiagrams(DiagramQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetDiagrams(dto));
    }

    @Override
    public InnerResult<StartProcessResult> startApproval(StartApprovalDTO dto) {
        StartProcessDTO target = new StartProcessDTO();
        BeanUtils.copyProperties(dto, target);
        return InnerResult.success(workflowApplicationService.startProcess(target));
    }

    @Override
    public InnerResult<List<ApprovalStatusVO>> batchGetApprovalStatus(ApprovalStatusQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetApprovalStatus(dto));
    }

    @Override
    public InnerResult<BatchQueryResult<ProcessBusinessKey, LatestApprovalVO>> batchGetLatestApprovals(
            BusinessKeyBatchQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetLatestApprovals(dto));
    }

    @Override
    public InnerResult<TaskHandleResult> approve(ApprovalActionDTO dto) {
        return handleApproval(dto, TaskActionEnum.APPROVE.getCode());
    }

    @Override
    public InnerResult<TaskHandleResult> reject(ApprovalActionDTO dto) {
        return handleApproval(dto, TaskActionEnum.REJECT.getCode());
    }

    @Override
    public InnerResult<ApprovalStatusVO> cancelApproval(CancelApprovalDTO dto) {
        return InnerResult.success(workflowApplicationService.cancelApproval(dto));
    }

    @Override
    public InnerResult<List<HistoryVO>> getApprovalHistory(ApprovalHistoryQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.getApprovalHistory(dto));
    }

    @Override
    public InnerResult<ApprovalDiagramVO> getApprovalDiagram(ApprovalDiagramQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.getApprovalDiagram(dto));
    }

    @Override
    public InnerResult<CursorPageVO<TaskVO>> pageTodoTasks(TodoCursorQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.pageTodoTasks(dto));
    }

    private InnerResult<TaskHandleResult> handleApproval(ApprovalActionDTO dto, String action) {
        HandleTaskDTO target = new HandleTaskDTO();
        BeanUtils.copyProperties(dto, target);
        target.setAction(action);
        return InnerResult.success(workflowApplicationService.handleTask(target));
    }
}
