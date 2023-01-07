package com.dqcer.mcdull.admin.flow.registry;

import com.dqcer.framework.base.util.JsonUtil;
import com.dqcer.framework.base.util.ObjUtil;
import com.dqcer.mcdull.admin.flow.properties.Process;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileSystemProcessDefinitionReader implements ProcessDefinitionReader {

    private static final TypeReference<List<Process>> LIST_OF_PROCESS = new TypeReference<List<Process>>() {};

    public static final String DEFAULT_PROCESS_FILE_PATH = "flow/process_flow_*.json";

    private final ResourcePatternResolver fileResolver = new PathMatchingResourcePatternResolver();

    private Set<Process> definedProcesses;


    public Set<Process> getDefinedProcesses() {
        return definedProcesses;
    }

    public void setDefinedProcesses(Set<Process> definedProcesses) {
        this.definedProcesses = definedProcesses;
    }

    public ResourcePatternResolver getFileResolver() {
        return fileResolver;
    }

    /**
     * 加载process列表
     *
     * @return
     */
    @Override
    public Collection<Process> loadProcessDefinition() {
        if (ObjUtil.isNotNull(this.definedProcesses)) {
            return definedProcesses;
        }

        try {
            List<Process> load = load(DEFAULT_PROCESS_FILE_PATH);
            this.definedProcesses = Collections.unmodifiableSet(new HashSet<>(load));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.definedProcesses;
    }

    protected List<Process> load(String resourcePath) throws Exception {
        List<Process> processList = new ArrayList<>();
        Resource[] resources = fileResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resourcePath);
        for (Resource resource : resources) {
            processList.addAll(JsonUtil.deserializeList(resource.getInputStream(), LIST_OF_PROCESS));
        }
        return processList;
    }
}
