package com.dqcer.mcdull.framework.base.annotation;


import com.dqcer.mcdull.framework.base.validator.EnumsStrValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 状态效验注解
 *
 * @author dqcer
 * @version 2021/12/20
 */
@Documented
@Constraint(validatedBy = EnumsStrValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumsStrValid {

    String message() default "值不在枚举值中";

    Class<?>[] groups() default {};

    Class<? extends Enum> value();

    Class<? extends Payload>[] payload() default {};
}
