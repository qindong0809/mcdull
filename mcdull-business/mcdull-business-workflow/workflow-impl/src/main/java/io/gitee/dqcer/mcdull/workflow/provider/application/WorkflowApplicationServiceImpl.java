package io.gitee.dqcer.mcdull.workflow.provider.application;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.workflow.web.service.IFlowDefinitionService;
import org.dromara.warm.flow.core.FlowEngine;
import org.dromara.warm.flow.core.dto.FlowCombine;
import org.dromara.warm.flow.core.dto.FlowParams;
import org.dromara.warm.flow.core.entity.Definition;
import org.dromara.warm.flow.core.entity.HisTask;
import org.dromara.warm.flow.core.entity.Instance;
import org.dromara.warm.flow.core.entity.Node;
import org.dromara.warm.flow.core.entity.Skip;
import org.dromara.warm.flow.core.entity.Task;
import org.dromara.warm.flow.core.entity.User;
import org.dromara.warm.flow.core.enums.SkipType;
import org.dromara.warm.flow.core.enums.UserType;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
import io.gitee.dqcer.mcdull.workflow.facade.enums.TaskActionEnum;
import io.gitee.dqcer.mcdull.workflow.facade.enums.WorkflowCodeEnum;
import io.gitee.dqcer.mcdull.workflow.facade.enums.WorkflowStatusEnum;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryItem;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.CursorPageVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ItemResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalDiagramQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalHistoryQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.ApprovalStatusQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchHandleTaskDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.CancelApprovalDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BatchStartProcessDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BusinessKeyBatchQueryDTO;
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
import io.gitee.dqcer.mcdull.workflow.web.mapper.WorkflowFacadeMapper;
import jakarta.annotation.Resource;
import org.dromara.warm.flow.core.service.DefService;
import org.dromara.warm.flow.core.service.InsService;
import org.dromara.warm.flow.core.service.TaskService;
import org.dromara.warm.flow.core.service.UserService;
import org.dromara.warm.flow.orm.mapper.FlowDefinitionMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * workflow facade 用例实现。
 * <p>
 * 只读写 flow_* 表；用户、组织、业务单据信息全部由调用方以快照形式传入。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Service
public class WorkflowApplicationServiceImpl implements WorkflowApplicationService {

    @Resource
    private WorkflowFacadeMapper workflowFacadeMapper;

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private DefService defService;
    @Resource
    private InsService insService;
    @Resource
    private TaskService taskService;
    @Resource
    private UserService userService;


    @Override
    public List<DefinitionVO> batchGetDefinitions(DefinitionQueryDTO dto) {
        List<Definition> definitions = defService.getByFlowCode(dto.getCode());
        if (definitions == null || definitions.isEmpty()) {
            return Collections.emptyList();
        }
        return definitions.stream()
                .map(definition -> toDefinitionVO(dto.getCode(), definition))
                .collect(Collectors.toList());
    }

    @Override
    public BatchQueryResult<String, DefinitionVersionVO> batchGetDefinitionVersions(DefinitionVersionBatchDTO dto) {
        checkReadSize(dto.getDefinitionKeys().size());
        List<String> codes = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (String key : dto.getDefinitionKeys()) {
            if (StrUtil.startWith(key, "def_")) {
                try {
                    ids.add(Long.valueOf(key.substring(4)));
                    continue;
                } catch (NumberFormatException ignored) {
                    // 非法 def_ 前缀按 flowCode 处理
                }
            }
            codes.add(key);
        }
        Map<String, Definition> byKey = new LinkedHashMap<>();
        if (!codes.isEmpty()) {
            List<Definition> definitions = defService.queryByCodeList(codes);
            if (definitions != null) {
                for (Definition definition : definitions) {
                    byKey.putIfAbsent(definition.getFlowCode(), definition);
                }
            }
        }
        if (!ids.isEmpty()) {
            List<Definition> definitions = defService.getByIds(ids);
            if (definitions != null) {
                for (Definition definition : definitions) {
                    byKey.put("def_" + definition.getId(), definition);
                }
            }
        }
        BatchQueryResult<String, DefinitionVersionVO> result = BatchQueryResult.empty();
        for (String key : dto.getDefinitionKeys()) {
            Definition definition = byKey.get(key);
            result.add(key, definition == null ? null : DefinitionVersionVO.builder()
                    .definitionKey(key)
                    .version(definition.getVersion())
                    .updateTime(definition.getUpdateTime() == null
                            ? null : DateUtil.format(definition.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"))
                    .publishStatus(definition.getIsPublish() == null ? 0 : definition.getIsPublish())
                    .activityStatus(definition.getActivityStatus() == null ? 1 : definition.getActivityStatus())
                    .build());
        }
        return result;
    }

    @Override
    public List<DiagramVO> batchGetDiagrams(DiagramQueryDTO dto) {
        List<Definition> definitions = defService.getByFlowCode(dto.getCode());
        if (definitions == null || definitions.isEmpty()) {
            return Collections.emptyList();
        }
        return definitions.stream()
                .map(definition -> toDiagramVO(dto.getCode(), definition))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StartProcessResult startProcess(StartProcessDTO dto) {
        return doStart(dto);
    }


    @Override
    public BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> batchGetInstanceStatuses(BusinessKeyBatchQueryDTO dto) {
        return queryInstanceStatuses(dto);
    }


    @Override
    public CursorPageVO<TaskVO> pageTodoTasks(TodoCursorQueryDTO dto) {
        int limit = dto.getPageSize() == null ? 50 : Math.min(dto.getPageSize(), WorkflowConstant.MAX_PAGE_SIZE);
        String[] cursor = parseCursor(dto.getCursor());
        Long cursorId = cursor.length > 1 && StrUtil.isNotBlank(cursor[1]) ? Convert.toLong(cursor[1]) : null;

        Task condition = FlowEngine.newTask();
        condition.setFlowStatus(WorkflowStatusEnum.RUNNING.getCode());
        condition.setNodeType(1);
        List<Task> candidates = taskService.list(condition);
        if (candidates == null || candidates.isEmpty()) {
            return CursorPageVO.<TaskVO>builder()
                    .list(Collections.emptyList())
                    .nextCursor("")
                    .hasMore(false)
                    .total(0L)
                    .build();
        }

        Map<Long, List<String>> assigneesByTask = assigneesByTaskIds(candidates.stream()
                .map(Task::getId).collect(Collectors.toList()));
        Map<Long, Definition> definitions = definitionsByTaskIds(candidates);
        List<Task> matched = filterTodoTasks(candidates, assigneesByTask, definitions,
                dto.getAssigneeId(), dto.getKeyword(), dto.getFlowCode());
        matched.sort(Comparator.comparing(Task::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Task::getId, Comparator.nullsLast(Comparator.reverseOrder())));

        List<Task> page = matched.stream()
                .filter(task -> beforeCursor(task, cursor[0], cursorId))
                .limit(limit)
                .collect(Collectors.toList());
        Map<Long, Instance> instances = instancesByTaskIds(page);
        List<TaskVO> list = page.stream()
                .map(task -> toTaskVO(task, instances, definitions, assigneesByTask))
                .collect(Collectors.toList());

        String nextCursor = "";
        if (!page.isEmpty() && page.size() == limit) {
            Task last = page.get(page.size() - 1);
            nextCursor = time(last.getCreateTime()) + "|" + str(last.getId());
        }
        Long total = dto.isNeedTotal() ? (long) matched.size() : null;
        return CursorPageVO.<TaskVO>builder()
                .list(list)
                .nextCursor(nextCursor)
                .hasMore(!page.isEmpty() && page.size() == limit)
                .total(total)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskHandleResult handleTask(HandleTaskDTO dto) {
        return doHandle(dto);
    }


    @Override
    public List<ApprovalStatusVO> batchGetApprovalStatus(ApprovalStatusQueryDTO dto) {
        checkReadSize(dto.getBusinessIds().size());
        BusinessKeyBatchQueryDTO query = new BusinessKeyBatchQueryDTO();
        query.setKeys(dto.getBusinessIds().stream()
                .map(businessId -> ProcessBusinessKey.builder()
                        .flowCode(dto.getCode())
                        .businessId(businessId)
                        .build())
                .collect(Collectors.toList()));
        BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> source = batchGetInstanceStatuses(query);
        return source.getItems().stream()
                .map(BatchQueryItem::getValue)
                .filter(value -> value != null)
                .map(this::toApprovalStatusVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryVO> getApprovalHistory(ApprovalHistoryQueryDTO dto) {
        Instance instance = findInstance(dto.getCode(), dto.getBusinessId());
        Long instanceId = instance == null ? null : instance.getId();
        if (instanceId == null || instanceId <= 0) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
        }
        int limit = dto.getLimit() == null || dto.getLimit() <= 0
                ? 50 : Math.min(dto.getLimit(), WorkflowConstant.MAX_PAGE_SIZE);
        List<HisTask> rows = FlowEngine.hisTaskService().getByInsId(instanceId);
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }
        return latestHistories(instanceId, rows, limit).stream()
                .map(row -> toHistoryVO(instanceId, row))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovalStatusVO cancelApproval(CancelApprovalDTO dto) {
        return doCancel(dto);
    }

    private ApprovalStatusVO doCancel(CancelApprovalDTO dto) {
        Instance instance = findInstance(dto.getCode(), dto.getBusinessId());
        if (instance == null) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
        }
        String operator = dto.getOperatorId();
        if (StrUtil.isBlank(operator) || !operator.equals(str(attributes(instance.getExt()).get("starterId")))) {
            throw new BusinessException(WorkflowCodeEnum.PERMISSION_DENIED);
        }
        if (!WorkflowStatusEnum.RUNNING.getCode().equals(str(instance.getFlowStatus()))) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_STATUS_INVALID);
        }
        Long instanceId = instance.getId();
        if (instanceId == null || instanceId <= 0) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
        }

        String withdrawn = WorkflowStatusEnum.WITHDRAWN.getCode();
        FlowParams params = FlowParams.build()
                .handler(operator)
                .message(dto.getReason())
                .flowStatus(withdrawn)
                .ignore(true);
        FlowEngine.taskService().terminationByInsId(instanceId, params);
        insertEvent("PROCESS_WITHDRAWN", str(instanceId), instance);
        return ApprovalStatusVO.builder()
                .instanceId(instanceId)
                .flowCode(dto.getCode())
                .businessId(dto.getBusinessId())
                .attributes(attributes(instance.getExt()))
                .flowStatus(withdrawn)
                .statusText(WorkflowStatusEnum.WITHDRAWN.getText())
                .currentNodeCode("")
                .currentNodeName("")
                .currentApproverIds(Collections.emptyList())
                .updateTime(DateUtil.now())
                .build();
    }

    @Override
    public ApprovalDiagramVO getApprovalDiagram(ApprovalDiagramQueryDTO dto) {
        Instance instance = null;
        if (StrUtil.isNotBlank(dto.getBusinessId())) {
            instance = findInstance(dto.getCode(), dto.getBusinessId());
            if (instance == null) {
                throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
            }
        }

        Definition definition = null;
        if (instance != null) {
            Long definitionId = instance.getDefinitionId();
            if (definitionId != null && definitionId > 0) {
                definition = defService.getById(definitionId);
            }
        } else {
            List<Definition> definitions = defService.getByFlowCode(dto.getCode());
            if (definitions != null && !definitions.isEmpty()) {
                definition = definitions.get(0);
            }
        }
        if (definition == null) {
            throw new BusinessException(WorkflowCodeEnum.DEFINITION_NOT_FOUND);
        }
        DiagramVO diagram = toDiagramVO(dto.getCode(), definition);

        ApprovalDiagramVO.ApprovalDiagramVOBuilder builder = ApprovalDiagramVO.builder()
                .definitionKey(dto.getCode())
                .nodes(diagram.getNodes())
                .edges(diagram.getEdges())
                .businessId(dto.getBusinessId());
        if (instance == null) {
            return builder.build();
        }

        Long instanceId = instance.getId();
        if (instanceId == null || instanceId <= 0) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
        }
        String flowStatus = str(instance.getFlowStatus());
        builder.instanceId(instanceId)
                .flowStatus(flowStatus)
                .statusText(WorkflowStatusEnum.textOf(flowStatus))
                .currentNodeCode(str(instance.getNodeCode()))
                .currentNodeName(str(instance.getNodeName()));

        List<String> approverIds = new ArrayList<>();
        List<Task> currentTasks = taskService.getByInsId(instanceId);
        if (currentTasks != null && !currentTasks.isEmpty()) {
            List<Long> taskIds = currentTasks.stream().map(Task::getId).collect(Collectors.toList());
            List<User> users = userService.getByAssociateds(taskIds);
            if (users != null) {
                users.stream().map(User::getProcessedBy).filter(StrUtil::isNotBlank).forEach(approverIds::add);
            }
        }
        builder.currentApproverIds(approverIds);

        LinkedHashSet<String> visited = new LinkedHashSet<>();
        List<HistoryVO> history = new ArrayList<>();
        List<HisTask> historyRows = FlowEngine.hisTaskService().getByInsId(instanceId);
        if (historyRows != null) {
            for (HisTask row : historyRows) {
                HistoryVO item = toHistoryVO(instanceId, row);
                history.add(item);
                if (StrUtil.isNotBlank(item.getNodeCode())) {
                    visited.add(item.getNodeCode());
                }
            }
        }
        return builder.visitedNodeCodes(new ArrayList<>(visited)).history(history).build();
    }

    @Override
    public BatchQueryResult<ProcessBusinessKey, LatestApprovalVO> batchGetLatestApprovals(BusinessKeyBatchQueryDTO dto) {
        checkReadSize(dto.getKeys().size());
        BatchQueryResult<ProcessBusinessKey, LatestApprovalVO> result = BatchQueryResult.empty();
        for (ProcessBusinessKey key : dto.getKeys()) {
            Instance instance = findInstance(key.getFlowCode(), key.getBusinessId());
            if (instance == null) {
                result.add(key, null);
                continue;
            }
            List<HisTask> histories = latestHistories(instance.getId(),
                    FlowEngine.hisTaskService().getByInsId(instance.getId()), 1);
            if (histories.isEmpty()) {
                result.add(key, null);
                continue;
            }
            HisTask history = histories.get(0);
            String flowStatus = str(instance.getFlowStatus());
            result.add(key, LatestApprovalVO.builder()
                    .instanceId(instance.getId())
                    .flowCode(key.getFlowCode())
                    .businessId(key.getBusinessId())
                    .flowStatus(flowStatus)
                    .statusText(WorkflowStatusEnum.textOf(flowStatus))
                    .handlerId(history.getApprover())
                    .attributes(attributes(history.getExt()))
                    .action(actionOf(history))
                    .comment(history.getMessage())
                    .updateTime(time(history.getUpdateTime()))
                    .build());
        }
        return result;
    }

    // ==================== 私有方法 ====================

    private BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> queryInstanceStatuses(BusinessKeyBatchQueryDTO dto) {
        checkReadSize(dto.getKeys().size());
        BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> result = BatchQueryResult.empty();
        for (ProcessBusinessKey key : dto.getKeys()) {
            Instance instance = findInstance(key.getFlowCode(), key.getBusinessId());
            if (instance == null) {
                result.add(key, null);
                continue;
            }
            String flowStatus = str(instance.getFlowStatus());
            List<String> approverIds = new ArrayList<>();
            List<Task> tasks = taskService.getByInsId(instance.getId());
            if (tasks != null && !tasks.isEmpty()) {
                List<Long> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toList());
                List<User> users = userService.getByAssociateds(taskIds);
                if (users != null) {
                    users.stream().map(User::getProcessedBy).filter(StrUtil::isNotBlank).forEach(approverIds::add);
                }
            }
            result.add(key, InstanceStatusVO.builder()
                    .instanceId(instance.getId())
                    .flowCode(key.getFlowCode())
                    .businessId(key.getBusinessId())
                    .attributes(attributes(instance.getExt()))
                    .flowStatus(flowStatus)
                    .statusText(WorkflowStatusEnum.textOf(flowStatus))
                    .currentNodeCode(str(instance.getNodeCode()))
                    .currentNodeName(str(instance.getNodeName()))
                    .currentApproverIds(approverIds)
                    .updateTime(time(instance.getUpdateTime()))
                    .build());
        }
        return result;
    }

    private Instance findInstance(String flowCode, String businessId) {
        List<Definition> definitions = defService.getByFlowCode(flowCode);
        if (definitions == null || definitions.isEmpty()) {
            return null;
        }
        for (Definition definition : definitions) {
            Long definitionId = definition.getId();
            if (definitionId == null || definitionId <= 0) {
                continue;
            }
            Instance condition = FlowEngine.newIns();
            condition.setDefinitionId(definitionId);
            condition.setBusinessId(businessId);
            List<Instance> instances = insService.list(condition);
            if (instances != null && !instances.isEmpty()) {
                return instances.get(0);
            }
        }
        return null;
    }

    private StartProcessResult doStart(StartProcessDTO dto) {
        Instance exists = findInstance(dto.getFlowCode(), dto.getBusinessId());
        if (exists != null && !WorkflowStatusEnum.isFinal(str(exists.getFlowStatus()))) {
            throw new BusinessException(WorkflowCodeEnum.BUSINESS_KEY_CONFLICT);
        }

        String operatorId = StrUtil.isBlank(dto.getOperatorId()) ? "0" : dto.getOperatorId();
        Map<String, Object> attributes = dto.getAttributes() == null
                ? new HashMap<>() : new HashMap<>(dto.getAttributes());
        attributes.putIfAbsent("starterId", operatorId);

        FlowParams params = FlowParams.build()
                .flowCode(dto.getFlowCode())
                .handler(operatorId)
                .variable(dto.getVariables())
                .flowStatus(WorkflowStatusEnum.RUNNING.getCode())
                .ext(json(attributes))
                .ignore(true);
        if (dto.getAssigneeIds() != null && !dto.getAssigneeIds().isEmpty()) {
            params.nextHandler(dto.getAssigneeIds().toArray(String[]::new));
        }
        Instance instance = FlowEngine.insService().start(dto.getBusinessId(), params);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", instance.getId());
        payload.put("definitionId", instance.getDefinitionId());
        payload.put("flowCode", dto.getFlowCode());
        payload.put("businessId", instance.getBusinessId());
        payload.put("attributes", attributes);
        payload.put("nodeType", instance.getNodeType());
        payload.put("nodeCode", instance.getNodeCode());
        payload.put("nodeName", instance.getNodeName());
        payload.put("variable", instance.getVariable());
        payload.put("flowStatus", instance.getFlowStatus());
        payload.put("createBy", instance.getCreateBy());
        insertEvent("PROCESS_STARTED", str(instance.getId()), payload);

        List<Task> tasks = FlowEngine.taskService().getByInsId(instance.getId());
        if (tasks != null) {
            for (Task task : tasks) {
                Map<String, Object> taskPayload = new HashMap<>();
                taskPayload.put("id", task.getId());
                taskPayload.put("definitionId", task.getDefinitionId());
                taskPayload.put("instanceId", task.getInstanceId());
                taskPayload.put("nodeCode", task.getNodeCode());
                taskPayload.put("nodeName", task.getNodeName());
                taskPayload.put("nodeType", task.getNodeType());
                taskPayload.put("flowStatus", task.getFlowStatus());
                insertEvent("TASK_CREATED", str(task.getId()), taskPayload);
            }
        }
        return StartProcessResult.builder()
                .instanceId(instance.getId())
                .flowCode(dto.getFlowCode())
                .businessId(instance.getBusinessId())
                .flowStatus(instance.getFlowStatus())
                .currentNodeCode(instance.getNodeCode())
                .currentNodeName(instance.getNodeName())
                .createTime(DateUtil.format(instance.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                .build();
    }

    private TaskHandleResult doHandle(HandleTaskDTO dto) {
        TaskActionEnum action = TaskActionEnum.of(dto.getAction());
        if (action == null) {
            throw new BusinessException(WorkflowCodeEnum.TASK_ACTION_INVALID);
        }
        Task task = taskService.getById(dto.getTaskId());
        if (task == null) {
            throw new BusinessException(WorkflowCodeEnum.TASK_NOT_FOUND);
        }
        Instance instance = insService.getById(task.getInstanceId());
        if (instance == null) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_NOT_FOUND);
        }
        String instanceStatus = str(instance.getFlowStatus());
        if (!WorkflowStatusEnum.RUNNING.getCode().equals(instanceStatus)) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_STATUS_INVALID);
        }
        String operator = StrUtil.isBlank(dto.getOperatorId()) ? "0" : dto.getOperatorId();
        List<String> approvers = FlowEngine.userService()
                .getPermission(dto.getTaskId(), UserType.APPROVAL.getKey());
        if (approvers == null || !approvers.contains(operator)) {
            throw new BusinessException(WorkflowCodeEnum.TASK_NOT_ASSIGNED);
        }
        Long taskId = task.getId();
        Long instanceId = task.getInstanceId();
        Long definitionId = task.getDefinitionId();
        String operatorName = StrUtil.isBlank(dto.getOperatorName()) ? operator : dto.getOperatorName();

        if (action == TaskActionEnum.APPROVE || action == TaskActionEnum.REJECT) {
            return doHandleByWarmFlow(dto, action, taskId, instanceId, operator);
        }

        HandlePlan plan = buildPlan(action);
        if (action == TaskActionEnum.URGE) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("taskId", taskId);
            payload.put("instanceId", instanceId);
            payload.put("definitionId", definitionId);
            payload.put("nodeCode", task.getNodeCode());
            payload.put("nodeName", task.getNodeName());
            payload.put("taskStatus", task.getFlowStatus());
            payload.put("businessId", task.getBusinessId());
            payload.put("instanceStatus", instanceStatus);
            insertEvent("TASK_URGED", str(taskId), payload);
            return TaskHandleResult.builder()
                    .taskId(taskId)
                    .instanceId(instanceId)
                    .action(action.getCode())
                    .flowStatus(instanceStatus)
                    .finished(false)
                    .build();
        }

        Long historyId = IdUtil.getSnowflakeNextId();
        Map<String, Object> history = new HashMap<>();
        history.put("id", historyId);
        history.put("definitionId", definitionId);
        history.put("instanceId", instanceId);
        history.put("taskId", taskId);
        history.put("nodeCode", task.getNodeCode());
        history.put("nodeName", task.getNodeName());
        history.put("targetNodeCode", plan.nextCode);
        history.put("approver", operator);
        Map<String, Object> historyAttributes = dto.getAttributes() == null
                ? new HashMap<>() : new HashMap<>(dto.getAttributes());
        historyAttributes.putIfAbsent("approverName", operatorName);
        history.put("ext", json(historyAttributes));
        history.put("cooperateType", plan.cooperateType);
        history.put("skipType", plan.skipType);
        history.put("flowStatus", plan.flowStatus);
        history.put("message", dto.getComment());
        history.put("variable", json(dto.getVariables()));
        workflowFacadeMapper.insertHistory(history);

        if (plan.insertUsers) {
            List<String> targets = dto.getTargetAssigneeIds() == null || dto.getTargetAssigneeIds().isEmpty()
                    ? Collections.singletonList(operator) : dto.getTargetAssigneeIds();
            List<User> users = new ArrayList<>();
            for (String target : targets) {
                User user = FlowEngine.newUser();
                user.setType(str(plan.cooperateType));
                user.setProcessedBy(target);
                user.setAssociated(taskId);
                user.setCreateBy(operator);
                users.add(user);
            }
            FlowEngine.userService().saveBatch(users);
        }

        if (plan.completeTask) {
            FlowParams pendingParams = FlowParams.build()
                    .handler(operator)
                    .message(dto.getComment())
                    .flowStatus(plan.flowStatus)
                    .ignore(true);
            FlowEngine.taskService().pending(taskId, pendingParams);
        }
        if (plan.updateInstance) {
            Instance instance1 = insService.getById(instanceId);
            if (instance1 != null) {
                instance1.setFlowStatus(plan.flowStatus);
                instance1.setNodeCode(plan.nextCode);
                instance1.setNodeName(plan.nextName);
                insService.updateById(instance1);
            }
        }
        insertEvent("TASK_COMPLETED", str(historyId), history);
        if (WorkflowStatusEnum.isFinal(plan.flowStatus)) {
            insertEvent("PROCESS_COMPLETED", str(instanceId), history);
        }
        return TaskHandleResult.builder()
                .taskId(taskId)
                .instanceId(instanceId)
                .historyId(historyId)
                .action(action.getCode())
                .flowStatus(plan.flowStatus)
                .nextNodeCode(plan.nextCode)
                .nextNodeName(plan.nextName)
                .finished(WorkflowStatusEnum.isFinal(plan.flowStatus))
                .build();
    }

    private TaskHandleResult doHandleByWarmFlow(HandleTaskDTO dto, TaskActionEnum action,
                                                Long taskId, Long instanceId, String operator) {
        FlowParams params = FlowParams.build()
                .handler(operator)
                .message(dto.getComment())
                .variable(dto.getVariables())
                .ignore(true);
        if (action == TaskActionEnum.APPROVE) {
            params.skipType(SkipType.PASS.getKey());
        } else {
            params.skipType(SkipType.REJECT.getKey())
                    .flowStatus(WorkflowStatusEnum.REJECTED.getCode())
                    .hisStatus(WorkflowStatusEnum.REJECTED.getCode());
        }
        Instance instance = FlowEngine.taskService().skip(taskId, params);

        List<HisTask> hisTasks = FlowEngine.hisTaskService().listByTaskId(taskId);
        Long historyId = hisTasks == null || hisTasks.isEmpty() ? null : hisTasks.get(hisTasks.size() - 1).getId();
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", historyId);
        payload.put("instanceId", instanceId);
        payload.put("taskId", taskId);
        payload.put("action", action.getCode());
        payload.put("comment", dto.getComment());
        payload.put("variables", dto.getVariables());
        payload.put("flowStatus", instance.getFlowStatus());
        insertEvent("TASK_COMPLETED", str(historyId), payload);
        if (WorkflowStatusEnum.isFinal(instance.getFlowStatus())) {
            insertEvent("PROCESS_COMPLETED", str(instanceId), payload);
        }
        return TaskHandleResult.builder()
                .taskId(taskId)
                .instanceId(instanceId)
                .historyId(historyId)
                .action(action.getCode())
                .flowStatus(instance.getFlowStatus())
                .nextNodeCode(instance.getNodeCode())
                .nextNodeName(instance.getNodeName())
                .finished(WorkflowStatusEnum.isFinal(instance.getFlowStatus()))
                .build();
    }

    private HandlePlan buildPlan(TaskActionEnum action) {
        HandlePlan plan = new HandlePlan();
        plan.skipType = "NONE";
        plan.cooperateType = 1;
        plan.nextCode = "";
        plan.nextName = "";
        switch (action) {
            case REJECT -> {
                plan.flowStatus = WorkflowStatusEnum.REJECTED.getCode();
                plan.skipType = "REJECT";
                plan.completeTask = true;
                plan.updateInstance = true;
            }
            case TRANSFER, DELEGATE, COUNTERSIGN -> {
                plan.flowStatus = WorkflowStatusEnum.RUNNING.getCode();
                plan.cooperateType = action == TaskActionEnum.TRANSFER ? 2
                        : action == TaskActionEnum.DELEGATE ? 3 : 6;
                plan.completeTask = action != TaskActionEnum.COUNTERSIGN;
                plan.updateInstance = false;
                plan.insertUsers = true;
            }
            case WITHDRAW, TAKE_BACK -> {
                plan.flowStatus = action == TaskActionEnum.WITHDRAW
                        ? WorkflowStatusEnum.WITHDRAWN.getCode()
                        : WorkflowStatusEnum.TAKEN_BACK.getCode();
                plan.completeTask = true;
                plan.updateInstance = true;
            }
            case URGE -> plan.flowStatus = WorkflowStatusEnum.RUNNING.getCode();
            default -> throw new BusinessException(WorkflowCodeEnum.TASK_ACTION_INVALID);
        }
        return plan;
    }

    private void insertEvent(String eventType, String aggregateId, Object payload) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", IdUtil.getSnowflakeNextId());
        row.put("eventType", eventType);
        row.put("aggregateId", aggregateId);
        row.put("payloadJson", json(payload));
        workflowFacadeMapper.insertEvent(row);
    }

    private List<String> resolveAssignees(StartProcessDTO dto, String handlerPath, String starterId) {
        List<String> result = new ArrayList<>();
        if (dto.getAssigneeIds() != null) {
            for (String id : dto.getAssigneeIds()) {
                if (StrUtil.isNotBlank(id)) {
                    result.add(id.trim());
                }
            }
        }
        if (result.isEmpty() && StrUtil.isNotBlank(handlerPath)) {
            for (String part : handlerPath.split("[,;@]+")) {
                String id = part.trim();
                if (id.startsWith("user:")) {
                    id = id.substring(5);
                }
                if (StrUtil.isNumeric(id)) {
                    result.add(id);
                }
            }
        }
        if (result.isEmpty()) {
            result.add(starterId);
        }
        return new ArrayList<>(new LinkedHashSet<>(result));
    }

    private StartProcessDTO inheritStart(StartProcessDTO item, BaseWorkflowRequest base) {
        if (StrUtil.isBlank(item.getOperatorId())) {
            item.setOperatorId(base.getOperatorId());
        }
        if (StrUtil.isBlank(item.getOperatorName())) {
            item.setOperatorName(base.getOperatorName());
        }
        if (StrUtil.isBlank(item.getSourceSystem())) {
            item.setSourceSystem(base.getSourceSystem());
        }
        if (StrUtil.isBlank(item.getTraceId())) {
            item.setTraceId(base.getTraceId());
        }
        return item;
    }

    private HandleTaskDTO inheritHandle(HandleTaskDTO item, BaseWorkflowRequest base) {
        if (StrUtil.isBlank(item.getOperatorId())) {
            item.setOperatorId(base.getOperatorId());
        }
        if (StrUtil.isBlank(item.getOperatorName())) {
            item.setOperatorName(base.getOperatorName());
        }
        if (StrUtil.isBlank(item.getSourceSystem())) {
            item.setSourceSystem(base.getSourceSystem());
        }
        if (StrUtil.isBlank(item.getTraceId())) {
            item.setTraceId(base.getTraceId());
        }
        return item;
    }

    private DefinitionVO toDefinitionVO(String key, Definition definition) {
        return DefinitionVO.builder()
                .definitionKey(key)
                .definitionId(definition.getId())
                .flowCode(definition.getFlowCode())
                .flowName(definition.getFlowName())
                .category(definition.getCategory())
                .version(definition.getVersion())
                .publishStatus(definition.getIsPublish() == null ? 0 : definition.getIsPublish())
                .activityStatus(definition.getActivityStatus() == null ? 1 : definition.getActivityStatus())
                .formCustom(definition.getFormCustom())
                .formPath(definition.getFormPath())
                .ext(definition.getExt())
                .updateTime(definition.getUpdateTime() == null
                        ? null : DateUtil.format(definition.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"))
                .build();
    }

    private ApprovalStatusVO toApprovalStatusVO(InstanceStatusVO value) {
        return ApprovalStatusVO.builder()
                .instanceId(value.getInstanceId())
                .flowCode(value.getFlowCode())
                .businessId(value.getBusinessId())
                .attributes(value.getAttributes())
                .flowStatus(value.getFlowStatus())
                .statusText(value.getStatusText())
                .currentNodeCode(value.getCurrentNodeCode())
                .currentNodeName(value.getCurrentNodeName())
                .currentApproverIds(value.getCurrentApproverIds())
                .updateTime(value.getUpdateTime())
                .build();
    }

    private DiagramVO toDiagramVO(String code, Definition definition) {
        FlowCombine combine = defService.getFlowCombineNoDef(definition.getId());
        List<DiagramVO.DiagramNodeVO> nodes = new ArrayList<>();
        List<DiagramVO.DiagramEdgeVO> edges = new ArrayList<>();
        if (combine != null) {
            for (Node node : combine.getAllNodes()) {
                nodes.add(DiagramVO.DiagramNodeVO.builder()
                        .nodeCode(node.getNodeCode())
                        .nodeName(node.getNodeName())
                        .nodeType(node.getNodeType())
                        .coordinate(node.getCoordinate())
                        .handlerPath(node.getListenerPath())
                        .permissionFlag(node.getPermissionFlag())
                        .build());
            }
            for (Skip skip : combine.getAllSkips()) {
                edges.add(DiagramVO.DiagramEdgeVO.builder()
                        .fromNodeCode(skip.getNowNodeCode())
                        .toNodeCode(skip.getNextNodeCode())
                        .label(skip.getSkipName())
                        .condition(skip.getSkipCondition())
                        .skipType(skip.getSkipType())
                        .build());
            }
        }
        return DiagramVO.builder()
                .definitionKey(code)
                .nodes(nodes)
                .edges(edges)
                .build();
    }

    private TaskVO toTaskVO(Task task, Map<Long, Instance> instances,
                            Map<Long, Definition> definitions,
                            Map<Long, List<String>> assigneesByTask) {
        Instance instance = instances.get(task.getInstanceId());
        Definition definition = definitions.get(task.getDefinitionId());
        Map<String, Object> attributes = attributes(instance == null ? null : instance.getExt());
        return TaskVO.builder()
                .taskId(task.getId())
                .instanceId(task.getInstanceId())
                .definitionId(task.getDefinitionId())
                .flowCode(definition == null ? str(attributes.get("flowCode")) : definition.getFlowCode())
                .flowStatus(str(task.getFlowStatus()))
                .nodeCode(str(task.getNodeCode()))
                .nodeName(str(task.getNodeName()))
                .businessId(str(task.getBusinessId()))
                .attributes(attributes)
                .assigneeIds(assigneesByTask.getOrDefault(task.getId(), Collections.emptyList()))
                .createTime(time(task.getCreateTime()))
                .updateTime(time(task.getUpdateTime()))
                .build();
    }

    private Map<Long, List<String>> assigneesByTaskIds(List<Long> taskIds) {
        Map<Long, List<String>> result = new LinkedHashMap<>();
        if (taskIds == null || taskIds.isEmpty()) {
            return result;
        }
        List<User> users = userService.getByAssociateds(taskIds, UserType.APPROVAL.getKey());
        if (users == null) {
            return result;
        }
        for (User user : users) {
            String processedBy = str(user.getProcessedBy());
            if (StrUtil.isBlank(processedBy)) {
                continue;
            }
            result.computeIfAbsent(user.getAssociated(), k -> new ArrayList<>()).add(processedBy);
        }
        return result;
    }

    private Map<Long, Definition> definitionsByTaskIds(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return new LinkedHashMap<>();
        }
        List<Long> definitionIds = tasks.stream().map(Task::getDefinitionId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (definitionIds.isEmpty()) {
            return new LinkedHashMap<>();
        }
        Map<Long, Definition> result = new LinkedHashMap<>();
        List<Definition> definitions = defService.getByIds(definitionIds);
        if (definitions != null) {
            for (Definition definition : definitions) {
                result.put(definition.getId(), definition);
            }
        }
        return result;
    }

    private Map<Long, Instance> instancesByTaskIds(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return new LinkedHashMap<>();
        }
        List<Long> instanceIds = tasks.stream().map(Task::getInstanceId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (instanceIds.isEmpty()) {
            return new LinkedHashMap<>();
        }
        Map<Long, Instance> result = new LinkedHashMap<>();
        List<Instance> instances = insService.getByIds(instanceIds);
        if (instances != null) {
            for (Instance instance : instances) {
                result.put(instance.getId(), instance);
            }
        }
        return result;
    }

    private List<Task> filterTodoTasks(List<Task> tasks,
                                       Map<Long, List<String>> assigneesByTask,
                                       Map<Long, Definition> definitions,
                                       String assigneeId, String keyword, String flowCode) {
        String normalizedKeyword = StrUtil.isBlank(keyword) ? "" : keyword.trim().toLowerCase();
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            List<String> taskAssignees = assigneesByTask.getOrDefault(task.getId(), Collections.emptyList());
            if (StrUtil.isNotBlank(assigneeId) && !taskAssignees.contains(assigneeId)) {
                continue;
            }
            Definition definition = definitions.get(task.getDefinitionId());
            String taskFlowCode = definition == null ? "" : str(definition.getFlowCode());
            if (StrUtil.isNotBlank(flowCode) && !flowCode.equals(taskFlowCode)) {
                continue;
            }
            if (StrUtil.isNotBlank(normalizedKeyword)) {
                String nodeName = str(task.getNodeName()).toLowerCase();
                String businessId = str(task.getBusinessId()).toLowerCase();
                if (!nodeName.contains(normalizedKeyword) && !businessId.contains(normalizedKeyword)) {
                    continue;
                }
            }
            result.add(task);
        }
        return result;
    }

    private boolean beforeCursor(Task task, String cursorTime, Long cursorId) {
        if (StrUtil.isBlank(cursorTime)) {
            return true;
        }
        String current = time(task.getCreateTime());
        int compare = current.compareTo(cursorTime);
        return compare < 0 || (compare == 0 && cursorId != null && task.getId() < cursorId);
    }

    private String flowCodeOf(Map<Long, Definition> definitions, Task task) {
        Definition definition = definitions.get(task.getDefinitionId());
        return definition == null ? "" : str(definition.getFlowCode());
    }

    private List<HisTask> latestHistories(Long instanceId, List<HisTask> rows, int limit) {
        if (rows == null || rows.isEmpty() || limit <= 0) {
            return Collections.emptyList();
        }
        return rows.stream()
                .filter(row -> instanceId == null || Objects.equals(instanceId, row.getInstanceId()))
                .sorted(Comparator.comparing(HisTask::getUpdateTime,
                                Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(HisTask::getId, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private HistoryVO toHistoryVO(Long instanceId, HisTask row) {
        return HistoryVO.builder()
                .historyId(row.getId())
                .instanceId(instanceId)
                .nodeCode(row.getNodeCode())
                .nodeName(row.getNodeName())
                .handlerId(row.getApprover())
                .attributes(attributes(row.getExt()))
                .action(actionOf(row))
                .skipType(row.getSkipType())
                .comment(row.getMessage())
                .createTime(time(row.getCreateTime()))
                .updateTime(time(row.getUpdateTime()))
                .build();
    }

    private String actionOf(HisTask row) {
        int cooperateType = intOf(row.getCooperateType(), 1);
        String skipType = str(row.getSkipType());
        if (WorkflowStatusEnum.WITHDRAWN.getCode().equals(str(row.getFlowStatus()))) {
            return TaskActionEnum.WITHDRAW.getCode();
        }
        if (cooperateType == 2) {
            return TaskActionEnum.TRANSFER.getCode();
        }
        if (cooperateType == 3) {
            return TaskActionEnum.DELEGATE.getCode();
        }
        if (cooperateType == 6) {
            return TaskActionEnum.COUNTERSIGN.getCode();
        }
        if ("REJECT".equals(skipType)) {
            return TaskActionEnum.REJECT.getCode();
        }
        if ("WITHDRAW".equals(skipType)) {
            return TaskActionEnum.WITHDRAW.getCode();
        }
        if ("PASS".equals(skipType)) {
            return TaskActionEnum.APPROVE.getCode();
        }
        return TaskActionEnum.APPROVE.getCode();
    }

    private String[] parseCursor(String cursor) {
        if (StrUtil.isBlank(cursor)) {
            return new String[]{"", "0"};
        }
        String[] parts = cursor.split("\\|", 2);
        return parts.length == 2 ? parts : new String[]{parts[0], "0"};
    }

    private void checkReadSize(int size) {
        if (size > WorkflowConstant.MAX_BATCH_READ_SIZE) {
            throw new BusinessException(WorkflowCodeEnum.BATCH_SIZE_EXCEEDED);
        }
    }

    private void checkWriteSize(int size) {
        if (size > WorkflowConstant.MAX_BATCH_WRITE_SIZE) {
            throw new BusinessException(WorkflowCodeEnum.BATCH_SIZE_EXCEEDED);
        }
    }

    private int codeOf(BusinessException e) {
        return e.getCode() == null ? WorkflowCodeEnum.ENGINE_ERROR.getCode() : e.getCode().getCode();
    }

    private String str(Object value) {
        return Convert.toStr(value, "");
    }

    private long longOf(Object value) {
        return Convert.toLong(value, 0L);
    }

    private int intOf(Object value, int defaultValue) {
        return Convert.toInt(value, defaultValue);
    }

    private String time(Object value) {
        return value == null ? "" : Convert.toStr(value);
    }

    private Map<String, Object> attributes(Object value) {
        if (value == null) {
            return new HashMap<>();
        }
        try {
            Map<String, Object> parsed = objectMapper.readValue(str(value), Map.class);
            return parsed == null ? new HashMap<>() : parsed;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private String json(Object value) {
        try {
            return objectMapper.writeValueAsString(value == null ? Collections.emptyMap() : value);
        } catch (Exception e) {
            throw new BusinessException(WorkflowCodeEnum.ENGINE_ERROR);
        }
    }

    private static class HandlePlan {
        private String flowStatus;
        private String skipType;
        private int cooperateType;
        private String nextCode;
        private String nextName;
        private boolean completeTask;
        private boolean updateInstance;
        private boolean insertUsers;
    }
}
