package io.gitee.dqcer.mcdull.admin.util;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;

import java.util.concurrent.ConcurrentHashMap;

public class LogHelpUtil {

    public static void setLog(String log) {
        UnifySession session = UserContextHolder.getSession();
        ConcurrentHashMap<String, Object> extension = session.getExtension();
        extension.put("log", log);
        session.setExtension(extension);
    }

    public static String getLog() {
        UnifySession session = UserContextHolder.getSession();
        return Convert.toStr(session.getExtension().get("log"));
    }
}
