package io.gitee.dqcer.mcdull.framework.flow.registry;

import cn.hutool.core.annotation.AnnotationUtil;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import jakarta.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


/**
 * Node Bean Post Processor
 *
 * @author dqcer
 * @since 2023/01/07 19:01:17
 */
//@Component
public class NodeBeanPostProcessor implements BeanPostProcessor {

    @Resource
    private NodeRegistry nodeRegistry;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof ProcessHandler)) {
           return bean;
        }

        TreeNode annotation = AnnotationUtil.getAnnotation(bean.getClass(), TreeNode.class);
        if (annotation == null) {
            return bean;
        }
        ProcessHandler processHandler = (ProcessHandler) bean;
       nodeRegistry.register(processHandler);
        return bean;
    }
}
