package com.dqcer.mcdull.framework.web.listener;

import com.dqcer.mcdull.framework.web.feign.model.LogDTO;
import com.dqcer.mcdull.framework.web.feign.service.LogFeignClient;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志事件监听器
 *
 * @author dqcer
 * @date 2021/08/19
 */
public class LogEventListener {


    @Async("threadPoolTaskExecutor")
    @Order
    @EventListener(LogDTO.class)
    public void listenLog(LogDTO sysLog) {
        List<LogDTO> dtos = new ArrayList<>();
        dtos.add(sysLog);
    }

}