package io.gitee.dqcer.mcdull.framework.flow.node;

/**
 * 入参 出参 上下文管理抽象类
 *
 * @author dqcer
 * @since 2023/01/18 22:01:75
 */
public interface Context {

    /**
     * 业务流程唯一标识
     *
     * @return {@link String}
     */
    String getId();
}
