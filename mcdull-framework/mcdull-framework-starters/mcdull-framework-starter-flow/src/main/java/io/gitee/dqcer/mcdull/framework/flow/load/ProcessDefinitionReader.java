package io.gitee.dqcer.mcdull.framework.flow.load;

import io.gitee.dqcer.mcdull.framework.flow.properties.ProcessBean;

import java.util.Collection;

/**
 * 流程定义
 *
 * @author dqcer
 * @since 2023/01/08 16:01:02
 */
public interface ProcessDefinitionReader {

    /**
     * 加载流程定义
     *
     * @return {@link Collection<  ProcessBean  >}
     */
    Collection<ProcessBean> loadProcessDefinition();
}
