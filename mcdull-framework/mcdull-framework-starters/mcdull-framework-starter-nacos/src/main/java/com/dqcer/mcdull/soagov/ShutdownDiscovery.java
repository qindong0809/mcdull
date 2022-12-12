package com.dqcer.mcdull.soagov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 监听关闭事件
 *
 * @author dqcer
 * @date 2022/12/12
 */
@Component
public class ShutdownDiscovery implements ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ShutdownDiscovery.class);

    @Resource
    private AbstractAutoServiceRegistration autoServiceRegistration;

    /**
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("Nacos 注销补偿机制...");
        // TODO: 2022/12/12
        try {
            autoServiceRegistration.destroy();
        } catch (Exception e) {
            throw e;
        }
    }
}
