package io.gitee.dqcer.mcdull.system.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * remote 模式下 system 契约的装配面校验
 * <p>
 * 多个契约共用同一个服务名时，若不各自给出 {@code contextId}，Feign 会为其生成同一个
 * {@code <contextId>.FeignClientSpecification} bean 名并抛出 {@code BeanDefinitionOverrideException}，
 * 表现为远程部署的服务启动即失败。这个冲突发生在 bean 定义阶段，所以测试只需要跑到定义注册为止。
 * <p>
 * 用一个把所有 bean 置为懒加载的后置处理器，是为了不去实例化 Feign 代理——那会连带把 Redis、
 * 注册中心这些外部依赖拖进单元测试。
 *
 * @author dqcer
 * @since 2026-11-16
 */
class SystemFeignAssemblyTest {

    /**
     * {@code @EnableFeignClients} 注册全局默认配置时使用的占位名，与具体客户端无关
     */
    private static final String DEFAULT_SPEC_CONTEXT_ID = "default";

    private static final ApplicationContextRunner RUNNER = new ApplicationContextRunner()
            .withUserConfiguration(RemoteConsumerApp.class);

    @Configuration(proxyBeanMethods = false)
    @EnableSystemFeignClients
    static class RemoteConsumerApp {

        @Bean
        static BeanFactoryPostProcessor everythingLazy() {
            return beanFactory -> {
                for (String name : beanFactory.getBeanDefinitionNames()) {
                    beanFactory.getBeanDefinition(name).setLazyInit(true);
                }
            };
        }
    }

    @Test
    @DisplayName("启用契约客户端后容器必须能起来")
    void contextMustStartWithoutBeanNameCollision() {
        RUNNER.run(ctx -> assertNull(ctx.getStartupFailure(),
                "启用 @EnableSystemFeignClients 后容器启动失败：" + ctx.getStartupFailure()));
    }

    @Test
    @DisplayName("注册出的客户端恰好是这五个契约，各自独占一个配置子上下文")
    void exactlyTheRemoteCapableContractsMustBeRegistered() {
        RUNNER.run(ctx -> assertEquals(
                Set.of("UserApi", "DeptApi", "RoleApi", "AuthApi", "ConfigApi"),
                contextIdsOf(ctx),
                "Feign 客户端集合与预期不符：多出来的是没有服务端的死契约，缺少的会在远程模式下注入失败"));
    }

    /**
     * Feign 以 {@code <contextId>.FeignClientSpecification} 之名注册每个客户端的配置占位 bean，
     * 因此这个 bean 名集合就是实际注册出来的 contextId 集合。
     * {@code default.} 前缀那条是全局默认配置的载体，不代表任何客户端，需排除。
     */
    private static Set<String> contextIdsOf(ConfigurableApplicationContext ctx) {
        return Arrays.stream(ctx.getBeanDefinitionNames())
                .filter(name -> name.endsWith(".FeignClientSpecification"))
                .map(name -> name.substring(0, name.indexOf('.')))
                .filter(contextId -> !DEFAULT_SPEC_CONTEXT_ID.equals(contextId))
                .collect(Collectors.toSet());
    }
}
