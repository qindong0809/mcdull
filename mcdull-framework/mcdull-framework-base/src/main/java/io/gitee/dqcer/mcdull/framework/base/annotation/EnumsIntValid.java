package io.gitee.dqcer.mcdull.framework.base.annotation;


import io.gitee.dqcer.mcdull.framework.base.validator.EnumsIntValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
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

    String message() default "{valid.enum.not.exist}";

    boolean required() default true;

    Class<?>[] groups() default {};

    Class<? extends Enum> value();

    Class<? extends Payload>[] payload() default {};
}
