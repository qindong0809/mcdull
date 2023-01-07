package com.dqcer.mcdull.admin.flow.registry;

import com.dqcer.mcdull.admin.flow.node.Node;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NodeRegistry {

    private final Map<String, Node> nodeNameToNodeMap = new ConcurrentHashMap<>();

    public <Context> void register(Node<Context> node) {
        Node existedNode = nodeNameToNodeMap.get(node.getNodeCode());
        if (existedNode != null) {
            throw new IllegalArgumentException("节点名称已存在");
        }
        nodeNameToNodeMap.put(node.getNodeCode(), node);
    }

    public void clear() {
        this.nodeNameToNodeMap.clear();
    }


    public Node getNode(String nodeName) {
        return this.nodeNameToNodeMap.get(nodeName);
    }
}
