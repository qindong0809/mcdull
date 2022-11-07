package com.dqcer.mcdull.framework.web.transform;

import com.dqcer.framework.base.dict.IDict;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 翻译切面处理
 *
 * @author dqcer
 * @date 2022/10/05
 */
@Aspect
@Order(2)
public class TranslatorAspect {

    private static final Logger log = LoggerFactory.getLogger(TranslatorAspect.class);


    @Pointcut("@annotation(com.dqcer.mcdull.framework.web.transform.Transform) ")
    public void translatorPointCut() {
        // 切点
    }

    @AfterReturning(pointcut = "translatorPointCut()",  returning = "object")
    public void doAfter(JoinPoint joinPoint, Object object) {
        log.info("翻译切面处理 object: {}", object);
        doTranslateObject(object);
    }

    private void doTranslateObject(Object result) {
        if (null == result) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(result.getClass());
        for (Field field : fields) {
            if (null == field.getAnnotation(Transform.class)) {
                continue;
            }
            doTransform(result, field);
        }
        // 嵌套处理
        for (Field field : fields) {
            if (null == field.getAnnotation(Transform.class)) {
                continue;
            }
            doTransform(result, field);
            Object subObject = ReflectUtil.invokeGet(result, field);
            //  集合处理
            if (subObject instanceof Collection<?>) {
                Collection<?> collection = (Collection<?>) subObject;
                for (Object subResult : collection) {
                    doTranslateObject(subResult);
                }
                continue;
            }
            //  单个处理
            doTranslateObject(subObject);
        }
    }

    private void doTransform(Object result, Field field) {
        Transform annotation = field.getAnnotation(Transform.class);
        Class<?> aClass = annotation.dataSource();
        Object filedValue = ReflectUtil.invokeGet(result, annotation.from());
        if (null == filedValue) {
            return;
        }
        if (IDict.class.isAssignableFrom(aClass)) {
            String translate = new EnumTransformer().transform(filedValue, aClass, null);
            if (null != translate) {
                ReflectUtil.invokeSet(result, field, translate);
            }
            return;
        }

        Transformer<Object> transformer = SpringContextUtil.getBean(annotation.transformer());
        String translate = transformer.transform(filedValue, annotation.dataSource(), annotation.param());
        if (null != translate) {
            ReflectUtil.invokeSet(result, field, translate);
        }
    }
}
