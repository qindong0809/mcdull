package com.dqcer.mcdull.admin.flow.registry;

import com.dqcer.mcdull.admin.flow.node.Node;
import com.dqcer.mcdull.admin.flow.properties.Process;

import java.util.List;

public interface ProcessDefinitionRegistry {


    /**
     * 注册一个流程
     *
     * @param bizCode   业务代码
     * @param nodeList  该流程下面的所有结点
     */
    void registryProcess(String bizCode, List<Node> nodeList);

    void registryProcess(Process process, List<Node> nodes);
}
