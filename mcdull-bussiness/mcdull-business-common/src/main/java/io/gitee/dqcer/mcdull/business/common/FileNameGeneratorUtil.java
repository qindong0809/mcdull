package io.gitee.dqcer.mcdull.business.common;

import cn.hutool.core.date.DateUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;

public final class FileNameGeneratorUtil {
    private static final String EXCEL = ".xlsx";

    public static String simple(String name) {
        String formatted = DateUtil.formatDateTime(UserContextHolder.getSession().getNow());
        final String format = "%s-%s" + EXCEL;
        return String.format(format, name, formatted);
    }
}
