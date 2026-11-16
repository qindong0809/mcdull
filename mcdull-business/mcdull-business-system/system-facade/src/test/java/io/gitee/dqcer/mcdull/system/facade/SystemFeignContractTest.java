package io.gitee.dqcer.mcdull.system.facade;

import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.AuthApi;
import io.gitee.dqcer.mcdull.system.facade.service.ConfigApi;
import io.gitee.dqcer.mcdull.system.facade.service.def.DeptApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.def.RoleApiDef;
import io.gitee.dqcer.mcdull.system.facade.service.def.UserApiDef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * system 契约的服务名与装配面校验
 * <p>
 * 断言全部走 {@code @EnableFeignClients} 实际使用的读取路径（{@link AnnotationTypeFilter} +
 * {@code AnnotationMetadata#getAnnotationAttributes}），而不是直接反射读 {@code @RpcClient} 的成员——
 * 后者只能证明源码里写了什么，证明不了容器启动时会注册出什么。
 *
 * @author dqcer
 * @since 2026-11-16
 */
class SystemFeignContractTest {

    /**
     * 直接标注 {@code @FeignClient} 的客户端
     */
    private static final List<Class<?>> DIRECT_CLIENTS = List.of(AuthApi.class, ConfigApi.class);

    /**
     * 通过元注解 {@code RpcClient} 声明、应当被扫描注册的契约
     */
    private static final Set<Class<?>> META_ANNOTATED_CONTRACTS =
            Set.of(UserApiDef.class, DeptApiDef.class, RoleApiDef.class);

    @Test
    @DisplayName("直接声明的客户端必须指向契约里的服务名")
    void directClientsMustUseContractServiceName() {
        for (Class<?> client : DIRECT_CLIENTS) {
            FeignClient feignClient = client.getAnnotation(FeignClient.class);
            assertNotNull(feignClient, client.getName() + " 缺少 @FeignClient");
            assertEquals(SystemConstant.SERVICE_NAME, resolvedName(client), client.getName() + " 的目标服务名不是单一来源");
        }
    }

    @Test
    @DisplayName("元注解声明的契约解析出的服务名必须与常量一致")
    void metaAnnotatedContractsMustUseContractServiceName() {
        for (Class<?> contract : META_ANNOTATED_CONTRACTS) {
            assertEquals(SystemConstant.SERVICE_NAME, resolvedName(contract),
                    contract.getName() + " 经 @AliasFor 合并后拿到的服务名不正确");
        }
    }

    @Test
    @DisplayName("契约包必须只扫出预期的客户端，且 CLIENT_BASE_PACKAGE 指对了包")
    void contractPackageMustRegisterExactlyTheExpectedClients() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false) {
                    @Override
                    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
                    }
                };
        scanner.addIncludeFilter(new AnnotationTypeFilter(FeignClient.class));

        Set<String> discovered = scanner.findCandidateComponents(SystemConstant.CLIENT_BASE_PACKAGE).stream()
                .map(BeanDefinition::getBeanClassName)
                .collect(Collectors.toSet());

        Set<String> expected = META_ANNOTATED_CONTRACTS.stream().map(Class::getName).collect(Collectors.toSet());
        assertEquals(expected, discovered,
                "扫描 " + SystemConstant.CLIENT_BASE_PACKAGE + " 得到的客户端集合与预期不符，remote 模式会装配出多余或缺失的代理");
    }

    /**
     * 复刻 FeignClientsRegistrar#getName 的取值顺序：serviceId -> name -> value
     */
    private static String resolvedName(Class<?> type) {
        Map<String, Object> attributes = metadata(type).getAnnotationAttributes(FeignClient.class.getName());
        assertNotNull(attributes, type.getName() + " 读不到 @FeignClient 属性");
        for (String key : List.of("serviceId", "name", "value")) {
            Object candidate = attributes.get(key);
            if (candidate instanceof String text && !text.isBlank()) {
                return text;
            }
        }
        return null;
    }

    private static StandardAnnotationMetadata metadata(Class<?> type) {
        return new StandardAnnotationMetadata(type, true);
    }
}
