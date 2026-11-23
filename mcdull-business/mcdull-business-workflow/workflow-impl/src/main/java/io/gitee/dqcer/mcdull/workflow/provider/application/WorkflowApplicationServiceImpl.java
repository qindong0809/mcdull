package io.gitee.dqcer.mcdull.workflow.provider.application;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
import io.gitee.dqcer.mcdull.workflow.facade.enums.TaskActionEnum;
import io.gitee.dqcer.mcdull.workflow.facade.enums.WorkflowCodeEnum;
import io.gitee.dqcer.mcdull.workflow.facade.enums.WorkflowStatusEnum;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BaseWorkflowRequest;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.CursorPageVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ItemResult;
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
import io.gitee.dqcer.mcdull.workflow.web.mapper.WorkflowFacadeMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

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
    @Lazy
    private WorkflowApplicationService self;

    @Override
    public BatchQueryResult<String, DefinitionVO> batchGetDefinitions(DefinitionBatchQueryDTO dto) {
        checkReadSize(dto.getDefinitionKeys().size());
        String tenant = tenant(dto.getTenantId());
        List<Map<String, Object>> keys = definitionKeys(dto.getDefinitionKeys());
        List<Map<String, Object>> rows = workflowFacadeMapper.selectDefinitionsByKeys(tenant, keys);
        Map<Long, Map<String, Object>> byId = new LinkedHashMap<>();
        Map<String, Map<String, Object>> byCode = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            byId.put(longOf(row.get("definitionId")), row);
            byCode.put(str(row.get("flowCode")), row);
        }
        BatchQueryResult<String, DefinitionVO> result = BatchQueryResult.empty();
        for (String key : dto.getDefinitionKeys()) {
            Map<String, Object> row = findDefinition(key, byId, byCode);
            result.add(key, row == null ? null : toDefinitionVO(key, row));
        }
        return result;
    }

    @Override
    public BatchQueryResult<String, DefinitionVersionVO> batchGetDefinitionVersions(DefinitionVersionBatchDTO dto) {
        checkReadSize(dto.getDefinitionKeys().size());
        String tenant = tenant(dto.getTenantId());
        List<Map<String, Object>> keys = definitionKeys(dto.getDefinitionKeys());
        List<Map<String, Object>> rows = workflowFacadeMapper.selectDefinitionsByKeys(tenant, keys);
        Map<Long, Map<String, Object>> byId = new LinkedHashMap<>();
        Map<String, Map<String, Object>> byCode = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            byId.put(longOf(row.get("definitionId")), row);
            byCode.put(str(row.get("flowCode")), row);
        }
        BatchQueryResult<String, DefinitionVersionVO> result = BatchQueryResult.empty();
        for (String key : dto.getDefinitionKeys()) {
            Map<String, Object> row = findDefinition(key, byId, byCode);
            if (row == null) {
                result.add(key, null);
                continue;
            }
            result.add(key, DefinitionVersionVO.builder()
                    .definitionKey(key)
                    .version(str(row.get("version")))
                    .updateTime(time(row.get("updateTime")))
                    .publishStatus(intOf(row.get("publishStatus"), 0))
                    .activityStatus(intOf(row.get("activityStatus"), 1))
                    .build());
        }
        return result;
    }

    @Override
    public BatchQueryResult<String, DiagramVO> batchGetDiagrams(DiagramBatchQueryDTO dto) {
        checkReadSize(dto.getDefinitionKeys().size());
        String tenant = tenant(dto.getTenantId());
        List<Map<String, Object>> keys = definitionKeys(dto.getDefinitionKeys());
        List<Map<String, Object>> rows = workflowFacadeMapper.selectDefinitionsByKeys(tenant, keys);
        Map<Long, Map<String, Object>> byId = new LinkedHashMap<>();
        Map<String, Map<String, Object>> byCode = new LinkedHashMap<>();
        Set<Long> definitionIds = new LinkedHashSet<>();
        for (Map<String, Object> row : rows) {
            Long id = longOf(row.get("definitionId"));
            byId.put(id, row);
            byCode.put(str(row.get("flowCode")), row);
            definitionIds.add(id);
        }
        Map<Long, List<DiagramVO.DiagramNodeVO>> nodes = new LinkedHashMap<>();
        Map<Long, List<DiagramVO.DiagramEdgeVO>> edges = new LinkedHashMap<>();
        if (!definitionIds.isEmpty()) {
            List<Long> ids = new ArrayList<>(definitionIds);
            for (Map<String, Object> row : workflowFacadeMapper.selectDefinitionNodes(tenant, ids)) {
                Long definitionId = longOf(row.get("definitionId"));
                nodes.computeIfAbsent(definitionId, k -> new ArrayList<>()).add(DiagramVO.DiagramNodeVO.builder()
                        .nodeCode(str(row.get("nodeCode")))
                        .nodeName(str(row.get("nodeName")))
                        .nodeType(intOf(row.get("nodeType"), 0))
                        .coordinate(str(row.get("coordinate")))
                        .handlerPath(str(row.get("handlerPath")))
                        .permissionFlag(str(row.get("permissionFlag")))
                        .build());
            }
            for (Map<String, Object> row : workflowFacadeMapper.selectDefinitionSkips(tenant, ids)) {
                Long definitionId = longOf(row.get("definitionId"));
                edges.computeIfAbsent(definitionId, k -> new ArrayList<>()).add(DiagramVO.DiagramEdgeVO.builder()
                        .fromNodeCode(str(row.get("fromNodeCode")))
                        .toNodeCode(str(row.get("toNodeCode")))
                        .label(str(row.get("label")))
                        .condition(str(row.get("condition")))
                        .skipType(str(row.get("skipType")))
                        .build());
            }
        }
        BatchQueryResult<String, DiagramVO> result = BatchQueryResult.empty();
        for (String key : dto.getDefinitionKeys()) {
            Map<String, Object> row = findDefinition(key, byId, byCode);
            if (row == null) {
                result.add(key, null);
                continue;
            }
            Long definitionId = longOf(row.get("definitionId"));
            result.add(key, DiagramVO.builder()
                    .definitionKey(key)
                    .nodes(nodes.getOrDefault(definitionId, Collections.emptyList()))
                    .edges(edges.getOrDefault(definitionId, Collections.emptyList()))
                    .build());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StartProcessResult startProcess(StartProcessDTO dto) {
        String tenant = tenant(dto.getTenantId());
        return withIdempotency(tenant, dto.getIdempotencyKey(), dto, StartProcessResult.class, () -> doStart(tenant, dto));
    }

    @Override
    public BatchResult<StartProcessResult> batchStartProcesses(BatchStartProcessDTO dto) {
        checkWriteSize(dto.getItems().size());
        List<ItemResult<StartProcessResult>> items = new ArrayList<>();
        for (StartProcessDTO raw : dto.getItems()) {
            StartProcessDTO item = inheritStart(raw, dto);
            String itemKey = StrUtil.isNotBlank(raw.getIdempotencyKey())
                    ? raw.getIdempotencyKey()
                    : raw.getFlowCode() + ":" + raw.getBusinessId();
            try {
                items.add(ItemResult.success(itemKey, self.startProcess(item)));
            } catch (BusinessException e) {
                items.add(ItemResult.fail(itemKey, codeOf(e), e.getMessage()));
            } catch (RuntimeException e) {
                items.add(ItemResult.fail(itemKey, WorkflowCodeEnum.ENGINE_ERROR.getCode(), e.getMessage()));
            }
        }
        return BatchResult.of(items);
    }

    @Override
    public BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> batchGetInstancesByBusinessKeys(BusinessKeyBatchQueryDTO dto) {
        return queryInstanceStatuses(dto);
    }

    @Override
    public BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> batchGetInstanceStatuses(BusinessKeyBatchQueryDTO dto) {
        return queryInstanceStatuses(dto);
    }

    @Override
    public List<TodoCountVO> batchCountTodo(TodoCountBatchDTO dto) {
        checkReadSize(dto.getAssigneeIds().size());
        String tenant = tenant(dto.getTenantId());
        List<String> assignees = new ArrayList<>(new LinkedHashSet<>(dto.getAssigneeIds()));
        List<Map<String, Object>> rows = workflowFacadeMapper.selectTodoCounts(
                tenant, assignees, dto.getFlowCodes(), dto.getGroupBy());
        List<TodoCountVO> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            result.add(TodoCountVO.builder()
                    .assigneeId(str(row.get("assigneeId")))
                    .flowCode(str(row.get("flowCode")))
                    .count(intOf(row.get("cnt"), 0))
                    .build());
        }
        return result;
    }

    @Override
    public CursorPageVO<TaskVO> pageTodoTasks(TodoCursorQueryDTO dto) {
        String tenant = tenant(dto.getTenantId());
        int limit = dto.getPageSize() == null ? 50 : Math.min(dto.getPageSize(), WorkflowConstant.MAX_PAGE_SIZE);
        String[] cursor = parseCursor(dto.getCursor());
        List<Map<String, Object>> rows = workflowFacadeMapper.selectTodoPage(
                tenant, dto.getAssigneeId(), dto.getKeyword(), dto.getFlowCode(),
                cursor[0], cursor.length > 1 ? Convert.toLong(cursor[1]) : null, limit);
        List<Long> pageTaskIds = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            pageTaskIds.add(longOf(row.get("taskId")));
        }
        Map<Long, List<String>> assigneeIds = new LinkedHashMap<>();
        Map<Long, List<String>> assigneeNames = new LinkedHashMap<>();
        if (!pageTaskIds.isEmpty()) {
            collectAssignees(workflowFacadeMapper.selectAssigneesByIds(tenant, pageTaskIds),
                    assigneeIds, assigneeNames);
        }
        List<TaskVO> list = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            list.add(toTaskVO(row, assigneeIds, assigneeNames));
        }
        String nextCursor = "";
        if (!rows.isEmpty() && list.size() == limit) {
            Map<String, Object> last = rows.get(rows.size() - 1);
            nextCursor = time(last.get("createTime")) + "|" + str(last.get("taskId"));
        }
        Long total = null;
        if (dto.isNeedTotal()) {
            total = workflowFacadeMapper.countTodo(tenant, dto.getAssigneeId(), dto.getKeyword(), dto.getFlowCode());
        }
        return CursorPageVO.<TaskVO>builder()
                .list(list)
                .nextCursor(nextCursor)
                .hasMore(!rows.isEmpty() && list.size() == limit)
                .total(total)
                .build();
    }

    @Override
    public BatchQueryResult<Long, TaskVO> batchGetTasks(TaskBatchQueryDTO dto) {
        checkReadSize(dto.getTaskIds().size());
        String tenant = tenant(dto.getTenantId());
        List<Long> ids = new ArrayList<>(new LinkedHashSet<>(dto.getTaskIds()));
        List<Map<String, Object>> rows = workflowFacadeMapper.selectTasksByIds(tenant, ids, null);
        Map<Long, List<String>> assigneeIds = new LinkedHashMap<>();
        Map<Long, List<String>> assigneeNames = new LinkedHashMap<>();
        collectAssignees(workflowFacadeMapper.selectAssigneesByIds(tenant, ids), assigneeIds, assigneeNames);
        Map<Long, TaskVO> byId = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            Long taskId = longOf(row.get("taskId"));
            byId.put(taskId, toTaskVO(row, assigneeIds, assigneeNames));
        }
        BatchQueryResult<Long, TaskVO> result = BatchQueryResult.empty();
        for (Long id : dto.getTaskIds()) {
            result.add(id, byId.get(id));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskHandleResult handleTask(HandleTaskDTO dto) {
        String tenant = tenant(dto.getTenantId());
        return withIdempotency(tenant, dto.getIdempotencyKey(), dto, TaskHandleResult.class, () -> doHandle(tenant, dto));
    }

    @Override
    public BatchResult<TaskHandleResult> batchHandleTasks(BatchHandleTaskDTO dto) {
        checkWriteSize(dto.getItems().size());
        List<ItemResult<TaskHandleResult>> items = new ArrayList<>();
        for (HandleTaskDTO raw : dto.getItems()) {
            HandleTaskDTO item = inheritHandle(raw, dto);
            String itemKey = StrUtil.isNotBlank(raw.getIdempotencyKey())
                    ? raw.getIdempotencyKey()
                    : str(raw.getTaskId());
            try {
                items.add(ItemResult.success(itemKey, self.handleTask(item)));
            } catch (BusinessException e) {
                items.add(ItemResult.fail(itemKey, codeOf(e), e.getMessage()));
            } catch (RuntimeException e) {
                items.add(ItemResult.fail(itemKey, WorkflowCodeEnum.ENGINE_ERROR.getCode(), e.getMessage()));
            }
        }
        return BatchResult.of(items);
    }

    @Override
    public BatchQueryResult<Long, List<HistoryVO>> batchGetHistoriesByInstanceIds(HistoryBatchQueryDTO dto) {
        checkReadSize(dto.getInstanceIds().size());
        String tenant = tenant(dto.getTenantId());
        int limit = dto.getLimitPerInstance() == null
                ? 50 : Math.min(dto.getLimitPerInstance(), WorkflowConstant.MAX_PAGE_SIZE);
        List<Long> ids = new ArrayList<>(new LinkedHashSet<>(dto.getInstanceIds()));
        List<Map<String, Object>> rows = workflowFacadeMapper.selectHistoriesByInstanceIds(tenant, ids, limit);
        Map<Long, List<HistoryVO>> byInstance = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            Long instanceId = longOf(row.get("instanceId"));
            byInstance.computeIfAbsent(instanceId, k -> new ArrayList<>()).add(HistoryVO.builder()
                    .historyId(longOf(row.get("historyId")))
                    .instanceId(instanceId)
                    .nodeCode(str(row.get("nodeCode")))
                    .nodeName(str(row.get("nodeName")))
                    .handlerId(str(row.get("handlerId")))
                    .handlerName(str(row.get("handlerName")))
                    .action(actionOf(row))
                    .skipType(str(row.get("skipType")))
                    .comment(str(row.get("comment")))
                    .createTime(time(row.get("createTime")))
                    .updateTime(time(row.get("updateTime")))
                    .build());
        }
        BatchQueryResult<Long, List<HistoryVO>> result = BatchQueryResult.empty();
        for (Long id : dto.getInstanceIds()) {
            result.add(id, byInstance.getOrDefault(id, Collections.emptyList()));
        }
        return result;
    }

    @Override
    public BatchQueryResult<ProcessBusinessKey, LatestApprovalVO> batchGetLatestApprovals(BusinessKeyBatchQueryDTO dto) {
        checkReadSize(dto.getKeys().size());
        String tenant = tenant(dto.getTenantId());
        List<Map<String, Object>> rows = workflowFacadeMapper.selectLatestApprovals(tenant, dto.getKeys());
        Map<String, LatestApprovalVO> byKey = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            String flowCode = str(row.get("flowCode"));
            String businessId = str(row.get("businessId"));
            String flowStatus = str(row.get("flowStatus"));
            byKey.put(flowCode + "|" + businessId, LatestApprovalVO.builder()
                    .instanceId(longOf(row.get("instanceId")))
                    .flowCode(flowCode)
                    .businessId(businessId)
                    .flowStatus(flowStatus)
                    .statusText(WorkflowStatusEnum.textOf(flowStatus))
                    .handlerId(str(row.get("handlerId")))
                    .handlerName(str(row.get("handlerName")))
                    .action(actionOf(row))
                    .comment(str(row.get("comment")))
                    .updateTime(time(row.get("updateTime")))
                    .build());
        }
        BatchQueryResult<ProcessBusinessKey, LatestApprovalVO> result = BatchQueryResult.empty();
        for (ProcessBusinessKey key : dto.getKeys()) {
            result.add(key, byKey.get(key.getFlowCode() + "|" + key.getBusinessId()));
        }
        return result;
    }

    // ==================== 私有方法 ====================

    private BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> queryInstanceStatuses(BusinessKeyBatchQueryDTO dto) {
        checkReadSize(dto.getKeys().size());
        String tenant = tenant(dto.getTenantId());
        List<Map<String, Object>> rows = workflowFacadeMapper.selectInstancesByBusinessKeys(tenant, dto.getKeys());
        Map<String, Map<String, Object>> byKey = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            byKey.put(str(row.get("flowCode")) + "|" + str(row.get("businessId")), row);
        }
        List<Long> instanceIds = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            instanceIds.add(longOf(row.get("id")));
        }
        Map<Long, List<String>> approverIds = new LinkedHashMap<>();
        Map<Long, List<String>> approverNames = new LinkedHashMap<>();
        if (!instanceIds.isEmpty()) {
            List<Map<String, Object>> approvers = workflowFacadeMapper.selectApproversByInstanceIds(tenant, instanceIds);
            for (Map<String, Object> row : approvers) {
                Long instanceId = longOf(row.get("instanceId"));
                approverIds.computeIfAbsent(instanceId, k -> new ArrayList<>()).add(str(row.get("assigneeId")));
                approverNames.computeIfAbsent(instanceId, k -> new ArrayList<>()).add(str(row.get("assigneeName")));
            }
        }
        BatchQueryResult<ProcessBusinessKey, InstanceStatusVO> result = BatchQueryResult.empty();
        for (ProcessBusinessKey key : dto.getKeys()) {
            Map<String, Object> row = byKey.get(key.getFlowCode() + "|" + key.getBusinessId());
            if (row == null) {
                result.add(key, null);
                continue;
            }
            Long instanceId = longOf(row.get("id"));
            String flowStatus = str(row.get("flowStatus"));
            result.add(key, InstanceStatusVO.builder()
                    .instanceId(instanceId)
                    .flowCode(key.getFlowCode())
                    .businessId(key.getBusinessId())
                    .businessType(str(row.get("business_type")))
                    .businessTitle(str(row.get("business_title")))
                    .flowStatus(flowStatus)
                    .statusText(WorkflowStatusEnum.textOf(flowStatus))
                    .currentNodeCode(str(row.get("node_code")))
                    .currentNodeName(str(row.get("node_name")))
                    .currentApproverIds(approverIds.getOrDefault(instanceId, Collections.emptyList()))
                    .currentApproverNames(approverNames.getOrDefault(instanceId, Collections.emptyList()))
                    .starterId(str(row.get("starter_id")))
                    .starterName(str(row.get("starter_name")))
                    .updateTime(time(row.get("update_time")))
                    .build());
        }
        return result;
    }

    private StartProcessResult doStart(String tenant, StartProcessDTO dto) {
        Map<String, Object> definition = workflowFacadeMapper.selectDefinitionByCode(tenant, dto.getFlowCode());
        if (definition == null) {
            throw new BusinessException(WorkflowCodeEnum.DEFINITION_NOT_FOUND);
        }
        Long definitionId = longOf(definition.get("id"));
        Map<String, Object> exists = workflowFacadeMapper.selectInstanceByBusinessKey(
                tenant, dto.getFlowCode(), dto.getBusinessId());
        if (exists != null && !WorkflowStatusEnum.isFinal(str(exists.get("flow_status")))) {
            throw new BusinessException(WorkflowCodeEnum.BUSINESS_KEY_CONFLICT);
        }
        List<Map<String, Object>> firstNodes = workflowFacadeMapper.selectFirstNodes(tenant, definitionId);
        if (firstNodes == null || firstNodes.isEmpty()) {
            throw new BusinessException(WorkflowCodeEnum.ENGINE_ERROR);
        }
        Map<String, Object> firstNode = firstNodes.get(0);
        Long instanceId = IdUtil.getSnowflakeNextId();
        String operatorId = StrUtil.isBlank(dto.getOperatorId()) ? "0" : dto.getOperatorId();
        Map<String, Object> instance = new HashMap<>();
        instance.put("id", instanceId);
        instance.put("definitionId", definitionId);
        instance.put("flowCode", dto.getFlowCode());
        instance.put("businessId", dto.getBusinessId());
        instance.put("businessType", dto.getBusinessType());
        instance.put("businessTitle", dto.getBusinessTitle());
        instance.put("nodeType", 1);
        instance.put("nodeCode", str(firstNode.get("node_code")));
        instance.put("nodeName", str(firstNode.get("node_name")));
        instance.put("variable", json(dto.getVariables()));
        instance.put("flowStatus", WorkflowStatusEnum.RUNNING.getCode());
        instance.put("activityStatus", 1);
        instance.put("createBy", operatorId);
        instance.put("starterId", operatorId);
        instance.put("starterName", StrUtil.isBlank(dto.getOperatorName()) ? operatorId : dto.getOperatorName());
        instance.put("ext", "");
        instance.put("revision", 0L);
        instance.put("tenantId", tenant);
        workflowFacadeMapper.insertInstance(instance);
        insertEvent(tenant, "PROCESS_STARTED", str(instanceId), instance);

        for (Map<String, Object> node : firstNodes) {
            Long taskId = IdUtil.getSnowflakeNextId();
            Map<String, Object> task = new HashMap<>();
            task.put("id", taskId);
            task.put("definitionId", definitionId);
            task.put("instanceId", instanceId);
            task.put("nodeCode", str(node.get("node_code")));
            task.put("nodeName", str(node.get("node_name")));
            task.put("nodeType", 1);
            task.put("flowStatus", WorkflowStatusEnum.RUNNING.getCode());
            task.put("formCustom", "N");
            task.put("formPath", str(node.get("form_path")));
            task.put("revision", 0L);
            task.put("tenantId", tenant);
            workflowFacadeMapper.insertTask(task);
            List<String> assignees = resolveAssignees(dto, str(node.get("handler_path")), operatorId);
            for (String assignee : assignees) {
                insertFlowUser(tenant, taskId, "1", assignee,
                        StrUtil.isBlank(dto.getAssigneeNames().get(assignee))
                                ? assignee : dto.getAssigneeNames().get(assignee), operatorId);
            }
            insertEvent(tenant, "TASK_CREATED", str(taskId), task);
        }
        return StartProcessResult.builder()
                .instanceId(instanceId)
                .flowCode(dto.getFlowCode())
                .businessId(dto.getBusinessId())
                .flowStatus(WorkflowStatusEnum.RUNNING.getCode())
                .currentNodeCode(str(firstNode.get("node_code")))
                .currentNodeName(str(firstNode.get("node_name")))
                .createTime(LocalDateTime.now().toString())
                .build();
    }

    private TaskHandleResult doHandle(String tenant, HandleTaskDTO dto) {
        TaskActionEnum action = TaskActionEnum.of(dto.getAction());
        if (action == null) {
            throw new BusinessException(WorkflowCodeEnum.TASK_ACTION_INVALID);
        }
        Map<String, Object> task = workflowFacadeMapper.selectTaskForHandle(tenant, dto.getTaskId());
        if (task == null) {
            throw new BusinessException(WorkflowCodeEnum.TASK_NOT_FOUND);
        }
        String instanceStatus = str(task.get("instanceStatus"));
        if (!WorkflowStatusEnum.RUNNING.getCode().equals(instanceStatus)) {
            throw new BusinessException(WorkflowCodeEnum.INSTANCE_STATUS_INVALID);
        }
        String operator = StrUtil.isBlank(dto.getOperatorId()) ? "0" : dto.getOperatorId();
        if (workflowFacadeMapper.countAssignee(tenant, dto.getTaskId(), operator) == 0) {
            throw new BusinessException(WorkflowCodeEnum.TASK_NOT_ASSIGNED);
        }
        Long expectedRevision = dto.getExpectedRevision() == null
                ? longOf(task.get("taskRevision")) : dto.getExpectedRevision();
        Long expectedInstanceRevision = dto.getExpectedRevision() == null
                ? longOf(task.get("instanceRevision")) : dto.getExpectedRevision();
        Long taskId = longOf(task.get("taskId"));
        Long instanceId = longOf(task.get("instanceId"));
        Long definitionId = longOf(task.get("definitionId"));
        String operatorName = StrUtil.isBlank(dto.getOperatorName()) ? operator : dto.getOperatorName();

        HandlePlan plan = buildPlan(tenant, action, task, definitionId);
        if (action == TaskActionEnum.URGE) {
            insertEvent(tenant, "TASK_URGED", str(taskId), task);
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
        history.put("nodeCode", str(task.get("nodeCode")));
        history.put("nodeName", str(task.get("nodeName")));
        history.put("targetNodeCode", plan.nextCode);
        history.put("approver", operator);
        history.put("approverId", operator);
        history.put("approverName", operatorName);
        history.put("cooperateType", plan.cooperateType);
        history.put("skipType", plan.skipType);
        history.put("flowStatus", plan.flowStatus);
        history.put("message", dto.getComment());
        history.put("variable", json(dto.getVariables()));
        history.put("tenantId", tenant);
        workflowFacadeMapper.insertHistory(history);

        if (plan.insertUsers) {
            List<String> targets = dto.getTargetAssigneeIds() == null || dto.getTargetAssigneeIds().isEmpty()
                    ? Collections.singletonList(operator) : dto.getTargetAssigneeIds();
            List<Map<String, Object>> userRows = new ArrayList<>();
            for (String target : targets) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", IdUtil.getSnowflakeNextId());
                row.put("type", str(plan.cooperateType));
                row.put("processedBy", target);
                row.put("processedName", StrUtil.isBlank(dto.getTargetAssigneeNames().get(target))
                        ? target : dto.getTargetAssigneeNames().get(target));
                row.put("associated", taskId);
                row.put("createBy", operator);
                row.put("tenantId", tenant);
                userRows.add(row);
            }
            workflowFacadeMapper.insertHandleUsers(userRows);
        }

        if (plan.completeTask) {
            int updated = workflowFacadeMapper.completeTask(
                    tenant, taskId, plan.flowStatus, expectedRevision);
            if (updated == 0) {
                throw new BusinessException(WorkflowCodeEnum.CONCURRENT_CONFLICT);
            }
        }
        if (plan.updateInstance) {
            int updated = workflowFacadeMapper.updateInstanceForHandle(
                    tenant, instanceId, plan.flowStatus, plan.nextCode, plan.nextName, expectedInstanceRevision);
            if (updated == 0) {
                throw new BusinessException(WorkflowCodeEnum.CONCURRENT_CONFLICT);
            }
        }
        insertEvent(tenant, "TASK_COMPLETED", str(historyId), history);
        if (WorkflowStatusEnum.isFinal(plan.flowStatus)) {
            insertEvent(tenant, "PROCESS_COMPLETED", str(instanceId), history);
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

    private HandlePlan buildPlan(String tenant, TaskActionEnum action,
                                 Map<String, Object> task, Long definitionId) {
        HandlePlan plan = new HandlePlan();
        plan.skipType = "NONE";
        plan.cooperateType = 1;
        plan.nextCode = "";
        plan.nextName = "";
        switch (action) {
            case APPROVE -> {
                List<Map<String, Object>> nextNodes = workflowFacadeMapper.selectNextNode(
                        tenant, definitionId, str(task.get("nodeCode")));
                Map<String, Object> next = nextNodes == null || nextNodes.isEmpty() ? null : nextNodes.get(0);
                plan.skipType = "PASS";
                if (next != null && StrUtil.isNotBlank(str(next.get("nodeCode")))) {
                    plan.flowStatus = WorkflowStatusEnum.RUNNING.getCode();
                    plan.nextCode = str(next.get("nodeCode"));
                    plan.nextName = str(next.get("nodeName"));
                } else {
                    plan.flowStatus = WorkflowStatusEnum.COMPLETED.getCode();
                }
                plan.completeTask = true;
                plan.updateInstance = true;
            }
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

    private <T> T withIdempotency(String tenant, String key, Object request, Class<T> type, Supplier<T> action) {
        if (StrUtil.isBlank(key)) {
            return action.get();
        }
        String hash = md5(json(request));
        Map<String, Object> existing = workflowFacadeMapper.selectIdempotency(tenant, key);
        if (existing == null) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", IdUtil.getSnowflakeNextId());
            row.put("tenantId", tenant);
            row.put("idempotencyKey", key);
            row.put("requestHash", hash);
            row.put("tenantId", tenant);
            try {
                workflowFacadeMapper.insertIdempotency(row);
            } catch (DuplicateKeyException e) {
                existing = workflowFacadeMapper.selectIdempotency(tenant, key);
            }
        }
        if (existing != null) {
            if (!hash.equals(str(existing.get("request_hash")))) {
                throw new BusinessException(WorkflowCodeEnum.IDEMPOTENCY_CONFLICT);
            }
            String status = str(existing.get("status"));
            if ("SUCCESS".equals(status)) {
                return fromJson(str(existing.get("response_json")), type);
            }
            if ("PROCESSING".equals(status)) {
                throw new BusinessException(WorkflowCodeEnum.DUPLICATE_REQUEST);
            }
        }
        try {
            T result = action.get();
            workflowFacadeMapper.updateIdempotencySuccess(tenant, key, json(result));
            return result;
        } catch (BusinessException e) {
            workflowFacadeMapper.updateIdempotencyFailed(
                    tenant, key, codeOf(e), e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            workflowFacadeMapper.updateIdempotencyFailed(
                    tenant, key, WorkflowCodeEnum.ENGINE_ERROR.getCode(), e.getMessage());
            throw e;
        }
    }

    private void insertEvent(String tenant, String eventType, String aggregateId, Object payload) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", IdUtil.getSnowflakeNextId());
        row.put("tenantId", tenant);
        row.put("eventType", eventType);
        row.put("aggregateId", aggregateId);
        row.put("payloadJson", json(payload));
        workflowFacadeMapper.insertEvent(row);
    }

    private void insertFlowUser(String tenant, Long taskId, String type,
                                String processedBy, String processedName, String createBy) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", IdUtil.getSnowflakeNextId());
        row.put("type", type);
        row.put("processedBy", processedBy);
        row.put("processedName", processedName);
        row.put("associated", taskId);
        row.put("createBy", createBy);
        row.put("tenantId", tenant);
        workflowFacadeMapper.insertFlowUser(row);
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
        if (StrUtil.isBlank(item.getTenantId())) {
            item.setTenantId(base.getTenantId());
        }
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
        if (StrUtil.isBlank(item.getTenantId())) {
            item.setTenantId(base.getTenantId());
        }
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

    private List<Map<String, Object>> definitionKeys(List<String> keys) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (String key : keys) {
            Map<String, Object> row = new HashMap<>();
            if (StrUtil.startWith(key, "def_")) {
                try {
                    row.put("type", "ID");
                    row.put("value", Long.valueOf(key.substring(4)));
                    result.add(row);
                    continue;
                } catch (NumberFormatException ignored) {
                    // 非法 def_ 前缀按 flowCode 处理
                }
            }
            row.put("type", "CODE");
            row.put("value", key);
            result.add(row);
        }
        return result;
    }

    private Map<String, Object> findDefinition(String key,
                                               Map<Long, Map<String, Object>> byId,
                                               Map<String, Map<String, Object>> byCode) {
        if (!StrUtil.startWith(key, "def_")) {
            return byCode.get(key);
        }
        try {
            return byId.get(Long.valueOf(key.substring(4)));
        } catch (NumberFormatException e) {
            return byCode.get(key);
        }
    }

    private DefinitionVO toDefinitionVO(String key, Map<String, Object> row) {
        return DefinitionVO.builder()
                .definitionKey(key)
                .definitionId(longOf(row.get("definitionId")))
                .flowCode(str(row.get("flowCode")))
                .flowName(str(row.get("flowName")))
                .category(str(row.get("category")))
                .version(str(row.get("version")))
                .publishStatus(intOf(row.get("publishStatus"), 0))
                .activityStatus(intOf(row.get("activityStatus"), 1))
                .formCustom(str(row.get("formCustom")))
                .formPath(str(row.get("formPath")))
                .ext(str(row.get("ext")))
                .updateTime(time(row.get("updateTime")))
                .build();
    }

    private TaskVO toTaskVO(Map<String, Object> row,
                            Map<Long, List<String>> assigneeIds,
                            Map<Long, List<String>> assigneeNames) {
        Long taskId = longOf(row.get("taskId"));
        return TaskVO.builder()
                .taskId(taskId)
                .instanceId(longOf(row.get("instanceId")))
                .definitionId(longOf(row.get("definitionId")))
                .flowCode(str(row.get("flowCode")))
                .flowStatus(str(row.get("flowStatus")))
                .nodeCode(str(row.get("nodeCode")))
                .nodeName(str(row.get("nodeName")))
                .businessId(str(row.get("businessId")))
                .businessTitle(str(row.get("businessTitle")))
                .starterId(str(row.get("starterId")))
                .starterName(str(row.get("starterName")))
                .assigneeIds(assigneeIds.getOrDefault(taskId, Collections.emptyList()))
                .assigneeNames(assigneeNames.getOrDefault(taskId, Collections.emptyList()))
                .createTime(time(row.get("createTime")))
                .updateTime(time(row.get("updateTime")))
                .revision(longOf(row.get("revision")))
                .build();
    }

    private void collectAssignees(List<Map<String, Object>> rows,
                                  Map<Long, List<String>> ids,
                                  Map<Long, List<String>> names) {
        for (Map<String, Object> row : rows) {
            Long taskId = longOf(row.get("taskId"));
            ids.computeIfAbsent(taskId, k -> new ArrayList<>()).add(str(row.get("assigneeId")));
            names.computeIfAbsent(taskId, k -> new ArrayList<>()).add(str(row.get("assigneeName")));
        }
    }

    private String actionOf(Map<String, Object> row) {
        int cooperateType = intOf(row.get("cooperateType"), 1);
        String skipType = str(row.get("skipType"));
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

    private String tenant(String tenant) {
        return StrUtil.isBlank(tenant) ? "0" : tenant;
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

    private String json(Object value) {
        try {
            return objectMapper.writeValueAsString(value == null ? Collections.emptyMap() : value);
        } catch (Exception e) {
            throw new BusinessException(WorkflowCodeEnum.ENGINE_ERROR);
        }
    }

    private <T> T fromJson(String value, Class<T> type) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        try {
            return objectMapper.readValue(value, type);
        } catch (Exception e) {
            throw new BusinessException(WorkflowCodeEnum.ENGINE_ERROR);
        }
    }

    private String md5(String value) {
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                builder.append(String.format("%02x", b & 0xff));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
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
