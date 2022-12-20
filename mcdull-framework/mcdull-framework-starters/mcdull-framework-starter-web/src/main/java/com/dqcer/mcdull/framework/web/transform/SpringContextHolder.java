package com.dqcer.mcdull.framework.web.transform;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.*;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringValueResolver;

import java.util.Map;

/**
 * Spring上下文工具
 *
 * @author dqcer
 * @version 2022/10/05
 */
public class SpringContextHolder implements ApplicationContextAware, EnvironmentAware, EmbeddedValueResolverAware, DisposableBean {

    private static ApplicationContext context;
    private static Environment environment;
    private static StringValueResolver stringValueResolver;

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> Map<String, T> getBeanMap(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    /**
     * 获取当前配置属性
     */
    public static String getProperty(String configKey) {
        return environment.getProperty(configKey);
    }

    /**
     * 获取spring value，和@Value的效果相同
     * 注意：如果是获取配置，找不到配置会报异常
     *
     * @param spEL spring表达式，须用"${spEL}"括起来
     * @return 表达式的值
     */
    public static String getSpringValue(String spEL) {
        return stringValueResolver.resolveStringValue(spEL);
    }

    /**
     * 获取当前profile
     */
    public static String getProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 当前环境是否是开发环境
     */
    public static boolean isDev() {
        return "dev".equals(getProfile());
    }

    /**
     * 当前环境是否是测试环境
     */
    public static boolean isTest() {
        return "test".equals(getProfile());
    }

    /**
     * 当前环境是否是生产环境
     */
    public static boolean isProd() {
        return "prod".equals(getProfile());
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.context = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        SpringContextHolder.environment = environment;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        SpringContextHolder.stringValueResolver = resolver;
    }

    public static void clearHolder() {
        context = null;
    }


//    public static void publishEvent(ApplicationEvent event) {
//        if (context == null) {
//            return;
//        }
//        context.publishEvent(event);
//    }

    @Override
    public void destroy() {
        SpringContextHolder.clearHolder();
    }
}
