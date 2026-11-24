package io.gitee.dqcer.mcdull.framework.oss;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * OSS 自动配置。
 *
 * <p>统一通过 {@code AutoConfiguration.imports} 装配，不再依赖业务侧组件扫描。
 *
 * @author dqcer
 * @since 1.0.0
 */
@AutoConfiguration
@Import(OssServiceImpl.class)
public class OssAutoConfiguration {
}
