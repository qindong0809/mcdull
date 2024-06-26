package io.gitee.dqcer.mcdull.framework.mysql;

import io.gitee.dqcer.mcdull.framework.mysql.config.MysqlAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用动态数据源
 *
 * @author dqcer
 * @since 2021/08/21 19:08:69
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import({MysqlAutoConfiguration.class})
@SuppressWarnings("unused")
public @interface EnableDynamicDataSource {
}
