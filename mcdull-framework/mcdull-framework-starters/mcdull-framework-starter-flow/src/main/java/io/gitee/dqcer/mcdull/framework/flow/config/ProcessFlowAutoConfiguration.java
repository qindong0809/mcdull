package io.gitee.dqcer.mcdull.framework.flow.config;


import io.gitee.dqcer.mcdull.framework.flow.factory.ProcessDefinitionRegistry;
import io.gitee.dqcer.mcdull.framework.flow.load.JsonFileProcessDefinitionReader;
import io.gitee.dqcer.mcdull.framework.flow.load.ProcessDefinitionReader;
import io.gitee.dqcer.mcdull.framework.flow.registry.NodeBeanPostProcessor;
import io.gitee.dqcer.mcdull.framework.flow.registry.NodeRegistry;
import io.gitee.dqcer.mcdull.framework.flow.registry.ProcessFlowRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工作流程自动配置
 *
 * @author dqcer
 * @since 2023/01/07 20:01:41
 */
@Configuration
public class ProcessFlowAutoConfiguration {

    @Bean
    public ProcessDefinitionReader processDefinitionReader(){
        return new JsonFileProcessDefinitionReader();
    }

    @Bean
    public NodeBeanPostProcessor nodeBeanPostProcessor(){
        return new NodeBeanPostProcessor();
    }

    @Bean
    public ProcessFlowRegistry processNodeAssembler(NodeRegistry nodeRegistry, ProcessDefinitionRegistry processDefinitionRegistry){
        return new ProcessFlowRegistry(nodeRegistry,processDefinitionRegistry,processDefinitionReader());
    }
}
