package io.gitee.dqcer.mcdull.framework.web.json.serialize;

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

    private JsonFormat jsonFormat;

    private DynamicDateFormat dynamicDateFormat;

    /**
     * 默认构造方法, 以便序列化器能被Jackson正确识别
     */
    @SuppressWarnings("unused")
    public DynamicDateSerialize() {}

    public DynamicDateSerialize(JsonFormat jsonFormat, DynamicDateFormat dynamicDateFormat) {
        this.jsonFormat = jsonFormat;
        this.dynamicDateFormat = dynamicDateFormat;
    }

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        if (date == null) {
            jsonGenerator.writeNull();
            return;
        }
        String pattern = dynamicDateFormat.dateFormat();
        String result = TimeZoneUtil.serializeDate(date, pattern, null, null, null);
        jsonGenerator.writeString(result);
    }

    @Override
    public DateSerializer createContextual(SerializerProvider serializerProvider,
                                           BeanProperty beanProperty) {
        JsonFormat jsonFormat = beanProperty.getAnnotation(JsonFormat.class);
        DynamicDateFormat dynamicDateFormat = beanProperty.getAnnotation(DynamicDateFormat.class);
        return new DynamicDateSerialize(jsonFormat, dynamicDateFormat);
    }
}
