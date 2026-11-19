package io.gitee.dqcer.mcdull.framework.web.context;

import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.spi.AsyncContextPropagator;

/**
 * 登录态上下文的跨线程传播器。
 * <p>
 * 捕获的是同一个 {@link UnifySession} 引用而非副本：会话在一次请求内基本只读，拷贝会让
 * 提交线程后续的会话变更看不到。若将来 {@code UnifySession} 出现会被异步线程写入的可变属性，
 * 这里需要改为 {@code session.copy()}。
 *
 * @author dqcer
 * @since 2026/11/19
 */
public class UserSessionContextPropagator implements AsyncContextPropagator {

    @Override
    public Object capture() {
        return UserContextHolder.getSession();
    }

    @Override
    public void apply(Object snapshot) {
        if (snapshot == null) {
            UserContextHolder.clearSession();
            return;
        }
        UserContextHolder.setSession((UnifySession) snapshot);
    }
}
