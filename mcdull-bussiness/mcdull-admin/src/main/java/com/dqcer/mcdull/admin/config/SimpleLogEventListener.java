package com.dqcer.mcdull.admin.config;

import com.dqcer.mcdull.framework.web.feign.model.LogDTO;
import com.dqcer.mcdull.framework.web.listener.LogEventListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleLogEventListener extends LogEventListener {

    @Override
    public void listenLog(LogDTO sysLog) {
        System.out.println(sysLog);
        super.listenLog(sysLog);
    }
}
