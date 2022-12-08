package com.dqcer.mcdull.framework.config.properties;

import com.dqcer.framework.base.constants.GlobalConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = GlobalConstant.ROOT_PREFIX)
public class McdullProperties {

    /**
     * 网关
     */
    private GatewayProperties gateway = new GatewayProperties();

    public GatewayProperties getGateway() {
        return gateway;
    }

    public void setGateway(GatewayProperties gateway) {
        this.gateway = gateway;
    }
}
