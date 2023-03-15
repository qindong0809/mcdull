package io.gitee.dqcer.mcdull.framework.flow.factory;

import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.properties.ProcessBean;

import java.util.List;

/**
 * 流程定义注册表
 *
 * @author dqcer
 * @since 2023/01/08 16:01:21
 */
public interface ProcessDefinitionRegistry {


    /**
     * 注册一个流程
     *
     * @param bizCode   业务代码
     * @param processHandlerList  该流程下面的所有结点
     */
    void registryProcess(String bizCode, List<ProcessHandler> processHandlerList);

    /**
     * 注册一个流程
     *
     * @param processBean     processBean
     * @param processHandlers 该流程下面的所有结点
     */
    void registryProcess(ProcessBean processBean, List<ProcessHandler> processHandlers);
}
