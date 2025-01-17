package io.gitee.dqcer.mcdull.framework.base.validator;

import cn.hutool.core.util.ReflectUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

/**
 * 枚举字符串验证器
 *
 * @author dqcer
 * @since 2022/01/11
 */
public class EnumsStrValidator implements ConstraintValidator<EnumsStrValid, String> {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private Class<?> enumClass;

    private static final String METHOD_NAME = "getByCode";

    private boolean required;

    @Override
    public void initialize(EnumsStrValid annotation) {
        enumClass = annotation.value();
        required = annotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Method declareMethod;
        if (required) {
            declareMethod = ReflectUtil.getMethodByName(enumClass, METHOD_NAME);
            try {
                Object invoke = declareMethod.invoke(null, enumClass, value);
                if (invoke == null) {
                    return false;
                }
            } catch (Exception e) {
                LogHelp.error(log, "Invoke error.", e);
                return false;
            }
        }
        return true;
    }
}
