package io.gitee.dqcer.mcdull.framework.tag.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mcdull.tag")
public class TagProperties {

    /**
     * 是否开启全链路标签路由（默认开启）
     */
    private boolean enabled = true;

    /**
     * 严格模式：为 true 时，若找不到对应标签的实例，不再降级走无标签公共实例（适合生产强隔离灰度）
     */
    private boolean strict = false;

    /**
     * 是否在网关入口清洗外部客户端传入的 X-Env-Tag 请求头（建议在 prod 环境设为 true）
     */
    private boolean cleanHeader = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isStrict() {
        return strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public boolean isCleanHeader() {
        return cleanHeader;
    }

    public void setCleanHeader(boolean cleanHeader) {
        this.cleanHeader = cleanHeader;
    }
}
