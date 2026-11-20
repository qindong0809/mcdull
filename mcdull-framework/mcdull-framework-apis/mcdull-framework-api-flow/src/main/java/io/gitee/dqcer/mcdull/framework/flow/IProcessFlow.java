package io.gitee.dqcer.mcdull.framework.flow;

import io.gitee.dqcer.mcdull.framework.flow.node.Context;

/**
 * 流程执行器契约。
 */
public interface IProcessFlow {

    /**
     * 执行流程。
     *
     * @param context 流程上下文
     */
    void run(Context context);
}
