package io.gitee.dqcer.framework.flow.registry;

import io.gitee.dqcer.framework.flow.node.ProcessHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 节点注册
 *
 * @author dqcer
 * @version 2023/01/08 16:01:74
 */
@Component
public class NodeRegistry {

    private final Map<String, ProcessHandler> nodeNameToNodeMap = new ConcurrentHashMap<>();

    public <Context> void register(ProcessHandler<Context> processHandler) {
        ProcessHandler existedProcessHandler = nodeNameToNodeMap.get(processHandler.getNodeCode());
        if (existedProcessHandler != null) {
            throw new IllegalArgumentException("节点名称已存在");
        }
        nodeNameToNodeMap.put(processHandler.getNodeCode(), processHandler);
    }

    public void clear() {
        this.nodeNameToNodeMap.clear();
    }


    public ProcessHandler getNode(String nodeName) {
        return this.nodeNameToNodeMap.get(nodeName);
    }
}
