package io.gitee.dqcer.mcdull.framework.flow.registry;

import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import io.gitee.dqcer.mcdull.framework.base.util.ClassUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Node Bean Post Processor
 *
 * @author dqcer
 * @since 2023/01/07 19:01:17
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
