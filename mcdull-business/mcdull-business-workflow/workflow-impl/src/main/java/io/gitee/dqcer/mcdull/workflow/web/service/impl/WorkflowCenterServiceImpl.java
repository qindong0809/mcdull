package io.gitee.dqcer.mcdull.workflow.web.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.workflow.web.mapper.WorkflowCenterMapper;
import io.gitee.dqcer.mcdull.workflow.web.service.IWorkflowCenterService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
@Service
public class WorkflowCenterServiceImpl implements IWorkflowCenterService {

    @Resource
    private WorkflowCenterMapper workflowCenterMapper;

    @Override
    public Map<String, Object> overview() {
        Map<String, Object> result = workflowCenterMapper.selectOverview();
        if (result == null) {
            result = new HashMap<>();
        }
        result.putIfAbsent("runningInstances", 0);
        result.putIfAbsent("myTodos", 0);
        result.putIfAbsent("done7d", 0);
        result.putIfAbsent("definitions", 0);
        result.putIfAbsent("published", 0);
        result.putIfAbsent("urgentTodos", 0);
        List<Map<String, Object>> trend = workflowCenterMapper.selectTrend();
        result.put("trend", trend == null ? Collections.emptyList() : trend);
        return result;
    }

    @Override
    public PagedVO<Map<String, Object>> definitionList(Map<String, Object> param) {
        Map<String, Object> p = queryParam(param);
        p.put("publishFlag", "published".equals(p.get("status")) ? 1 : 0);
        Page<Map<String, Object>> page = page(param);
        return PageUtil.toPage(workflowCenterMapper.selectDefinitions(page, p));
    }

    @Override
    public Map<String, Object> definitionDetail(String id) {
        Long defId = parseId(id, "def_");
        Map<String, Object> def = workflowCenterMapper.selectDefinition(defId);
        if (def == null) {
            return Collections.emptyMap();
        }
        def.put("nodes", nodeGraph(defId));
        def.put("edges", edgeGraph(defId));
        return def;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveGraph(Map<String, Object> param) {
        Long defId = parseId(Convert.toStr(param.get("definitionId")), "def_");
        String code = Convert.toStr(param.get("flowCode"), "NEW_FLOW");
        String name = Convert.toStr(param.get("flowName"), "新建流程");
        String version = Convert.toStr(param.get("version"), "v1.0");
        int publish = Boolean.TRUE.equals(param.get("publish")) ? 1 : 0;

        if (defId == null || defId == 0L) {
            defId = IdUtil.getSnowflakeNextId();
            Map<String, Object> def = new HashMap<>();
            def.put("id", defId);
            def.put("flowCode", code);
            def.put("flowName", name);
            def.put("category", Convert.toStr(param.get("category"), "通用"));
            def.put("version", version);
            def.put("isPublish", publish);
            def.put("formCustom", "N");
            def.put("formPath", Convert.toStr(param.get("form"), ""));
            def.put("ext", Convert.toStr(param.get("desc"), ""));
            workflowCenterMapper.insertDefinition(def);
        } else {
            Map<String, Object> def = new HashMap<>();
            def.put("id", defId);
            def.put("flowName", name);
            def.put("version", version);
            def.put("isPublish", publish);
            workflowCenterMapper.updateDefinition(def);
        }

        workflowCenterMapper.deleteNodes(defId);
        workflowCenterMapper.deleteSkips(defId);

        List<Map<String, Object>> nodes = castList(param.get("nodes"));
        for (Map<String, Object> n : nodes) {
            String nodeCode = Convert.toStr(n.get("id"));
            if (StrUtil.isBlank(nodeCode)) {
                continue;
            }
            Map<String, Object> row = new HashMap<>();
            row.put("id", IdUtil.getSnowflakeNextId());
            row.put("definitionId", defId);
            row.put("nodeType", nodeType(Convert.toStr(n.get("type"))));
            row.put("nodeCode", nodeCode);
            row.put("nodeName", Convert.toStr(n.get("name"), nodeCode));
            row.put("coordinate", Convert.toInt(n.get("x"), 0) + "," + Convert.toInt(n.get("y"), 0));
            row.put("handlerType", "role");
            row.put("handlerPath", Convert.toStr(n.get("assignee"), ""));
            row.put("permissionFlag", Convert.toStr(n.get("policy"), ""));
            row.put("formPath", Convert.toStr(n.get("form"), ""));
            row.put("ext", "right:" + Convert.toStr(n.get("right"), "可编辑") + ";timeout:" + Convert.toInt(n.get("timeout"), 8));
            row.put("version", version);
            workflowCenterMapper.insertNode(row);
        }

        List<Map<String, Object>> edges = castList(param.get("edges"));
        for (Map<String, Object> e : edges) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", IdUtil.getSnowflakeNextId());
            row.put("definitionId", defId);
            row.put("from", Convert.toStr(e.get("from")));
            row.put("to", Convert.toStr(e.get("to")));
            row.put("label", Convert.toStr(e.get("label"), ""));
            row.put("cond", Convert.toStr(e.get("cond"), ""));
            row.put("skipType", "PASS");
            workflowCenterMapper.insertSkip(row);
        }
        return true;
    }

    @Override
    public PagedVO<Map<String, Object>> instanceList(Map<String, Object> param) {
        Page<Map<String, Object>> page = page(param);
        return PageUtil.toPage(workflowCenterMapper.selectInstances(page, queryParam(param)));
    }

    @Override
    public Map<String, Object> instanceDetail(String id) {
        Map<String, Object> inst = workflowCenterMapper.selectInstance(parseId(id, "FI-"));
        if (inst == null) {
            return Collections.emptyMap();
        }
        inst.put("passed", passedNodes(inst));
        Map<String, Object> def = definitionDetail(Convert.toStr(inst.get("defId")));
        inst.put("def", def);
        return inst;
    }

    @Override
    public Boolean terminate(String id) {
        return workflowCenterMapper.terminateInstance(parseId(id, "FI-")) > 0;
    }

    @Override
    public Boolean urge(String id) {
        // 催办仅发送提醒，不改变流程状态
        return true;
    }

    @Override
    public PagedVO<Map<String, Object>> taskList(Map<String, Object> param) {
        Map<String, Object> p = queryParam(param);
        p.putIfAbsent("userId", UserContextHolder.userIdStr());
        Page<Map<String, Object>> page = page(param);
        Page<Map<String, Object>> result = workflowCenterMapper.selectTasks(page, p);
        for (Map<String, Object> task : result.getRecords()) {
            task.put("pct", timePercent(Convert.toStr(task.get("arrived"))));
            task.put("level", Convert.toInt(task.get("pct"), 0) >= 70 ? "紧急" : "普通");
            task.put("limit", Convert.toInt(task.get("pct"), 0) >= 100 ? "已超时" : "剩余 8 小时");
            task.put("form", parseForm(Convert.toStr(task.get("variable"))));
        }
        return PageUtil.toPage(result);
    }

    @Override
    public List<Map<String, Object>> copied() {
        List<Map<String, Object>> list = workflowCenterMapper.selectCopied();
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public List<Map<String, Object>> started() {
        List<Map<String, Object>> list = workflowCenterMapper.selectStarted(UserContextHolder.userIdStr());
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean handleTask(Map<String, Object> param) {
        Long taskId = parseId(Convert.toStr(param.get("taskId")), "TK-");
        if (taskId == null || taskId == 0L) {
            return false;
        }
        Map<String, Object> task = workflowCenterMapper.selectTaskInfo(taskId);
        if (task == null) {
            return false;
        }
        String result = Convert.toStr(param.get("result"), "同意");
        String message = Convert.toStr(param.get("message"), "");
        Long instanceId = longOf(task.get("instanceId"));
        Long definitionId = longOf(task.get("definitionId"));

        String skipType = "PASS";
        String flowStatus = "2";
        Integer cooperateType = 1;
        String targetNodeCode = "";
        if ("驳回".equals(result)) {
            skipType = "REJECT";
            flowStatus = "9";
        } else if ("转办".equals(result)) {
            skipType = "NONE";
            flowStatus = "1";
            cooperateType = 2;
        } else if ("加签".equals(result)) {
            skipType = "NONE";
            flowStatus = "1";
            cooperateType = 6;
        } else {
            Map<String, Object> next = workflowCenterMapper.selectNextNode(taskId);
            if (next != null && StrUtil.isNotBlank(Convert.toStr(next.get("nodeCode")))) {
                targetNodeCode = Convert.toStr(next.get("nodeCode"));
                flowStatus = "1";
                workflowCenterMapper.updateInstanceNode(instanceId, targetNodeCode,
                        Convert.toStr(next.get("nodeName"), targetNodeCode), "1");
            } else {
                flowStatus = "8";
                workflowCenterMapper.updateInstanceNode(instanceId, "", "", "8");
            }
        }

        Map<String, Object> his = new HashMap<>();
        his.put("id", IdUtil.getSnowflakeNextId());
        his.put("definitionId", definitionId);
        his.put("instanceId", instanceId);
        his.put("taskId", taskId);
        his.put("nodeCode", Convert.toStr(task.get("nodeCode"), ""));
        his.put("nodeName", Convert.toStr(task.get("nodeName"), ""));
        his.put("targetNodeCode", targetNodeCode);
        his.put("approver", UserContextHolder.userIdStr());
        his.put("cooperateType", cooperateType);
        his.put("skipType", skipType);
        his.put("flowStatus", flowStatus);
        his.put("message", message);
        workflowCenterMapper.insertHisTask(his);

        if (!"加签".equals(result)) {
            workflowCenterMapper.completeTask(taskId, flowStatus);
        }
        return true;
    }

    @Override
    public PagedVO<Map<String, Object>> historyList(Map<String, Object> param) {
        Map<String, Object> p = queryParam(param);
        if (StrUtil.isNotBlank(Convert.toStr(p.get("instanceId")))) {
            p.put("instanceId", parseId(Convert.toStr(p.get("instanceId")), "FI-"));
        }
        Page<Map<String, Object>> page = page(param);
        return PageUtil.toPage(workflowCenterMapper.selectHistories(page, p));
    }

    @Override
    public Map<String, Object> diagram(String definitionId) {
        Long defId = parseId(definitionId, "def_");
        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodeGraph(defId));
        result.put("edges", edgeGraph(defId));
        return result;
    }

    private List<Map<String, Object>> nodeGraph(Long defId) {
        List<Map<String, Object>> rows = workflowCenterMapper.selectNodes(defId);
        List<Map<String, Object>> nodes = new ArrayList<>();
        if (rows == null) {
            return nodes;
        }
        for (Map<String, Object> row : rows) {
            String coordinate = Convert.toStr(row.get("coordinate"), "");
            int x = 290, y = 40;
            String[] xy = coordinate.split(",");
            if (xy.length == 2) {
                x = Convert.toInt(xy[0].trim(), x);
                y = Convert.toInt(xy[1].trim(), y);
            }
            Map<String, Object> n = new LinkedHashMap<>();
            n.put("id", row.get("id"));
            n.put("name", row.get("name"));
            n.put("type", row.get("type"));
            n.put("x", x);
            n.put("y", y);
            n.put("assignee", row.get("assignee"));
            String policy = Convert.toStr(row.get("policy"), "");
            n.put("policy", StrUtil.isBlank(policy) ? "单人办理" : policy);
            n.put("right", "可编辑");
            nodes.add(n);
        }
        return nodes;
    }

    private List<Map<String, Object>> edgeGraph(Long defId) {
        List<Map<String, Object>> edges = workflowCenterMapper.selectSkips(defId);
        return edges == null ? Collections.emptyList() : edges;
    }

    private List<String> passedNodes(Map<String, Object> inst) {
        if (inst == null || !"completed".equals(inst.get("status"))) {
            return Collections.emptyList();
        }
        Map<String, Object> def = definitionDetail(Convert.toStr(inst.get("defId")));
        Object nodes = def.get("nodes");
        List<String> passed = new ArrayList<>();
        if (nodes instanceof List) {
            for (Object item : (List<?>) nodes) {
                if (item instanceof Map) {
                    passed.add(Convert.toStr(((Map<?, ?>) item).get("id")));
                }
            }
        }
        return passed;
    }

    private List<List<String>> parseForm(String variable) {
        List<List<String>> form = new ArrayList<>();
        if (StrUtil.isBlank(variable)) {
            return form;
        }
        String text = variable.trim();
        if (text.startsWith("{") && text.endsWith("}")) {
            text = text.substring(1, text.length() - 1);
        }
        for (String part : text.split("[,;]")) {
            String[] kv = part.split("[:=]", 2);
            if (kv.length == 2) {
                List<String> item = new ArrayList<>();
                item.add(unquote(kv[0]));
                item.add(unquote(kv[1]));
                form.add(item);
            }
        }
        return form;
    }

    private String unquote(String s) {
        String v = s.trim();
        if (v.length() >= 2 && ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'")))) {
            return v.substring(1, v.length() - 1);
        }
        return v;
    }

    private int timePercent(String arrived) {
        return StrUtil.isBlank(arrived) ? 0 : 50;
    }

    private int nodeType(String type) {
        if ("start".equals(type)) return 0;
        if ("end".equals(type)) return 2;
        if ("gateway".equals(type)) return 3;
        if ("parallel".equals(type)) return 4;
        return 1;
    }

    private Map<String, Object> queryParam(Map<String, Object> param) {
        Map<String, Object> p = new HashMap<>();
        if (param != null) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                if (!"pageNum".equals(entry.getKey()) && !"pageSize".equals(entry.getKey())) {
                    p.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return p;
    }

    private Page<Map<String, Object>> page(Map<String, Object> param) {
        int pageNum = Math.max(1, Convert.toInt(param == null ? null : param.get("pageNum"), 1));
        int pageSize = Math.min(1000, Math.max(1, Convert.toInt(param == null ? null : param.get("pageSize"), 10)));
        return new Page<>(pageNum, pageSize);
    }

    private Long longOf(Object value) {
        try {
            return Convert.toLong(value);
        } catch (Exception e) {
            return 0L;
        }
    }

    private Long parseId(String value, String prefix) {
        if (StrUtil.isBlank(value)) {
            return 0L;
        }
        String v = value.trim();
        if (v.startsWith(prefix)) {
            v = v.substring(prefix.length());
        }
        int dash = v.lastIndexOf('-');
        if (dash >= 0) {
            v = v.substring(dash + 1);
        }
        try {
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castList(Object value) {
        if (value instanceof List) {
            return (List<Map<String, Object>>) value;
        }
        return Collections.emptyList();
    }
}
