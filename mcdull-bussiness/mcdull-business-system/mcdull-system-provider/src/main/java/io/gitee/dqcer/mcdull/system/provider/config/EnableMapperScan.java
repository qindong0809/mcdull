package io.gitee.dqcer.mcdull.system.provider.config;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;

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
//@MapperScan
//@EnableDynamicDataSource
public @interface EnableMapperScan {

    @SuppressWarnings("unused")
    String[] basePackages() default {GlobalConstant.BASE_PACKAGE + ".**.mapper"};
//    String[] basePackages() default {GlobalConstant.BASE_PACKAGE + ".system.provider.web.dao.mapper"};
}
