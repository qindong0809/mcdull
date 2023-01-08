package com.dqcer.mcdull.framework.flow.registry;

import com.dqcer.framework.base.util.ClassUtil;
import com.dqcer.mcdull.framework.flow.node.ProcessHandler;
import com.dqcer.mcdull.framework.flow.node.TreeNode;
import com.dqcer.mcdull.framework.flow.registry.NodeRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Node Bean Post Processor
 *
 * @author dqcer
 * @date 2023/01/07 19:01:17
 */
@Component
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

        TreeNode annotation = ClassUtil.findAnnotation(bean.getClass(), TreeNode.class);
        if (annotation == null) {
            return bean;
        }
        ProcessHandler processHandler = (ProcessHandler) bean;
       nodeRegistry.register(processHandler);
        return bean;
    }
}
