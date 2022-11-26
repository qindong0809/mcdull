package com.dqcer.framework.base.dict;

import java.lang.annotation.*;

/**
 * 翻译（由key翻译为value）
 *
 * @author dqcer
 * @version 2022/10/05
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Transform {

    /**
     * 数据源，可用于静态枚举
     *
     * @return {@link Class}<{@link ?}>
     */
    Class<?> dataSource() default Void.class;

    /**
     * id或者code的值来源于哪个字段
     */
    String from() default "";

    /**
     * 数据源 dict类型
     *
     * @return boolean
     */
    boolean dataSourceDict() default true;

    /**
     * 额外参数
     *
     * @return {@link String}
     */
    String param() default "";


    /**
     * 针对从数据库
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link Transformer}>
     */
    Class<? extends Transformer> transformer() default Transformer.class;
}
