package io.gitee.dqcer.mcdull.framework.base.storage;

/**
 * 用户上下文
 *
 * @author dqcer
 * @since 2021/08/19
 */
public class UserContextHolder {

    /**
     * 统一的会话
     */
    static InheritableThreadLocal<UnifySession> UNIFY_SESSION = new InheritableThreadLocal<>();

    /**
     * 获取会话
     *
     * @return {@link UnifySession}
     */
    public static UnifySession getSession() {
        return UNIFY_SESSION.get();
    }

    /**
     * 设置会话
     *
     * @param box 盒子
     */
    public static void setSession(UnifySession box) {
        UNIFY_SESSION.set(box);
    }

    /**
     * 清除会话
     */
    public static void clearSession() {
        UNIFY_SESSION.remove();
    }

}
