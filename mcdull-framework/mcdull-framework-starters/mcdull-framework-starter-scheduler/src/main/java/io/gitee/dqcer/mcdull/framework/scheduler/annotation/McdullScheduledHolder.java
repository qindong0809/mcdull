package io.gitee.dqcer.mcdull.framework.scheduler.annotation;

import java.lang.reflect.Method;

/**
 * 被 {@link McdullScheduled} 注解标记的方法元数据持有者。
 * <p>供 TaskRegistry 扫描后统一收集。
 *
 * @author dqcer
 * @since 1.0.0
 */
public class McdullScheduledHolder {

    private final Object bean;
    private final Method method;
    private final McdullScheduled annotation;

    public McdullScheduledHolder(Object bean, Method method, McdullScheduled annotation) {
        this.bean = bean;
        this.method = method;
        this.annotation = annotation;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    public McdullScheduled getAnnotation() {
        return annotation;
    }

    public String getName() {
        return annotation.name();
    }
}
