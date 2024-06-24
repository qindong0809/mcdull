package io.gitee.dqcer.mcdull.framework.web.json.deserialize;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

/**
 * 动态日期反序列化
 *
 * @author dqcer
 * @since 2024/06/24
 */
public class DynamicDateDeserialize extends DateDeserializers.DateDeserializer implements ContextualDeserializer  {

    private JsonFormat jsonFormat;

    public DynamicDateDeserialize(JsonFormat jsonFormat) {
        this.jsonFormat = jsonFormat;
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt, Date intoValue) throws IOException {
        Date date = null;
        long valueAsLong = p.getValueAsLong();
        if (valueAsLong != 0) {
            date = new Date(valueAsLong);
        }
        if (ObjectUtil.isNull(date)) {
            if (jsonFormat != null) {
                String pattern = jsonFormat.pattern();
                String dateStr = p.getValueAsString();
                date = DateUtil.parse(dateStr, pattern);
            }
        }
        if (ObjectUtil.isNull(date)) {
            date = super.deserialize(p, ctxt, intoValue);
        }
        return DateUtil.convertTimeZone(date, ZoneId.of("UTC"));
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        JsonFormat annotation = property.getAnnotation(JsonFormat.class);
        return new DynamicDateDeserialize(annotation);
    }
}
