package com.dqcer.mcdull.framework.web.transform;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 反射工具类
 *
 * @author peng.xy
 * @date 2022/4/14
 */
public class ReflectUtil {

    private static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);


    private static final String GET = "get";

    private static final String SET = "set";

    private ReflectUtil() {
    }

    /**
     * 调用set方法
     *
     * @param obj：对象
     * @param field：字段
     * @param param：参数
     * @author peng.xy
     * @date 2022/4/14
     */
    public static void invokeSet(Object obj, Field field, Object param) {
        invokeSet(obj, field.getName(), param);
    }

    /**
     * 调用set方法
     *
     * @param obj：对象
     * @param fieldName：字段名
     * @param param：参数
     * @author peng.xy
     * @date 2022/4/14
     */
    public static void invokeSet(Object obj, String fieldName, Object param) {
        String setMethodName = SET.concat(fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1)));
        Method setMethod = getMethod(obj.getClass(), setMethodName, param.getClass());
        try {
            setMethod.invoke(obj, param);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("invoke set exception", e);
            throw new RuntimeException("invoke set exception");
        }
    }


    /**
     * 调用get方法
     *
     * @param obj：对象
     * @param field：字段
     * @author peng.xy
     * @date 2022/4/14
     */
    public static Object invokeGet(Object obj, Field field) {
        return invokeGet(obj, field.getName());
    }

    /**
     * 调用get方法
     *
     * @param obj：对象
     * @param fieldName：字段名
     * @author peng.xy
     * @date 2022/4/14
     */
    public static Object invokeGet(Object obj, String fieldName) {
        String getMethodName = GET.concat(fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1)));
        Method getMethod = getMethod(obj.getClass(), getMethodName);
        try {
            return getMethod.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("invoke get exception", e);
            throw new RuntimeException("invoke get exception");
        }
    }

    /**
     * 获取自身包括父类的字段列表
     *
     * @param classType：类型
     * @return List<Field>
     * @author peng.xy
     */
    public static List<Field> getFieldList(Class<?> classType) {
        if (classType != null) {
            List<Field> fieldList = new LinkedList<>();
            Field[] fields = classType.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        fieldList.add(field);
                    }
                }
            }
            Class<?> superClass = classType.getSuperclass();
            if (superClass != null && !superClass.equals(Object.class)) {
                List<Field> superFieldList = getFieldList(superClass);
                if (superFieldList != null && !superFieldList.isEmpty()) {
                    fieldList.addAll(superFieldList);
                }
            }
            return fieldList;
        }
        return Collections.emptyList();
    }

    /**
     * 获取自身包括父类的字段数组
     *
     * @param classType：类型
     * @return Field[]
     * @author peng.xy
     */
    public static Field[] getFields(Class<?> classType) {
        List<Field> fieldList = getFieldList(classType);
        return fieldList.toArray(new Field[fieldList.size()]);
    }

    /**
     * 传入对象类型、方法，参数类型，获取方法
     *
     * @param classType：对象类型
     * @param methodName：方法名
     * @param parameterTypes：参数类型
     * @return fieldSetMethod：字段set方法
     * @author peng.xy
     */
    public static Method getMethod(Class<?> classType, String methodName, Class<?>... parameterTypes) {
        try {
            return classType.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            Class<?> superclass = classType.getSuperclass();
            if (superclass != null) {
                return getMethod(superclass, methodName, parameterTypes);
            }
        }
        log.error("no such method by set");
        throw new RuntimeException("no such method by set");
    }

}
