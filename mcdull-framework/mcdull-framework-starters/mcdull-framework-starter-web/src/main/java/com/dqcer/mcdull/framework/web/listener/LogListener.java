package com.dqcer.mcdull.framework.web.listener;

import com.dqcer.mcdull.framework.web.event.LogEvent;
import com.dqcer.mcdull.framework.web.remote.LogDTO;
import com.dqcer.mcdull.framework.web.remote.RemoteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dqcer
 * @description 异步监听日志事件
 * @date 2021/08/19
 */
public class LogListener {

    private static final Logger log = LoggerFactory.getLogger(LogListener.class);

    private final RemoteLogService logService;


    public LogListener(RemoteLogService logService) {
        this.logService = logService;
    }

    @Async
    @Order
    @EventListener(LogEvent.class)
    public void listenLog(LogEvent event) {
        LogDTO sysLog = (LogDTO) event.getSource();
        if (log.isDebugEnabled()) {
            log.debug("Log listener: {}", sysLog);
        }
        List<LogDTO> dtos = new ArrayList<>();
        dtos.add(sysLog);
        logService.batchSave(dtos);
    }

}