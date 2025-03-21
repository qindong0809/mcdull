package io.gitee.dqcer.mcdull.admin.config;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

import java.lang.annotation.*;

/**
 * 操作日志
 *
 * @author dqcer
 * @since 2023/01/15 12:01:64
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 所属模块
     *
     * @return {@link String}
     */
    String module();

    /**
     * 所属菜单
     *
     * @return {@link String}
     */
    String menu();

    /**
     * 操作类型
     *
     * @return {@link Class<? extends IEnum<?>>}
     */
    OperationTypeEnum type();
}
