package io.gitee.dqcer.framework.base.annotation;


import io.gitee.dqcer.framework.base.validator.EnumsIntValidator;

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
@Constraint(validatedBy = EnumsIntValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumsIntValid {

    String message() default "值不在枚举值中";

    Class<?>[] groups() default {};

    Class<? extends Enum> value();

    Class<? extends Payload>[] payload() default {};
}
