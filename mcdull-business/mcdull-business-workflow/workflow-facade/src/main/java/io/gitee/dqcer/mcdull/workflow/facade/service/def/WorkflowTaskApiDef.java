package io.gitee.dqcer.mcdull.workflow.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
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
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 任务契约。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public interface WorkflowTaskApiDef {

    @PostMapping(WorkflowConstant.BASE_PATH + "/tasks/todo-count")
    InnerResult<List<TodoCountVO>> batchCountTodo(@Valid @RequestBody TodoCountBatchDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/tasks/todo-page")
    InnerResult<CursorPageVO<TaskVO>> pageTodoTasks(@Valid @RequestBody TodoCursorQueryDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/tasks/batch-get")
    InnerResult<BatchQueryResult<Long, TaskVO>> batchGetTasks(@Valid @RequestBody TaskBatchQueryDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/tasks/handle")
    InnerResult<TaskHandleResult> handleTask(@Valid @RequestBody HandleTaskDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/tasks/batch-handle")
    InnerResult<BatchResult<TaskHandleResult>> batchHandleTasks(@Valid @RequestBody BatchHandleTaskDTO dto);
}
