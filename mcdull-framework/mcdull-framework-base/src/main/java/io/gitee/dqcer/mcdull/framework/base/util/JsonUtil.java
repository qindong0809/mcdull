package io.gitee.dqcer.mcdull.framework.base.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * json 工具类
 *
 * @author dqcer
 * @date 2022/12/19
 */
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper mapper;

    static {
        JsonFactory jf = new JsonFactory();
        // 忽略注释
        jf.enable(JsonParser.Feature.ALLOW_COMMENTS);
        mapper = new ObjectMapper(jf);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        // 如果存在未知属性，则忽略不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许key没有双引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许key有单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * 对象转为json字符串
     *
     * @param obj 待转换对象
     * @param <T> 待转换对象类型
     * @return json格式字符串
     */
    public static <T> String toJsonString(T obj) {
        return toJsonString(obj, false);
    }

    /**
     * 对象转为json字符串
     *
     * @param obj    待转换对象
     * @param format 是否格式化
     * @param <T>    待转换对象类型
     * @return json格式字符串
     */
    public static <T> String toJsonString(T obj, boolean format) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return format ? mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj) : mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonException(" Parse Object to String error ", e);
        }
    }

    /**
     * 转换json字符串为json对象
     *
     * @param text  待转换文本
     * @param clazz 转换为对象类型
     * @param <T>   换对象类型
     * @return 对象
     */
    public static <T> T parse(String text, Class<T> clazz) {
        if (StrUtil.isBlank(text) || clazz == null) {
            return null;
        }
        if (clazz.equals(String.class)) {
            return (T) text;
        }
        try {
            T obj = mapper.readValue(text, clazz);
            return obj;
        } catch (IOException e) {
            throw new JsonException(" Parse String to Object error ", e);
        }
    }

    /**
     * 转换json字符串为对象集合
     *
     * @param text  待转换文本
     * @param clazz 转换为对象类型
     * @param <T>   转换对象类型
     * @return 对象集合
     */
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StrUtil.isBlank(text) || clazz == null) {
            return null;
        }
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            List<T> list = mapper.readValue(text, javaType);

            return list;
        } catch (IOException e) {
            throw new JsonException(" Parse String to Array error ", e);
        }
    }

    public static <T> T deserialize(String json, Class<T> valueType) {
        if (StrUtil.isNotBlank(json) && valueType != null) {
            try {
                return mapper.readValue(json, valueType);
            } catch (Exception e) {
                log.error("JacksonSerializer deserialize error={}", ThrowableUtil.getStackTraceAsString(e));
                throw new JsonException(json);
            }
        } else {
            return null;
        }
    }

    public static <T> List<T> deserializeList(InputStream inputStream, TypeReference typeReference) {
        if (inputStream == null) {
            return null;
        } else {
            try {
                return (List)mapper.readValue(inputStream, typeReference);
            } catch (Exception e) {
                log.error("JacksonSerializer deserializeList error={}", ThrowableUtil.getStackTraceAsString(e));
                throw new JsonException("deserialize list from inputStream error, errorType = " + typeReference.getType().getTypeName() + ", inputStream = " + inputStream, e);
            }
        }
    }

    /**
     * json异常
     */
    public static class JsonException extends RuntimeException {
        public JsonException() {
            super();
        }

        public JsonException(String message) {
            super(message);
        }

        public JsonException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
