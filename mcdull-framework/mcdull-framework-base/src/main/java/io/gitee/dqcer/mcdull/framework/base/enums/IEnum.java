package io.gitee.dqcer.mcdull.framework.base.enums;

import io.gitee.dqcer.mcdull.framework.base.vo.EnumVO;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通用枚举基类
 *
 * @author dqcer
 * @version 2022/10/04
 */
public interface IEnum<T>  extends Serializable {


    /**
     * 通过code获取value
     *
     * @param clazz 枚举class
     * @param code  code
     * @return text
     */
    static <T> String getTextByCode(Class<? extends IEnum<T>> clazz, T code) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IEnum<T> e) -> e.getCode().equals(code))
                .map(IEnum::getText)
                .findAny().orElse(null);
    }

    /**
     * 通过text获取code
     *
     * @param clazz 枚举class
     * @param text  text
     * @return code
     */
    static <T> T getCodeByText(Class<? extends IEnum<T>> clazz, String text) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IEnum<T> e) -> e.getText().equals(text))
                .map(IEnum::getCode)
                .findAny().orElse(null);
    }

    /**
     * 通过code获取字典枚举实例
     *
     * @param clazz 枚举class
     * @param code  code
     * @param <T>   字典code类型
     * @param <R>   枚举类型
     * @return 字典枚举实例
     */
    @SuppressWarnings("unchecked")
    static <T, R extends IEnum<T>> R getByCode(Class<? extends IEnum<T>> clazz, T code) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IEnum<T> e) -> (e.getCode().equals(code)))
                .map(v -> (R) v)
                .findAny()
                .orElse(null);
    }

    /**
     * 获取给定的字典枚举项（常用下拉框数据请求）
     *
     * @param enums 可指定需要哪些项
     * @return List
     */
    @SafeVarargs
    static <T, E extends IEnum<T>> List<IEnum<T>> getItems(E... enums) {
        return Stream.of(enums)
                .map(DictPool::getDict)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有字典枚举项，除开指定的枚举
     *
     * @param exclude 指定排除的枚举
     * @return List
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    static <T, E extends IEnum<T>> List<IEnum<T>> getItemsExclude(E... exclude) {
        Class<IEnum<T>> clazz = (Class<IEnum<T>>) exclude.getClass().getComponentType();
        IEnum<T>[] allEnum = clazz.getEnumConstants();
        List<IEnum<T>> excludeList = Arrays.asList(exclude);
        return Stream.of(allEnum)
                .filter(e -> !excludeList.contains(e))
                .map(DictPool::getDict)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有字典枚举项（常用下拉框数据请求）
     * 枚举值上标记@Deprecated的不会返回
     *
     * @param clazz 字典枚举类
     * @return List
     */
    static <T> List<IEnum<T>> getAll(Class<? extends IEnum<T>> clazz) {
        Map<String, Field> fieldCache = Arrays.stream(clazz.getDeclaredFields()).
                filter(Field::isEnumConstant).
                collect(Collectors.toMap(Field::getName, Function.identity()));
        IEnum<T>[] allEnum = clazz.getEnumConstants();
        return Stream.of(allEnum)
                .filter(e -> !fieldCache.get(((Enum<?>) e).name()).isAnnotationPresent(Deprecated.class))
                .map(DictPool::getDict)
                .collect(Collectors.toList());
    }

    /**
     * 获取 code集合
     *
     * @param clazz clazz 字典枚举类
     * @return {@link List}<{@link T}>
     */
    static <T> List<T> getCodes(Class<? extends IEnum<T>> clazz) {
        List<IEnum<T>> all = getAll(clazz);
        return all.stream().map(IEnum::getCode).collect(Collectors.toList());
    }

    /**
     * 初始化
     *
     * @param code 字典编码
     * @param text 字典文本
     */
    default void init(T code, String text) {
        DictPool.putDict(this, code, text);
    }

    /**
     * 获取编码
     *
     * @return 编码
     */
    default T getCode() {
        return DictPool.getDict(this).getCode();
    }

    /**
     * 获取文本
     *
     * @return 文本
     */
    default String getText() {
        return DictPool.getDict(this).getText();
    }


    @SuppressWarnings("all")
    class DictPool {
        private static final Map<IEnum, EnumVO> DICT_MAP = new ConcurrentHashMap<>();

        private static final Map<String, Class<? extends IEnum>> DICT_NAME_CLASS_MAP = new ConcurrentHashMap<>();

        static <T> void putDict(IEnum<T> dict, T code, String text) {
            DICT_NAME_CLASS_MAP.put(dict.getClass().getName(), dict.getClass());
            DICT_MAP.put(dict, new EnumVO<>(code, text));
        }

        public static List<IEnum<Object>> getDict(String dictName) {
            Class<? extends IEnum> aClass = DICT_NAME_CLASS_MAP.get(dictName);
            return IEnum.getAll((Class<? extends IEnum<Object>>) aClass);
        }

        static <K extends IEnum<T>, T> EnumVO<T> getDict(K dict) {
            return DICT_MAP.get(dict);
        }


    }
}
