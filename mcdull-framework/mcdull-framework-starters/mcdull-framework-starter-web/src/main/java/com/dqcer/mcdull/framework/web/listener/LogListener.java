package com.dqcer.mcdull.framework.web.listener;

import com.alibaba.fastjson.JSONObject;
import com.dqcer.mcdull.framework.web.event.LogEvent;
import com.dqcer.mcdull.framework.web.remote.RemoteLogService;
import com.dqcer.mcdull.mdc.api.dto.SysLogFeignDTO;
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
        com.dqcer.mcdull.framework.web.remote.LogDTO sysLog = (com.dqcer.mcdull.framework.web.remote.LogDTO) event.getSource();
        if (log.isDebugEnabled()) {
            log.debug("Log listener: {}", sysLog);
        }
        List<SysLogFeignDTO> dtos = new ArrayList<>();
        SysLogFeignDTO sysLogDTO = JSONObject.parseObject(JSONObject.toJSONString(sysLog), SysLogFeignDTO.class);
        dtos.add(sysLogDTO);
        logService.batchSave(dtos);
    }

}