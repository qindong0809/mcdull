package io.gitee.dqcer.mcdull.framework.doc;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;

import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义枚举类文档
 *
 * @author dqcer
 * @since 2024/04/24
 */
@Component
public class SchemaEnumPropertyCustomizer implements PropertyCustomizer {

    @Override
    public Schema customize(Schema schema, AnnotatedType type) {
        if (type.getCtxAnnotations() == null) {
            return schema;
        }

        StringBuilder description = new StringBuilder();
        for (Annotation ctxAnnotation : type.getCtxAnnotations()) {
            if (ctxAnnotation.annotationType().equals(SchemaEnum.class)) {
                description.append(((SchemaEnum) ctxAnnotation).desc());
                Class<? extends Enum> clazz = ((SchemaEnum) ctxAnnotation).value();
                Enum<?>[] enumConstants = clazz.getEnumConstants();
                List<KeyValueVO<Object, Object>> list = new ArrayList<>();
                for (Enum<?> enumConstant : enumConstants) {
                    Object code = getEnumCode(enumConstant);
                    Object text = getEnumText(enumConstant);
                    list.add(new KeyValueVO<>(code, text));
                }
                JSONArray objects = JSONUtil.parseArray(list);
                description.append(objects);
            }
        }
        if (!description.isEmpty()) {
            schema.setDescription(description.toString());
        }
        return schema;
    }

    /**
     * 获取枚举的code值
     * 优先尝试调用getCode()方法，如果不存在则使用name()
     */
    private Object getEnumCode(Enum<?> enumConstant) {
        try {
            Method getCodeMethod = enumConstant.getClass().getMethod("getCode");
            return getCodeMethod.invoke(enumConstant);
        } catch (Exception e) {
            // 如果没有getCode方法，使用name作为code
            return enumConstant.name();
        }
    }

    /**
     * 获取枚举的text值
     * 优先尝试调用getText()方法，如果不存在则使用name()
     */
    private Object getEnumText(Enum<?> enumConstant) {
        try {
            Method getTextMethod = enumConstant.getClass().getMethod("getText");
            return getTextMethod.invoke(enumConstant);
        } catch (Exception e) {
            // 如果没有getText方法，使用name作为text
            return enumConstant.name();
        }
    }

}
