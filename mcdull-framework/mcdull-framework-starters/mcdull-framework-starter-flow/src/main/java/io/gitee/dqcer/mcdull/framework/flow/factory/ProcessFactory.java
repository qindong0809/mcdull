package io.gitee.dqcer.mcdull.framework.flow.factory;

import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.properties.ProcessBean;

import java.util.List;

public interface ProcessFactory {

    /**
     * 获取一个流程
     * @param id    业务唯一标识
     * @return
     */
    List<ProcessHandler> getProcessNodes(String id);


    /**
     * 获取一个流程
     * @param processBean  process
     * @return  获取process
     */
    List<ProcessHandler> getProcessNodes(ProcessBean processBean);


}
