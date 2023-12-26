package io.gitee.dqcer.mcdull.framework.base.annotation;

import java.lang.annotation.*;

/**
 * 翻译（由key翻译为value）
 *
 * @author dqcer
 * @since 2022/10/05
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Transform {

    /**
     * 数据源，可用于静态枚举
     *
     * @return Class&lt;?&gt;
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
//    Enum<? extends IEnum> param() default IEnum;

    String param() default "";


    /**
     * 针对从数据库
     *
     * @return Class&lt;? extends ITransformer&gt;
     */
    Class<? extends ITransformer> transformer() default ITransformer.class;
}
