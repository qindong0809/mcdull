package io.gitee.dqcer.mcdull.framework.flow.record;


import io.gitee.dqcer.mcdull.framework.flow.node.Context;


/**
 * 入参 出参 上下文管理抽象类
 *
 * @author dqcer
 * @date 2023/01/08 16:01:11
 */
public class AbstractContext implements Context {

    /**
     * 业务流程唯一标识
     */
    protected  String id;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
