package io.gitee.dqcer.mcdull.framework.swagger;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.SchemaEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
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
                Class<? extends IEnum<?>> clazz = ((SchemaEnum) ctxAnnotation).value();
                IEnum<?>[] enumConstants = clazz.getEnumConstants();
                List<KeyValueVO<Object, Object>> list = new ArrayList<>();
                for (IEnum<?> iEnum : enumConstants) {
                    list.add(new KeyValueVO<>(iEnum.getCode(), iEnum.getText()));
                }
                JSONArray objects = JSONUtil.parseArray(list);
                description.append(objects);
            }
        }
        if (description.length() > 0) {
            schema.setDescription(description.toString());
        }
        return schema;
    }

}
