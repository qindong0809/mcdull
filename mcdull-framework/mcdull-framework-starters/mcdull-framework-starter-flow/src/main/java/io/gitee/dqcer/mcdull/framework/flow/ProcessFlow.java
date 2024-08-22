package io.gitee.dqcer.mcdull.framework.flow;

import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.flow.factory.ProcessFactory;
import io.gitee.dqcer.mcdull.framework.flow.node.Context;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Transactional(rollbackFor = Exception.class)
    public void run(Context context) {
        List<ProcessHandler> processHandlers = processFactory.getProcessNodes(context.getId());
        if (ObjUtil.isNull(processHandlers)) {
            throw new IllegalArgumentException("节点不存在");
        }
        for (int i = 0; i < processHandlers.size(); i++) {
            ProcessHandler processHandler = processHandlers.get(i);
            Exception ex = null;
            try {
                processHandler.execute(context);
            } catch (Exception e) {
                if (i != 0 && processHandler.extendParentNodeTryAttr()) {
                    processHandlers.get(i - 1).catchException(e, context);
                } else {
                    processHandler.catchException(e, context);
                }
                ex = e;
                throw e;
            } finally {
                if (i != 0 && processHandler.extendParentNodeTryAttr()) {
                    processHandlers.get(i - 1).finallyException(ex, context);
                } else {
                    processHandler.finallyException(ex, context);
                }
            }
            boolean endNode = processHandler.endNode();
            if (endNode) {
                break;
            }
        }
    }
}
