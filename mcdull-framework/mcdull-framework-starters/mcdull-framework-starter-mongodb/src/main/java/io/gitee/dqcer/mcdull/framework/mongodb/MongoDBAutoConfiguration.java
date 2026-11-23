package io.gitee.dqcer.mcdull.framework.mongodb;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * mongodb 自动配置。
 *
 * @author dqcer
 * @since 1.0.0
 */
@AutoConfiguration
@Import(MongoDBTemplate.class)
public class MongoDBAutoConfiguration {
}
