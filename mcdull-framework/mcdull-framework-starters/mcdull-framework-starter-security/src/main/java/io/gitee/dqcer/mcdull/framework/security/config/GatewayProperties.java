package io.gitee.dqcer.mcdull.framework.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关免鉴权配置，对应 mcdull.gateway.no-auth。
 */
@ConfigurationProperties(prefix = "mcdull.gateway")
public class GatewayProperties {

    private List<String> noAuth = new ArrayList<>();

    /**
     * 是否校验网关下发的 Same-Token。
     * <p>
     * 本地直连子服务调试时可关闭；生产环境必须保持开启，确保外部请求都经过网关。
     */
    private boolean sameTokenEnabled = true;

    public List<String> getNoAuth() {
        return noAuth;
    }

    public void setNoAuth(List<String> noAuth) {
        this.noAuth = noAuth;
    }

    public boolean isSameTokenEnabled() {
        return sameTokenEnabled;
    }

    public void setSameTokenEnabled(boolean sameTokenEnabled) {
        this.sameTokenEnabled = sameTokenEnabled;
    }
}
