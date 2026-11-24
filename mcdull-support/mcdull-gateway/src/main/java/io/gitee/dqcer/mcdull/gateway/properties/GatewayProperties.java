package io.gitee.dqcer.mcdull.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关免鉴权配置，对应 mcdull.gateway.no-auth。
 */
@ConfigurationProperties(prefix = "mcdull.gateway")
public class GatewayProperties {

    private List<String> noAuth = new ArrayList<>();

    public List<String> getNoAuth() {
        return noAuth;
    }

    public void setNoAuth(List<String> noAuth) {
        this.noAuth = noAuth;
    }
}
