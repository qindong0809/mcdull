package com.dqcer.mcdull.admin.flow.factory;

import com.dqcer.mcdull.admin.flow.node.Node;
import com.dqcer.mcdull.admin.flow.properties.Process;

import java.util.List;

public interface ProcessFactory {

    /**
     * 获取一个流程
     * @param id    业务唯一标识
     * @return
     */
    List<Node> getProcessNodes(String id);


    /**
     * 获取一个流程
     * @param process  process
     * @return  获取process
     */
    List<Node> getProcessNodes(Process process);


}
