package io.gitee.dqcer.mcdull.framework.base.annotation;


import io.gitee.dqcer.mcdull.framework.base.enums.DataPermissionsEnum;

import java.lang.annotation.*;

/**
 * 授权
 *
 * @author dqcer
 * @since 2022/07/26
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorized {

    /**
     * 模块权限效验: sys:user:list
     *
     * @return {@link String}
     */
    String value() default "";


    /**
     * 数据权限效验: 角色
     *
     * @return {@link DataPermissionsEnum[]}
     */
    DataPermissionsEnum[] data() default {DataPermissionsEnum.ROLE};

}
