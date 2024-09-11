package io.gitee.dqcer.mcdull.framework.flow.registry;

import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.flow.factory.ProcessDefinitionRegistry;
import io.gitee.dqcer.mcdull.framework.flow.load.ProcessDefinitionReader;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.properties.ProcessBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 流程注册
 *
 * @author dqcer
 * @since 2023/01/08 16:01:12
 */
public class ProcessFlowRegistry {

    private static final Logger log = LoggerFactory.getLogger(ProcessFlowRegistry.class);

    private NodeRegistry nodeRegistry;

    private ProcessDefinitionRegistry processDefinitionRegistry;

    private ProcessDefinitionReader processDefinitionReader;

    public ProcessFlowRegistry(NodeRegistry nodeRegistry, ProcessDefinitionRegistry processDefinitionRegistry, ProcessDefinitionReader processDefinitionReader) {
        this.nodeRegistry = nodeRegistry;
        this.processDefinitionRegistry = processDefinitionRegistry;
        this.processDefinitionReader = processDefinitionReader;
    }


    @PostConstruct
    public void run() {
        Collection<ProcessBean> processBeans = processDefinitionReader.loadProcessDefinition();
        if (ObjUtil.isNull(processBeans)) {
            log.error("Load process error");
            return;
        }
        for (ProcessBean processBean : processBeans) {
            List<ProcessHandler> processProcessHandlers = new ArrayList<>();
            List<String> nodeList = processBean.getNodeList();
            if (ObjUtil.isNull(nodeList)) {
                continue;
            }
            for (String nodeName : nodeList) {
                ProcessHandler processHandler = nodeRegistry.getNode(nodeName);
                if (processHandler != null) {
                    processProcessHandlers.add(processHandler);
                }
            }
            processDefinitionRegistry.registryProcess(processBean, processProcessHandlers);
        }
    }
}
