package com.dqcer.mcdull.admin.flow.config;


import com.dqcer.mcdull.admin.flow.registry.FileSystemProcessDefinitionReader;
import com.dqcer.mcdull.admin.flow.registry.NodeRegistry;
import com.dqcer.mcdull.admin.flow.registry.ProcessDefinitionReader;
import com.dqcer.mcdull.admin.flow.registry.ProcessDefinitionRegistry;
import com.dqcer.mcdull.admin.flow.registry.ProcessFlowRegistry;
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
        return new FileSystemProcessDefinitionReader();
    }

    @Bean
    public ProcessFlowRegistry processNodeAssembler(NodeRegistry nodeRegistry, ProcessDefinitionRegistry processDefinitionRegistry){
        return new ProcessFlowRegistry(nodeRegistry,processDefinitionRegistry,processDefinitionReader());
    }
}
