package io.gitee.dqcer.mcdull.framework.web.json.serialize;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;

import java.io.IOException;
import java.util.Date;

/**
 * 动态日期序列化程序
 *
 * @author dqcer
 * @since 2024/06/24
 */
public class DynamicDateSerialize extends DateSerializer implements ContextualSerializer {

    private final JsonFormat jsonFormat;

    public DynamicDateSerialize(JsonFormat jsonFormat) {
        this.jsonFormat = jsonFormat;
    }

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (date == null) {
            jsonGenerator.writeNull();
            return;
        }
        if (jsonFormat != null) {
            String pattern = jsonFormat.pattern();
            if (StrUtil.isBlank(pattern)) {
                pattern = DatePattern.NORM_DATE_PATTERN;
            }
            jsonGenerator.writeString(TimeZoneUtil.serializeDate(date, pattern));
        }
    }

    @Override
    public DateSerializer createContextual(SerializerProvider serializerProvider,
                                           BeanProperty beanProperty) {
        JsonFormat annotation = beanProperty.getAnnotation(JsonFormat.class);
        return new DynamicDateSerialize(annotation);
    }
}
