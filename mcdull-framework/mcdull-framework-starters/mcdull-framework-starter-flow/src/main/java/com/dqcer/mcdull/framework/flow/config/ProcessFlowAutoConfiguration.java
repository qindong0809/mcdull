package com.dqcer.mcdull.framework.flow.config;


import com.dqcer.mcdull.framework.flow.load.JsonFileProcessDefinitionReader;
import com.dqcer.mcdull.framework.flow.registry.NodeRegistry;
import com.dqcer.mcdull.framework.flow.load.ProcessDefinitionReader;
import com.dqcer.mcdull.framework.flow.factory.ProcessDefinitionRegistry;
import com.dqcer.mcdull.framework.flow.registry.ProcessFlowRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工作流程自动配置
 *
 * @author dqcer
 * @date 2023/01/07 20:01:41
 */
@Configuration
public class ProcessFlowAutoConfiguration {

    @Bean
    public ProcessDefinitionReader processDefinitionReader(){
        return new JsonFileProcessDefinitionReader();
    }

    @Bean
    public ProcessFlowRegistry processNodeAssembler(NodeRegistry nodeRegistry, ProcessDefinitionRegistry processDefinitionRegistry){
        return new ProcessFlowRegistry(nodeRegistry,processDefinitionRegistry,processDefinitionReader());
    }
}
