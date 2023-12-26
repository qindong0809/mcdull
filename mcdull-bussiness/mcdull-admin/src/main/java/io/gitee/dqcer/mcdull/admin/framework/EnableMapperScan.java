package io.gitee.dqcer.mcdull.admin.framework;

import io.gitee.dqcer.mcdull.framework.mysql.EnableDynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;

import java.lang.annotation.*;

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
    String[] basePackages() default {"io.gitee.dqcer.mcdull.admin.web.dao.mapper"};
}
