package io.gitee.dqcer.mcdull.framework.web.config;

import io.gitee.dqcer.mcdull.framework.web.properties.OssProperties;
import io.gitee.dqcer.mcdull.framework.web.properties.ThreadPoolProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 注册 starter-web 自有的配置属性。
 */
@AutoConfiguration
@EnableConfigurationProperties({ThreadPoolProperties.class, OssProperties.class})
public class WebPropertiesAutoConfiguration {
}
