package io.gitee.dqcer.mcdull.workflow.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.CursorPageVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchHandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TaskBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCountBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.TodoCursorQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskHandleResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TaskVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.TodoCountVO;
import io.gitee.dqcer.mcdull.workflow.facade.service.WorkflowTaskApi;
import io.gitee.dqcer.mcdull.workflow.provider.application.WorkflowApplicationService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务 provider。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Primary
@RestController
public class WorkflowTaskApiDefImpl implements WorkflowTaskApi {

    @Resource
    private WorkflowApplicationService workflowApplicationService;

    @Override
    public InnerResult<List<TodoCountVO>> batchCountTodo(TodoCountBatchDTO dto) {
        return InnerResult.success(workflowApplicationService.batchCountTodo(dto));
    }

    @Override
    public InnerResult<CursorPageVO<TaskVO>> pageTodoTasks(TodoCursorQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.pageTodoTasks(dto));
    }

    @Override
    public InnerResult<BatchQueryResult<Long, TaskVO>> batchGetTasks(TaskBatchQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetTasks(dto));
    }

    @Override
    public InnerResult<TaskHandleResult> handleTask(HandleTaskDTO dto) {
        return InnerResult.success(workflowApplicationService.handleTask(dto));
    }

    @Override
    public InnerResult<BatchResult<TaskHandleResult>> batchHandleTasks(BatchHandleTaskDTO dto) {
        return InnerResult.success(workflowApplicationService.batchHandleTasks(dto));
    }
}
