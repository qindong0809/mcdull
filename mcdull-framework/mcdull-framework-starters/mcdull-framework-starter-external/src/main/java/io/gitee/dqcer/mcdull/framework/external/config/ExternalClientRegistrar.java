package io.gitee.dqcer.mcdull.framework.external.config;

import io.gitee.dqcer.mcdull.framework.external.annotation.EnableExternalClients;
import io.gitee.dqcer.mcdull.framework.external.annotation.ExternalApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * Scans for interfaces annotated with {@link ExternalApi} and registers
 * {@link ExternalClientFactoryBean} for each one.
 *
 * @author dqcer
 * @since 1.0.0
 */
public class ExternalClientRegistrar implements ImportBeanDefinitionRegistrar {

    private static final Logger log = LoggerFactory.getLogger(ExternalClientRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnableExternalClients.class.getName());
        String[] basePackages = attrs != null ? (String[]) attrs.get("basePackages") : new String[0];

        if (basePackages.length == 0) {
            basePackages = new String[]{ClassUtils.getPackageName(importingClassMetadata.getClassName())};
        }

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface();
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(ExternalApi.class));

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                String className = candidate.getBeanClassName();
                try {
                    Class<?> apiInterface = Class.forName(className);
                    ExternalApi annotation = apiInterface.getAnnotation(ExternalApi.class);
                    String clientName = annotation.name();

                    BeanDefinitionBuilder builder = BeanDefinitionBuilder
                            .genericBeanDefinition(ExternalClientFactoryBean.class);
                    builder.addPropertyValue("apiInterface", apiInterface);
                    builder.addPropertyValue("clientName", clientName);
                    builder.setAutowireMode(2); // AUTOWIRE_BY_TYPE

                    String beanName = StringUtils.uncapitalize(apiInterface.getSimpleName());
                    registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
                    log.info("[ExternalClient] Registered: {} -> {}", beanName, clientName);
                } catch (ClassNotFoundException e) {
                    log.error("[ExternalClient] Failed to load class: {}", className, e);
                }
            }
        }
    }
}
