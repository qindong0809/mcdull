package io.gitee.dqcer.mcdull.workflow.web.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工作流中心查询与办理 SQL（直接面向 warm-flow 的 flow_* 表）
 *
 * @author dqcer
 */
public interface WorkflowCenterMapper {

    Map<String, Object> selectOverview();

    List<Map<String, Object>> selectTrend();

    Page<Map<String, Object>> selectDefinitions(@Param("page") Page<?> page, @Param("p") Map<String, Object> p);

    Map<String, Object> selectDefinition(@Param("id") Long id);

    List<Map<String, Object>> selectNodes(@Param("defId") Long defId);

    List<Map<String, Object>> selectSkips(@Param("defId") Long defId);

    Page<Map<String, Object>> selectInstances(@Param("page") Page<?> page, @Param("p") Map<String, Object> p);

    Map<String, Object> selectInstance(@Param("id") Long id);

    Page<Map<String, Object>> selectTasks(@Param("page") Page<?> page, @Param("p") Map<String, Object> p);

    List<Map<String, Object>> selectCopied();

    List<Map<String, Object>> selectStarted(@Param("userId") String userId);

    Page<Map<String, Object>> selectHistories(@Param("page") Page<?> page, @Param("p") Map<String, Object> p);

    int insertDefinition(Map<String, Object> def);

    int updateDefinition(Map<String, Object> def);

    int deleteNodes(@Param("defId") Long defId);

    int deleteSkips(@Param("defId") Long defId);

    int insertNode(Map<String, Object> node);

    int insertSkip(Map<String, Object> skip);

    Map<String, Object> selectNextNode(@Param("taskId") Long taskId);

    Map<String, Object> selectTaskInfo(@Param("taskId") Long taskId);

    int completeTask(@Param("taskId") Long taskId, @Param("status") String status);

    int insertHisTask(Map<String, Object> his);

    int updateInstanceNode(@Param("instanceId") Long instanceId, @Param("nodeCode") String nodeCode,
                           @Param("nodeName") String nodeName, @Param("status") String status);

    int terminateInstance(@Param("instanceId") Long instanceId);
}
