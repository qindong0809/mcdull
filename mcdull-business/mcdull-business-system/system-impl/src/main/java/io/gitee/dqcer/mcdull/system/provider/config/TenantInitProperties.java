package io.gitee.dqcer.mcdull.system.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mcdull.tenant")
public class TenantInitProperties {
    private String initScriptDir = "sql/init";
    private long taskTtlSeconds = 86400L;
}
