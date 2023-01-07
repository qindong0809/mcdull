package com.dqcer.mcdull.admin.flow;

import com.dqcer.framework.base.util.ObjUtil;
import com.dqcer.mcdull.admin.flow.factory.ProcessFactory;
import com.dqcer.mcdull.admin.flow.node.Context;
import com.dqcer.mcdull.admin.flow.node.Node;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工作流
 *
 * @author dqcer
 * @date 2023/01/07 20:01:01
 */
@Component
public class ProcessFlow {

    @Resource
    private ProcessFactory processFactory;

    public void run(Context context) {
        List<Node> nodes = processFactory.getProcessNodes(context.getId());
        if (ObjUtil.isNull(nodes)) {
            throw new IllegalArgumentException("节点不存在");
        }
        for (Node node : nodes) {
            node.execute(context);
        }
    }
}
