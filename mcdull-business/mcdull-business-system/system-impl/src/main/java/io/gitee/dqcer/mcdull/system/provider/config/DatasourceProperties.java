package io.gitee.dqcer.mcdull.system.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mcdull.security.datasource")
public class DatasourceProperties {
    private String aesKey;
}
