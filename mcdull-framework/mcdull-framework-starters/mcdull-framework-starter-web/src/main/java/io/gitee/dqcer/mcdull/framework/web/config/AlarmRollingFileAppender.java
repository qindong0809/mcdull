package io.gitee.dqcer.mcdull.framework.web.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 报警滚动文件目的地
 *
 * @author dqcer
 * @since 2022/10/26
 */
public class AlarmRollingFileAppender extends RollingFileAppender {

    private static final Logger log = LoggerFactory.getLogger(AlarmRollingFileAppender.class);

    @Override
    protected void subAppend(Object event) {
        super.subAppend(event);

        if (event instanceof LoggingEvent) {
            LoggingEvent loggingEvent = (LoggingEvent) event;
            if (loggingEvent.getLevel().toInt() == Level.ERROR.toInt()) {
                log.info("错误日志监听: {}", loggingEvent.getMessage());
            }
            if (loggingEvent.getLevel().toInt() == Level.WARN.toInt()) {
                log.info("警告日志监听: {}", loggingEvent.getMessage());
            }
            // 此处可以进行异常第三方的推送提醒
        }
    }


    /**
     * 子模块可继承后自定义title
     *
     * @return {@link String}
     */
    protected String title() {
        return "";
    }

}

