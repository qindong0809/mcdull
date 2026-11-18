package io.gitee.dqcer.mcdull.framework.tag.config;

import feign.RequestInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.server.WebFilter;

import static org.assertj.core.api.Assertions.assertThat;

class TagAutoConfigurationTest {

    // 模拟普通 Servlet 业务微服务上下文
    private final WebApplicationContextRunner servletContextRunner = new WebApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(TagAutoConfiguration.class));

    // 模拟 WebFlux 网关响应式上下文
    private final ReactiveWebApplicationContextRunner reactiveContextRunner = new ReactiveWebApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(TagAutoConfiguration.class));

    @Test
    @DisplayName("1. Servlet 业务微服务环境：自动装配 ServletFilter、Feign拦截器与异步装饰器，且不加载网关 WebFilter")
    void testServletMicroserviceAutoConfiguration() {
        servletContextRunner.run(context -> {
            assertThat(context).hasSingleBean(TagProperties.class);
            assertThat(context).hasBean("envTagTaskDecorator");
            assertThat(context).getBean("envTagTaskDecorator").isInstanceOf(TaskDecorator.class);

            // 验证 Servlet 过滤器已注册
            assertThat(context).hasBean("envTagServletFilter");
            assertThat(context).getBean("envTagServletFilter").isInstanceOf(FilterRegistrationBean.class);

            // 验证 OpenFeign 拦截器已注册
            assertThat(context).hasSingleBean(RequestInterceptor.class);

            // 验证隔离性：Servlet 环境下绝不能加载网关专用的 prodEnvTagCleanFilter
            assertThat(context).doesNotHaveBean("prodEnvTagCleanFilter");
        });
    }

    @Test
    @DisplayName("2. WebFlux 网关（dev 开发环境）：跳过 ServletFilter，且不激活生产环境防伪造清洗过滤器")
    void testReactiveGatewayDevAutoConfiguration() {
        reactiveContextRunner
            .withPropertyValues("spring.profiles.active=dev")
            .run(context -> {
                assertThat(context).hasSingleBean(TagProperties.class);
                assertThat(context).hasBean("envTagTaskDecorator");

                // 验证隔离性：WebFlux 网关绝不能加载 Servlet 过滤器
                assertThat(context).doesNotHaveBean("envTagServletFilter");

                // 验证开发环境下不清洗外部传入的 X-Env-Tag
                assertThat(context).doesNotHaveBean("prodEnvTagCleanFilter");
            });
    }

    @Test
    @DisplayName("3. WebFlux 网关（prod 生产环境）：自动激活 prodEnvTagCleanFilter 清洗外部伪造标签")
    void testReactiveGatewayProdAutoConfiguration() {
        reactiveContextRunner
            // 显式为测试上下文激活 prod Profile
            .withInitializer(context -> context.getEnvironment().setActiveProfiles("prod"))
            .run(context -> {
                assertThat(context).hasSingleBean(TagProperties.class);

                // 验证生产环境防伪造清洗过滤器成功激活
                assertThat(context).hasBean("prodEnvTagCleanFilter");
                assertThat(context).getBean("prodEnvTagCleanFilter").isInstanceOf(WebFilter.class);

                // 验证隔离性：依然不加载 Servlet 过滤器
                assertThat(context).doesNotHaveBean("envTagServletFilter");
            });
    }


    @Test
    @DisplayName("4. 全局开关关闭 (mcdull.tag.enabled=false)：所有相关 Bean 均不加载")
    void testDisabledAutoConfiguration() {
        servletContextRunner
            .withPropertyValues("mcdull.tag.enabled=false")
            .run(context -> {
                assertThat(context).doesNotHaveBean(TagAutoConfiguration.class);
                assertThat(context).doesNotHaveBean(TagProperties.class);
                assertThat(context).doesNotHaveBean("envTagTaskDecorator");
                assertThat(context).doesNotHaveBean("envTagServletFilter");
                assertThat(context).doesNotHaveBean(RequestInterceptor.class);
            });
    }
}
