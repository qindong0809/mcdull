package io.gitee.dqcer.mcdull.framework.scheduler.registry;

import io.gitee.dqcer.mcdull.framework.scheduler.annotation.McdullScheduled;
import io.gitee.dqcer.mcdull.framework.scheduler.annotation.McdullScheduledHolder;
import io.gitee.dqcer.mcdull.framework.scheduler.node.McdullScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

/**
 * BeanPostProcessor：在所有 Bean 创建完成后，扫描并收集：
 * <ul>
 *     <li>实现了 {@link McdullScheduledTask} 接口的 Bean</li>
 *     <li>标注了 {@link McdullScheduled} 注解的方法</li>
 * </ul>
 *
 * @author dqcer
 * @since 1.0.0
 */
@Slf4j
public class TaskRegistrarPostProcessor implements BeanPostProcessor {

    private final TaskRegistry taskRegistry;

    public TaskRegistrarPostProcessor(TaskRegistry taskRegistry) {
        this.taskRegistry = taskRegistry;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (targetClass == null) {
            targetClass = bean.getClass();
        }

        // 1. 检查是否实现了 McdullScheduledTask 接口
        if (bean instanceof McdullScheduledTask) {
            taskRegistry.register((McdullScheduledTask<?>) bean);
        }

        // 2. 扫描 @McdullScheduled 注解方法
        for (Method method : targetClass.getDeclaredMethods()) {
            McdullScheduled annotation = method.getAnnotation(McdullScheduled.class);
            if (annotation != null) {
                if (method.getParameterCount() != 0) {
                    log.warn("[mcdull-scheduler] @McdullScheduled 注解的方法必须无参数，跳过: {}.{}",
                            targetClass.getName(), method.getName());
                    continue;
                }
                if (method.getReturnType() != void.class) {
                    log.warn("[mcdull-scheduler] @McdullScheduled 注解的方法必须返回 void，跳过: {}.{}",
                            targetClass.getName(), method.getName());
                    continue;
                }
                method.setAccessible(true);
                taskRegistry.register(new McdullScheduledHolder(bean, method, annotation));
            }
        }

        return bean;
    }
}
