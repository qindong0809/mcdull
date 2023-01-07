package com.dqcer.mcdull.admin.flow.factory;

import com.dqcer.framework.base.util.ObjUtil;
import com.dqcer.mcdull.admin.flow.properties.Process;
import com.dqcer.mcdull.admin.flow.node.Node;
import com.dqcer.mcdull.admin.flow.registry.ProcessDefinitionRegistry;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DefaultProcessFactory implements ProcessFactory, ProcessDefinitionRegistry {

    private final Map<String,List<Node>> processes = Maps.newConcurrentMap();


    private String buildKey( String id) {
        return id +
                "#" +
                "operation";
    }

    /**
     * 获取一个流程
     *
     * @param id   业务码
     * @return
     */
    @Override
    public List<Node> getProcessNodes(String id) {
        String key = buildKey(id);
        return processes.get(key);
    }

    /**
     * 获取一个流程
     *
     * @param process process
     * @return 获取process
     */
    @Override
    public List<Node> getProcessNodes(Process process) {
        return processes.get(getProcessNodes(process.getId()));
    }

    /**
     * 注册一个流程
     *
     * @param id   业务代码
     * @param nodeList  该流程下面的所有结点
     */
    @Override
    public void registryProcess(String id, List<Node> nodeList) {
        String key = buildKey(id);
        List<Node> nodes = processes.get(key);
        if (ObjUtil.isNotNull(nodes)) {
            throw new IllegalArgumentException("重复注册");
        }
        processes.put(key, nodeList);
    }

    @Override
    public void registryProcess(Process process, List<Node> nodes) {
        registryProcess(process.getId(), nodes);
    }
}
