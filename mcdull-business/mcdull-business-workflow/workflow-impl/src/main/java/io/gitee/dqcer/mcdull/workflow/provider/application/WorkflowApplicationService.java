package io.gitee.dqcer.mcdull.workflow.provider.application;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.CursorPageVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchHandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchStartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalDiagramQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalHistoryQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalStatusQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BusinessKeyBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.CancelApprovalDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionVersionBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DiagramQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HistoryBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.StartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TaskBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCountBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCursorQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.ApprovalDiagramVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.ApprovalStatusVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVersionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DiagramVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.HistoryVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.InstanceStatusVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.LatestApprovalVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.StartProcessResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskHandleResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TodoCountVO;

import java.util.List;

/**
 * workflow facade 用例层。provider 与 UI 层共用，保证逻辑单一来源。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public interface WorkflowApplicationService {

    List<DefinitionVO> batchGetDefinitions(DefinitionQueryDTO dto);

    BatchQueryResult<String, DefinitionVersionVO> batchGetDefinitionVersions(DefinitionVersionBatchDTO dto);

    List<DiagramVO> batchGetDiagrams(DiagramQueryDTO dto);

    StartProcessResult startProcess(StartProcessDTO dto);

    BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> batchGetInstanceStatuses(BusinessKeyBatchQueryDTO dto);

    CursorPageVO<TaskVO> pageTodoTasks(TodoCursorQueryDTO dto);

    TaskHandleResult handleTask(HandleTaskDTO dto);

    List<ApprovalStatusVO> batchGetApprovalStatus(ApprovalStatusQueryDTO dto);

    List<HistoryVO> getApprovalHistory(ApprovalHistoryQueryDTO dto);

    ApprovalStatusVO cancelApproval(CancelApprovalDTO dto);

    ApprovalDiagramVO getApprovalDiagram(ApprovalDiagramQueryDTO dto);

    BatchQueryResult<ProcessBusinessKey, LatestApprovalVO> batchGetLatestApprovals(BusinessKeyBatchQueryDTO dto);
}
