package io.gitee.dqcer.mcdull.framework.flow.load;

import io.gitee.dqcer.mcdull.framework.base.util.JsonUtil;
import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.flow.properties.ProcessBean;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * json 文件 读取配置
 *
 * @author dqcer
 * @date 2023/01/08 16:01:54
 */
public class JsonFileProcessDefinitionReader implements ProcessDefinitionReader {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final TypeReference<List<ProcessBean>> LIST_OF_PROCESS = new TypeReference<List<ProcessBean>>() {};

    public static final String DEFAULT_PROCESS_FILE_PATH = "flow/process_flow_*.json";

    private final ResourcePatternResolver fileResolver = new PathMatchingResourcePatternResolver();

    private Set<ProcessBean> definedProcessBeans;


    public Set<ProcessBean> getDefinedProcessBeans() {
        return definedProcessBeans;
    }

    public void setDefinedProcessBeans(Set<ProcessBean> definedProcessBeans) {
        this.definedProcessBeans = definedProcessBeans;
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
    public Collection<ProcessBean> loadProcessDefinition() {
        if (ObjUtil.isNotNull(this.definedProcessBeans)) {
            return definedProcessBeans;
        }

        try {
            List<ProcessBean> load = load(DEFAULT_PROCESS_FILE_PATH);
            this.definedProcessBeans = Collections.unmodifiableSet(new HashSet<>(load));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return this.definedProcessBeans;
    }

    protected List<ProcessBean> load(String resourcePath) throws Exception {
        List<ProcessBean> processBeanList = new ArrayList<>();
        Resource[] resources = fileResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resourcePath);
        for (Resource resource : resources) {
            processBeanList.addAll(JsonUtil.deserializeList(resource.getInputStream(), LIST_OF_PROCESS));
        }
        return processBeanList;
    }
}
