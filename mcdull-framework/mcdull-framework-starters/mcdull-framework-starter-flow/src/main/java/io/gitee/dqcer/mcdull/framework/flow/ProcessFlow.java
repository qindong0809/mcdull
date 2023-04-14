package io.gitee.dqcer.mcdull.framework.flow;

import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.flow.factory.ProcessFactory;
import io.gitee.dqcer.mcdull.framework.flow.node.Context;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工作流
 *
 * @author dqcer
 * @since 2023/01/07 20:01:01
 */
@Component
public class ProcessFlow {


    @Resource
    private ProcessFactory processFactory;

    public void run(Context context) {
        List<ProcessHandler> processHandlers = processFactory.getProcessNodes(context.getId());
        if (ObjUtil.isNull(processHandlers)) {
            throw new IllegalArgumentException("节点不存在");
        }
        for (ProcessHandler processHandler : processHandlers) {
            processHandler.execute(context);
            boolean stopFlow = processHandler.stopFlow();
            if (stopFlow) {
                break;
            }
        }
    }
}
