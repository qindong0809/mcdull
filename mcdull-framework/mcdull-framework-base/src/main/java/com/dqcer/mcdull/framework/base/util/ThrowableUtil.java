package com.dqcer.mcdull.framework.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常 工具类
 *
 * @author dqcer
 * @date 2023/01/07 17:01:28
 */
public class ThrowableUtil {

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
