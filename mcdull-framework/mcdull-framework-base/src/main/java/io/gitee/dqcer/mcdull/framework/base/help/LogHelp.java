package io.gitee.dqcer.mcdull.framework.base.help;

import cn.hutool.core.lang.Console;
import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * 日志帮助
 *
 * @author dqcer
 * @since 2024/03/28
 */
@SuppressWarnings("unused")
public class LogHelp {


    public static void debug(Logger logger, String format, Supplier<?>... suppliers) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, getAll(suppliers));
        }
    }


    public static void debug(Logger logger, Supplier<String> supplier) {
        if (logger.isDebugEnabled()) {
            logger.debug(supplier.get());
        }
    }

    public static void info(Logger logger, String format, Supplier<?>... suppliers) {
        if (logger.isInfoEnabled()) {
            logger.info(format, getAll(suppliers));
        }
    }

    public static void info(Logger logger, Supplier<String> supplier) {
        if (logger.isInfoEnabled()) {
            logger.info(supplier.get());
        }
    }


    public static void error(Logger logger, String format, Supplier<?>... suppliers) {
        if (logger.isErrorEnabled()) {
            logger.error(format, getAll(suppliers));
        }
    }


    public static void error(Logger logger, Supplier<String> supplier) {
        if (logger.isErrorEnabled()) {
            logger.error(supplier.get());
        }
    }


    public static void warn(Logger logger, String format, Supplier<?>... suppliers) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, getAll(suppliers));
        }
    }


    public static void warn(Logger logger, Supplier<String> supplier) {
        if (logger.isWarnEnabled()) {
            logger.warn(supplier.get());
        }
    }

    private static Object[] getAll(final Supplier<?>... suppliers) {
        if (suppliers == null) {
            return null;
        }
        final Object[] result = new Object[suppliers.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = suppliers[i].get();
        }
        return result;
    }


    public static void debug(Logger logger, String format, Object... objects) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, objects);
        }
    }

    public static void debug(Logger logger, String info) {
        if (logger.isDebugEnabled()) {
            logger.debug(info);
        }
    }

    public static void info(Logger logger, String format, Object... objects) {
        if (logger.isInfoEnabled()) {
            logger.info(format, objects);
        }
    }


    public static void info(Logger logger, String info) {
        if (logger.isInfoEnabled()) {
            logger.info(info);
        }
    }

    public static void error(Logger logger, String format, Object... objects) {
        if (logger.isErrorEnabled()) {
            logger.error(format, objects);
        }
    }


    public static void error(Logger logger, String info) {
        if (logger.isErrorEnabled()) {
            logger.error(info);
        }
    }


    public static void warn(Logger logger, String format, Object... objects) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, objects);
        }
    }


    public static void warn(Logger logger, String info) {
        if (logger.isWarnEnabled()) {
            logger.warn(info);
        }
    }
}
