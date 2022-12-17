package com.dqcer.mcdull.framework.web.event;

import com.dqcer.mcdull.framework.web.feign.model.LogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 *
 * @author dqcer
 * @version 2021/09/13
 */
public class LogEvent extends ApplicationEvent {

    /**
     * 日志事件
     *
     * @param source 源
     */
    public LogEvent(LogDTO source) {
        super(source);
    }
}
