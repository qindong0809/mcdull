package io.gitee.dqcer.mcdull.framework.base.validator;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    protected Logger log = LoggerFactory.getLogger(getClass());

    private Class<?> enumClass;

    private static final String METHOD_NAME = "toEnum";

    private boolean required;

    @Override
    public void initialize(EnumsIntValid annotation) {
        enumClass = annotation.value();
        required = annotation.required();
        try {
            enumClass.getDeclaredMethod(METHOD_NAME, Integer.class);
        } catch (NoSuchMethodException e){
            throw new IllegalArgumentException("the enum class has not toEnum() method", e);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        Method declareMethod;
        if (required) {
            try {
                declareMethod = enumClass.getDeclaredMethod(METHOD_NAME, Integer.class);
            }catch (NoSuchMethodException e){
                return false;
            }
            try {
                declareMethod.invoke(null, value);
            } catch (Exception e) {
                LogHelp.error(log, "Invoke error.", e);
                return false;
            }
        }
        return true;
    }

}
