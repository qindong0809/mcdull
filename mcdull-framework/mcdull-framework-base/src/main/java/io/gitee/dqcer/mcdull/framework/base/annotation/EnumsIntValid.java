package io.gitee.dqcer.mcdull.framework.base.annotation;


import io.gitee.dqcer.mcdull.framework.base.validator.EnumsIntValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 状态效验注解
 *
 * @author dqcer
 * @since 2021/12/20
 */
@Documented
@Constraint(validatedBy = EnumsIntValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumsIntValid {

    String message() default "值不在枚举值中";

    boolean required() default false;

    Class<?>[] groups() default {};

    Class<? extends Enum> value();

    Class<? extends Payload>[] payload() default {};
}
