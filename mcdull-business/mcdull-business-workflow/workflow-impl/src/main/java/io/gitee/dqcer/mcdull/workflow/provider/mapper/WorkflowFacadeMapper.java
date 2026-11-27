package io.gitee.dqcer.mcdull.workflow.web.mapper;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * workflow facade 专用 Mapper。
 * <p>
 * 只查询 flow_* 表，不允许关联其他服务拥有权的业务表。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public interface WorkflowFacadeMapper {

    List<Map<String, Object>> selectDefinitionsByKeys(@Param("tenantId") String tenantId,
                                                      @Param("keys") List<Map<String, Object>> keys);

    List<Map<String, Object>> selectDefinitionNodes(@Param("tenantId") String tenantId,
                                                    @Param("definitionIds") List<Long> definitionIds);

    List<Map<String, Object>> selectDefinitionSkips(@Param("tenantId") String tenantId,
                                                    @Param("definitionIds") List<Long> definitionIds);

    Map<String, Object> selectDefinitionByCode(@Param("tenantId") String tenantId,
                                               @Param("flowCode") String flowCode);

    List<Map<String, Object>> selectFirstNodes(@Param("tenantId") String tenantId,
                                               @Param("definitionId") Long definitionId);

    Map<String, Object> selectInstanceByBusinessKey(@Param("tenantId") String tenantId,
                                                    @Param("flowCode") String flowCode,
                                                    @Param("businessId") String businessId);

    List<Map<String, Object>> selectInstancesByBusinessKeys(@Param("tenantId") String tenantId,
                                                            @Param("keys") List<ProcessBusinessKey> keys);

    List<Map<String, Object>> selectApproversByInstanceIds(@Param("tenantId") String tenantId,
                                                           @Param("instanceIds") List<Long> instanceIds);

    int insertInstance(@Param("row") Map<String, Object> row);

    int insertTask(@Param("row") Map<String, Object> row);

    int updateInstanceSnapshot(@Param("tenantId") String tenantId,
                               @Param("instanceId") Long instanceId,
                               @Param("flowCode") String flowCode,
                               @Param("starterId") String starterId,
                               @Param("starterName") String starterName,
                               @Param("businessTitle") String businessTitle,
                               @Param("businessType") String businessType,
                               @Param("revision") Long revision);

    int insertFlowUser(@Param("row") Map<String, Object> row);

    int insertEvent(@Param("row") Map<String, Object> row);

    Map<String, Object> selectIdempotency(@Param("tenantId") String tenantId,
                                          @Param("idempotencyKey") String idempotencyKey);

    int insertIdempotency(@Param("row") Map<String, Object> row);

    int updateIdempotencySuccess(@Param("tenantId") String tenantId,
                                 @Param("idempotencyKey") String idempotencyKey,
                                 @Param("responseJson") String responseJson);

    int updateIdempotencyFailed(@Param("tenantId") String tenantId,
                                @Param("idempotencyKey") String idempotencyKey,
                                @Param("errorCode") int errorCode,
                                @Param("errorMessage") String errorMessage);

    List<Map<String, Object>> selectTodoCounts(@Param("tenantId") String tenantId,
                                               @Param("assigneeIds") List<String> assigneeIds,
                                               @Param("flowCodes") List<String> flowCodes,
                                               @Param("groupBy") String groupBy);

    List<Map<String, Object>> selectTodoPage(@Param("tenantId") String tenantId,
                                             @Param("assigneeId") String assigneeId,
                                             @Param("keyword") String keyword,
                                             @Param("flowCode") String flowCode,
                                             @Param("cursorTime") String cursorTime,
                                             @Param("cursorId") Long cursorId,
                                             @Param("limit") int limit);

    Long countTodo(@Param("tenantId") String tenantId,
                   @Param("assigneeId") String assigneeId,
                   @Param("keyword") String keyword,
                   @Param("flowCode") String flowCode);

    List<Map<String, Object>> selectTasksByIds(@Param("tenantId") String tenantId,
                                               @Param("taskIds") List<Long> taskIds,
                                               @Param("assigneeId") String assigneeId);

    List<Map<String, Object>> selectAssigneesByIds(@Param("tenantId") String tenantId,
                                                   @Param("taskIds") List<Long> taskIds);

    List<Map<String, Object>> selectHistoriesByInstanceIds(@Param("tenantId") String tenantId,
                                                           @Param("instanceIds") List<Long> instanceIds,
                                                           @Param("limitPerInstance") int limitPerInstance);

    List<Map<String, Object>> selectLatestApprovals(@Param("tenantId") String tenantId,
                                                    @Param("keys") List<ProcessBusinessKey> keys);

    Map<String, Object> selectTaskForHandle(@Param("tenantId") String tenantId,
                                            @Param("taskId") Long taskId);

    int countAssignee(@Param("tenantId") String tenantId,
                      @Param("taskId") Long taskId,
                      @Param("operatorId") String operatorId);

    List<Map<String, Object>> selectNextNode(@Param("tenantId") String tenantId,
                                             @Param("definitionId") Long definitionId,
                                             @Param("nodeCode") String nodeCode);

    int insertHistory(@Param("row") Map<String, Object> row);

    int insertHandleUsers(@Param("rows") List<Map<String, Object>> rows);

    int completeTask(@Param("tenantId") String tenantId,
                     @Param("taskId") Long taskId,
                     @Param("flowStatus") String flowStatus,
                     @Param("expectedRevision") Long expectedRevision);

    int updateInstanceForHandle(@Param("tenantId") String tenantId,
                                @Param("instanceId") Long instanceId,
                                @Param("flowStatus") String flowStatus,
                                @Param("nodeCode") String nodeCode,
                                @Param("nodeName") String nodeName,
                                @Param("expectedRevision") Long expectedRevision);
}
