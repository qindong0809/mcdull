package io.gitee.dqcer.mcdull.workflow.provider.application;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.CursorPageVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchHandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchStartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BusinessKeyBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionVersionBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DiagramBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HistoryBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.StartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TaskBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCountBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCursorQueryDTO;
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

    BatchQueryResult<String, DefinitionVO> batchGetDefinitions(DefinitionBatchQueryDTO dto);

    BatchQueryResult<String, DefinitionVersionVO> batchGetDefinitionVersions(DefinitionVersionBatchDTO dto);

    BatchQueryResult<String, DiagramVO> batchGetDiagrams(DiagramBatchQueryDTO dto);

    StartProcessResult startProcess(StartProcessDTO dto);

    BatchResult<StartProcessResult> batchStartProcesses(BatchStartProcessDTO dto);

    BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> batchGetInstancesByBusinessKeys(BusinessKeyBatchQueryDTO dto);

    BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> batchGetInstanceStatuses(BusinessKeyBatchQueryDTO dto);

    List<TodoCountVO> batchCountTodo(TodoCountBatchDTO dto);

    CursorPageVO<TaskVO> pageTodoTasks(TodoCursorQueryDTO dto);

    BatchQueryResult<Long, TaskVO> batchGetTasks(TaskBatchQueryDTO dto);

    TaskHandleResult handleTask(HandleTaskDTO dto);

    BatchResult<TaskHandleResult> batchHandleTasks(BatchHandleTaskDTO dto);

    BatchQueryResult<Long, List<HistoryVO>> batchGetHistoriesByInstanceIds(HistoryBatchQueryDTO dto);

    BatchQueryResult<ProcessBusinessKey, LatestApprovalVO> batchGetLatestApprovals(BusinessKeyBatchQueryDTO dto);
}
