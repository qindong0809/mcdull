package com.dqcer.mcdull.web.log;

import ch.qos.logback.core.rolling.RollingFileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;

/**
 * 日志事件监听器
 *
 * @author dqcer
 * @date 2022/10/24
 */
public class LoggingEventListener extends RollingFileAppender {

    private static final Logger log = LoggerFactory.getLogger(LoggingEventListener.class);

    @Override
    protected void subAppend(Object event) {
        super.subAppend(event);

        if (event instanceof LoggingEvent) {
            LoggingEvent loggingEvent = (LoggingEvent) event;
            if (loggingEvent.getLevel().toInt() == Level.ERROR.toInt()) {
                log.info("error log listener: {}", loggingEvent.getMessage());
            }
            if (loggingEvent.getLevel().toInt() == Level.WARN.toInt()) {
                log.info("Warn log listener: {}", loggingEvent.getMessage());
            }
            // 此处可以进行异常第三方的推送提醒
        }
    }


}

