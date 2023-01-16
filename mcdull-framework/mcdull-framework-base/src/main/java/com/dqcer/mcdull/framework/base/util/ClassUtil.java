package com.dqcer.mcdull.framework.base.util;

import java.lang.annotation.Annotation;

/**
 * class 工具类
 *
 * @author dqcer
 * @version 2023/01/07 19:01:24
 */
public class ClassUtil {

    /**
     * 判断当前class是否有对应的注解
     *
     * @param clazz          clazz
     * @param annotationType 注解类型
     * @return 对应的注解
     */
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        if (annotationType == null) {
            return null;
        }
        A annotation = clazz.getDeclaredAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && superclass != Object.class) {
            return findAnnotation(superclass, annotationType);
        }
        return null;
    }
}
