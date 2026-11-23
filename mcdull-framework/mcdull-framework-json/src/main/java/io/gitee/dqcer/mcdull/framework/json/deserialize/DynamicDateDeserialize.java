package io.gitee.dqcer.mcdull.framework.json.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import io.gitee.dqcer.mcdull.framework.json.serialize.DynamicDateFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态日期反序列化，与 {@link io.gitee.dqcer.mcdull.framework.json.serialize.DynamicDateSerialize} 成对使用。
 *
 * <p>动态序列化会把 Date 写成 {@code yyyy-MM-dd HH:mm:ss}（可能带 {@code (+08:00)} 后缀）等字符串，
 * Jackson 默认的 Date 反序列化器只接受 ISO-8601 或时间戳，缓存回读时会抛出
 * {@code InvalidFormatException}。这里按字段上的 {@link DynamicDateFormat} 还原字符串，
 * 并兼容 ISO-8601、时间戳等历史缓存数据。</p>
 *
 * @author dqcer
 * @since 2024/06/24
 */
public class DynamicDateDeserialize extends DateDeserializers.DateDeserializer implements ContextualDeserializer {

    private static final Pattern TIMEZONE_SUFFIX = Pattern.compile("\\((Z|[+-]\\d{2}:?\\d{2})\\)$");

    private static final String[] COMMON_PATTERNS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd"
    };

    private final String dynamicPattern;

    public DynamicDateDeserialize() {
        this(null);
    }

    public DynamicDateDeserialize(String dynamicPattern) {
        this.dynamicPattern = dynamicPattern;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        DynamicDateFormat format = property == null ? null : property.getAnnotation(DynamicDateFormat.class);
        if (format == null) {
            return super.createContextual(ctxt, property);
        }
        String pattern = format.dateFormat();
        if (format.showTime() && !containsHour(pattern)) {
            pattern = pattern + " HH:mm:ss";
        }
        return new DynamicDateDeserialize(pattern);
    }

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        if (parser.currentToken() == JsonToken.VALUE_STRING) {
            Date date = parseDynamic(parser.getText().trim());
            if (date != null) {
                return date;
            }
        }
        return super.deserialize(parser, ctxt);
    }

    private Date parseDynamic(String text) {
        String value = text;
        TimeZone timeZone = TimeZone.getDefault();
        Matcher matcher = TIMEZONE_SUFFIX.matcher(value);
        if (matcher.find()) {
            timeZone = toTimeZone(matcher.group(1));
            value = value.substring(0, matcher.start()).trim();
        }
        if (dynamicPattern != null) {
            Date date = tryParse(value, dynamicPattern, timeZone);
            if (date != null) {
                return date;
            }
        }
        for (String pattern : COMMON_PATTERNS) {
            Date date = tryParse(value, pattern, timeZone);
            if (date != null) {
                return date;
            }
        }
        return null;
    }

    private Date tryParse(String value, String pattern, TimeZone timeZone) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.ROOT);
        format.setLenient(false);
        format.setTimeZone(timeZone);
        ParsePosition position = new ParsePosition(0);
        Date date = format.parse(value, position);
        return date != null && position.getIndex() == value.length() ? date : null;
    }

    private TimeZone toTimeZone(String offset) {
        if ("Z".equalsIgnoreCase(offset)) {
            return TimeZone.getTimeZone("UTC");
        }
        return TimeZone.getTimeZone("GMT" + offset);
    }

    private boolean containsHour(String pattern) {
        return pattern.indexOf('H') >= 0 || pattern.indexOf('h') >= 0
                || pattern.indexOf('k') >= 0 || pattern.indexOf('K') >= 0;
    }
}
