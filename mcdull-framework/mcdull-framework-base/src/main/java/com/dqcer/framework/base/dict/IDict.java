package com.dqcer.framework.base.dict;

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
 * 字典接口，实现类为枚举类
 *
 * @author dqcer
 * @date 2022/10/04
 */
@SuppressWarnings("unused")
public interface IDict<T>  extends Serializable {


    /**
     * 通过code获取value
     *
     * @param clazz 枚举class
     * @param code  code
     * @return text
     */
    static <T> String getTextByCode(Class<? extends IDict<T>> clazz, T code) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IDict<T> e) -> e.getCode().equals(code))
                .map(IDict::getText)
                .findAny().orElse(null);
    }

    /**
     * 通过text获取code
     *
     * @param clazz 枚举class
     * @param text  text
     * @return code
     */
    static <T> T getCodeByText(Class<? extends IDict<T>> clazz, String text) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IDict<T> e) -> e.getText().equals(text))
                .map(IDict::getCode)
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
    static <T, R extends IDict<T>> R getByCode(Class<? extends IDict<T>> clazz, T code) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IDict<T> e) -> (e.getCode().equals(code)))
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
    static <T, E extends IDict<T>> List<IDict<T>> getItems(E... enums) {
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
    static <T, E extends IDict<T>> List<IDict<T>> getItemsExclude(E... exclude) {
        Class<IDict<T>> clazz = (Class<IDict<T>>) exclude.getClass().getComponentType();
        IDict<T>[] allEnum = clazz.getEnumConstants();
        List<IDict<T>> excludeList = Arrays.asList(exclude);
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
    static <T> List<IDict<T>> getAll(Class<? extends IDict<T>> clazz) {
        Map<String, Field> fieldCache = Arrays.stream(clazz.getDeclaredFields()).
                filter(Field::isEnumConstant).
                collect(Collectors.toMap(Field::getName, Function.identity()));
        IDict<T>[] allEnum = clazz.getEnumConstants();
        return Stream.of(allEnum)
                .filter(e -> !fieldCache.get(((Enum<?>) e).name()).isAnnotationPresent(Deprecated.class))
                .map(DictPool::getDict)
                .collect(Collectors.toList());
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
        private static final Map<IDict, DictBean> DICT_MAP = new ConcurrentHashMap<>();

        private static final Map<String, Class<? extends IDict>> DICT_NAME_CLASS_MAP = new ConcurrentHashMap<>();

        static <T> void putDict(IDict<T> dict, T code, String text) {
            DICT_NAME_CLASS_MAP.put(dict.getClass().getName(), dict.getClass());
            DICT_MAP.put(dict, new DictBean<>(code, text));
        }

        public static List<IDict<Object>> getDict(String dictName) {
            Class<? extends IDict> aClass = DICT_NAME_CLASS_MAP.get(dictName);
            return IDict.getAll((Class<? extends IDict<Object>>) aClass);
        }

        static <K extends IDict<T>, T> DictBean<T> getDict(K dict) {
            return DICT_MAP.get(dict);
        }


    }
}
