package io.gitee.dqcer.mcdull.workflow.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IWorkflowCenterService {

    Map<String, Object> overview();

    PagedVO<Map<String, Object>> definitionList(Map<String, Object> param);

    Map<String, Object> definitionDetail(String id);

    Boolean saveGraph(Map<String, Object> param);

    PagedVO<Map<String, Object>> instanceList(Map<String, Object> param);

    Map<String, Object> instanceDetail(String id);

    Boolean terminate(String id);

    Boolean urge(String id);

    PagedVO<Map<String, Object>> taskList(Map<String, Object> param);

    List<Map<String, Object>> copied();

    List<Map<String, Object>> started();

    Boolean handleTask(Map<String, Object> param);

    PagedVO<Map<String, Object>> historyList(Map<String, Object> param);

    Map<String, Object> diagram(String definitionId);
}
