package io.gitee.dqcer.mcdull.framework.integration.config;

import io.gitee.dqcer.mcdull.framework.integration.annotation.IntegrationApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Scans for interfaces annotated with {@link IntegrationApi} and registers
 * {@link IntegrationClientFactoryBean} for each one.
 *
 * <p>Scan packages are resolved from {@code mcdull.integration.base-packages} first;
 * when absent, the application base package (where the main class resides) is used.
 *
 * @author dqcer
 * @since 1.0.0
 */
public class IntegrationClientRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(IntegrationClientRegistrar.class);

    private static final String BASE_PACKAGES_PROPERTY = "mcdull.integration.base-packages";

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        String[] basePackages = resolveBasePackages(registry);
        if (basePackages.length == 0) {
            return;
        }

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface();
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(IntegrationApi.class));

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                String className = candidate.getBeanClassName();
                try {
                    Class<?> apiInterface = Class.forName(className);
                    IntegrationApi annotation = apiInterface.getAnnotation(IntegrationApi.class);
                    String clientName = annotation.name();

                    BeanDefinitionBuilder builder = BeanDefinitionBuilder
                            .genericBeanDefinition(IntegrationClientFactoryBean.class);
                    builder.addPropertyValue("apiInterface", apiInterface);
                    builder.addPropertyValue("clientName", clientName);

                    String beanName = StringUtils.uncapitalize(apiInterface.getSimpleName());
                    registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
                    log.info("[IntegrationClient] Registered: {} -> {}", beanName, clientName);
                } catch (ClassNotFoundException e) {
                    log.error("[IntegrationClient] Failed to load class: {}", className, e);
                }
            }
        }
    }

    private String[] resolveBasePackages(BeanDefinitionRegistry registry) {
        Set<String> packages = new LinkedHashSet<>();

        String[] configured = environment == null ? null : environment.getProperty(BASE_PACKAGES_PROPERTY, String[].class);
        if (configured != null) {
            for (String pkg : configured) {
                if (StringUtils.hasText(pkg)) {
                    packages.add(pkg.trim());
                }
            }
        }

        if (packages.isEmpty() && registry instanceof org.springframework.beans.factory.BeanFactory beanFactory
                && AutoConfigurationPackages.has(beanFactory)) {
            packages.addAll(AutoConfigurationPackages.get(beanFactory));
        }

        return packages.toArray(new String[0]);
    }
}
