package io.gitee.dqcer.mcdull.framework.base.annotation;


import io.gitee.dqcer.mcdull.framework.base.validator.EnumsStrValidator;
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
@Constraint(validatedBy = EnumsStrValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumsStrValid {

    String message() default "{valid.enum.not.exist}";

    boolean required() default true;

    Class<?>[] groups() default {};

    Class<? extends Enum> value();

    Class<? extends Payload>[] payload() default {};
}
