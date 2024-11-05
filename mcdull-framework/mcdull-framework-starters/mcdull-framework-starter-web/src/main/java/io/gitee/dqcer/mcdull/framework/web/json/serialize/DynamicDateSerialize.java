package io.gitee.dqcer.mcdull.framework.web.json.serialize;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

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
        if (jsonFormat != null) {
            String dynamicPattern = jsonFormat.pattern();
            if (StrUtil.isNotBlank(dynamicPattern)) {
                pattern = dynamicPattern;
            }
        }
        boolean timezoneStyle = false;
        String zoneIdStr = "UTC";
        Locale locale = serializerProvider.getLocale();
        UnifySession<?> unifySession = UserContextHolder.getSession();
        if (unifySession != null) {
            String dateFormat = unifySession.getDateFormat();
            if (StrUtil.isNotBlank(dateFormat)) {
                pattern = dateFormat;
            }
            String sessionZoneIdStr = unifySession.getZoneIdStr();
            if (StrUtil.isNotBlank(sessionZoneIdStr)) {
                zoneIdStr = sessionZoneIdStr;
            }
            if (dynamicDateFormat != null) {
                boolean enableTimezone = dynamicDateFormat.enableTimezone();
                if (!enableTimezone) {
                    zoneIdStr = null;
                }
                timezoneStyle = dynamicDateFormat.appendTimezoneStyle();
            } else {
                zoneIdStr = null;
            }
            if (ObjectUtil.isNotNull(unifySession.getLocale())) {
                locale = unifySession.getLocale();
            }
        }
        String result = TimeZoneUtil.serializeDate(date, pattern, locale, zoneIdStr, timezoneStyle);
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
