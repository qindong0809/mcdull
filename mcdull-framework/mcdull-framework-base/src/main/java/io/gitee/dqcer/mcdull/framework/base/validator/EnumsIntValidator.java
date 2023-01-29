package io.gitee.dqcer.mcdull.framework.base.validator;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

/**
 * 枚举字符串验证器
 *
 * @author dqcer
 * @since 2022/01/11
 */
public class EnumsIntValidator implements ConstraintValidator<EnumsIntValid, Integer> {


    private Class<? extends Enum> enumClass;

    private static final String METHOD_NAME = "toEnum";

    @Override
    public void initialize(EnumsIntValid annotation) {
        enumClass = annotation.value();
        try {
            enumClass.getDeclaredMethod(METHOD_NAME, Integer.class);
        } catch (NoSuchMethodException e){
            throw new IllegalArgumentException("the enum class has not toEnum() method", e);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        Method declareMethod;
        try {
            declareMethod = enumClass.getDeclaredMethod(METHOD_NAME, Integer.class);
        }catch (NoSuchMethodException e){
            return false;
        }
        try {
            declareMethod.invoke(null, value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
