package io.gitee.dqcer.mcdull.framework.flow.factory;

import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.properties.ProcessBean;
import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认处理
 *
 * @author dqcer
 * @since 2023/01/08 17:01:57
 */
@Component
public class DefaultProcessFactory implements ProcessFactory, ProcessDefinitionRegistry {

    private final Map<String,List<ProcessHandler>> processes = new ConcurrentHashMap<>();


    private String buildKey( String id) {
        return id  + "#";
    }

    /**
     * 获取一个流程
     *
     * @param id   业务码
     * @return
     */
    @Override
    public List<ProcessHandler> getProcessNodes(String id) {
        String key = buildKey(id);
        return processes.get(key);
    }

    /**
     * 获取一个流程
     *
     * @param processBean process
     * @return 获取process
     */
    @Override
    public List<ProcessHandler> getProcessNodes(ProcessBean processBean) {
        return processes.get(getProcessNodes(processBean.getId()));
    }

    /**
     * 注册一个流程
     *
     * @param id   业务代码
     * @param processHandlerList  该流程下面的所有结点
     */
    @Override
    public void registryProcess(String id, List<ProcessHandler> processHandlerList) {
        String key = buildKey(id);
        List<ProcessHandler> processHandlers = processes.get(key);
        if (ObjUtil.isNotNull(processHandlers)) {
            throw new IllegalArgumentException("重复注册");
        }
        processes.put(key, processHandlerList);
    }

    @Override
    public void registryProcess(ProcessBean processBean, List<ProcessHandler> processHandlers) {
        registryProcess(processBean.getId(), processHandlers);
    }
}
