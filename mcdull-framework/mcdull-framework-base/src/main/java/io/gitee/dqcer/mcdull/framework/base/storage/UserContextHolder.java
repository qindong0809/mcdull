package io.gitee.dqcer.mcdull.framework.base.storage;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;

import java.util.Date;
import java.util.Locale;

/**
 * 用户上下文
 *
 * @author dqcer
 * @since 2021/08/19
 */
public class UserContextHolder {

    static InheritableThreadLocal<UnifySession> UNIFY_SESSION = new InheritableThreadLocal<>();


    public static void setDefaultSession() {
        UnifySession session = new UnifySession();
        session.setTraceId(RandomUtil.uuid());
        session.setNow(new Date());
        session.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        session.setLanguage(Locale.SIMPLIFIED_CHINESE.getLanguage());
        session.setZoneIdStr("Asia/Shanghai");
        session.setTenantId(0);
        session.setLoginName("system");
        session.setUserId("0");
        UserContextHolder.setSession(session);
    }

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

    /**
     * 是否为管理人员
     *
     * @return boolean
     */
    public static boolean isAdmin() {
        UnifySession session = UNIFY_SESSION.get();
        return session != null && Boolean.TRUE.equals(session.getAdministratorFlag());
    }

    /**
     * 当前用户id
     *
     * @return {@link Long}
     */
    public static Object currentUserId() {
        UnifySession session = UNIFY_SESSION.get();
        return session != null ? session.getUserId() : null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T currentUserId(Class<T> tClass) {
        if (tClass == Long.class) {
            return (T) userIdLong();
        }
        if (tClass == Integer.class) {
            return (T) userId();
        }
        return (T) userIdStr();
    }

    public static Long userIdLong() {
        UnifySession session = UNIFY_SESSION.get();
        return session != null ? Convert.toLong(session.getUserId()) : null;
    }

    public static Integer userId() {
        UnifySession session = UNIFY_SESSION.get();
        return session != null ? Convert.toInt(session.getUserId()) : null;
    }

    public static String userIdStr() {
        UnifySession session = UNIFY_SESSION.get();
        return session != null ? Convert.toStr(session.getUserId()) : null;
    }

    public static String print() {
        UnifySession session = getSession();
        if (ObjUtil.isNotNull(session)) {
            return StrUtil.format("url: {}. userId: {}", session.getRequestUrl(), session.getUserId());
        }
        return StrUtil.EMPTY;
    }
}
