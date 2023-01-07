package com.dqcer.mcdull.admin.flow.registry;

import java.util.Collection;
import com.dqcer.mcdull.admin.flow.properties.Process;

public interface ProcessDefinitionReader {

    /**
     * 加载process列表
     * @return
     */
    Collection<Process> loadProcessDefinition();
}
