package io.gitee.dqcer.mcdull.framework.base.annotation;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举类字段属性的 自定义 doc 注解
 *
 * @author dqcer
 * @since 2024/04/24
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SchemaEnum {

    /**
     * 枚举类对象
     *
     */
    Class<? extends IEnum<?>> value();

    String example() default "";

    boolean hidden() default false;

    boolean required() default true;

    String dataType() default "";

    String desc() default "";

}
