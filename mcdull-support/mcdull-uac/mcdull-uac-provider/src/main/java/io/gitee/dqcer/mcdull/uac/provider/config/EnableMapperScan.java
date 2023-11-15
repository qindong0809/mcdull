package io.gitee.dqcer.mcdull.uac.provider.config;

import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mapper 配置
 *
 * @author dqcer
 * @since 2022/07/25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MapperScan
@EnableDynamicDataSource
public @interface EnableMapperScan {

    @SuppressWarnings("unused")
    String[] basePackages() default {"io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper"};
}
