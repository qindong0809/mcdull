package com.dqcer.mcdull.framework.web.aspect;

import com.dqcer.framework.base.annotation.ITransformer;
import com.dqcer.framework.base.annotation.Transform;
import com.dqcer.framework.base.enums.IEnum;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.framework.web.transform.EnumITransformer;
import com.dqcer.mcdull.framework.web.transform.ReflectUtil;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 翻译切面处理
 *
 * @author dqcer
 * @version 2022/10/05
 */
@Aspect
@Order(2)
public class TranslatorAspect {

    private static final Logger log = LoggerFactory.getLogger(TranslatorAspect.class);

    @Resource
    private ITransformer transform;

    @Pointcut("@annotation(com.dqcer.framework.base.annotation.Transform) ")
    public void translatorPointCut() {
        // 切点
    }

    @AfterReturning(pointcut = "translatorPointCut()",  returning = "object")
    public void doAfter(Object object) {
        if (log.isDebugEnabled()) {
            log.debug("翻译切面处理 object: {}", object);
        }
        if (object instanceof Result) {
            Result<?> result = (Result<?>) object;

            Object data = result.getData();
            if (data instanceof PagedVO) {
                PagedVO<?> pagedVO = (PagedVO<?>) data;
                for (Object o : pagedVO.getList()) {
                    doTranslateObject(o);
                }
                return;
            }
            doTranslateObject(data);
            return;
        }
        log.warn("未命中@Transform,返回值必须为Result");
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
        if (IEnum.class.isAssignableFrom(aClass)) {
            String translate = new EnumITransformer().transform(filedValue, aClass, null);
            if (null != translate) {
                ReflectUtil.invokeSet(result, field, translate);
            }
            return;
        }
        // TODO: 2022/11/26 需要改造，目前是直接调用mdc接口的需要改为feign
////        ITransformer<Object> transformer = SpringContextHolder.getBean(annotation.transformer());
//        Object bean = SpringContextHolder.getBean("dictTransformer");
//        if (bean instanceof ITransformer) {
//            ITransformer transformer = (ITransformer) bean;
//            String translate = transformer.transform(filedValue, annotation.dataSource(), annotation.param());
//            if (null != translate) {
//                ReflectUtil.invokeSet(result, field, translate);
//            }
//        }

        String translate = transform.transform(filedValue, annotation.dataSource(), annotation.param());
        if (null != translate) {
            ReflectUtil.invokeSet(result, field, translate);
        }
    }
}
