package com.dqcer.mcdull.admin.flow.registry;

import com.dqcer.framework.base.util.ObjUtil;
import com.dqcer.mcdull.admin.flow.properties.Process;
import com.dqcer.mcdull.admin.flow.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProcessFlowRegistry implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProcessFlowRegistry.class);

    private NodeRegistry nodeRegistry;

    private ProcessDefinitionRegistry processDefinitionRegistry;

    private ProcessDefinitionReader processDefinitionReader;

    public ProcessFlowRegistry(NodeRegistry nodeRegistry, ProcessDefinitionRegistry processDefinitionRegistry, ProcessDefinitionReader processDefinitionReader) {
        this.nodeRegistry = nodeRegistry;
        this.processDefinitionRegistry = processDefinitionRegistry;
        this.processDefinitionReader = processDefinitionReader;
    }


    @Override
    public void run(String... args) throws Exception {
        Collection<Process> processes = processDefinitionReader.loadProcessDefinition();
        if (ObjUtil.isNull(processes)) {
            log.error("Load process error");
            return;
        }
        List<Node> processNodes = new ArrayList<>();
        for (Process process : processes) {
            List<String> nodeList = process.getNodeList();
            if (ObjUtil.isNull(nodeList)) {
                continue;
            }
            for (String nodeName : nodeList) {
                Node node = nodeRegistry.getNode(nodeName);
                if (node != null) {
                    processNodes.add(node);
                }
            }
            processDefinitionRegistry.registryProcess(process, processNodes);
        }
    }
}
