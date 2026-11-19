package io.gitee.dqcer.mcdull.framework.tag.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.gitee.dqcer.mcdull.framework.tag.context.EnvTagContextPropagator;
import io.gitee.dqcer.mcdull.framework.tag.context.EnvTagContextHolder;
import io.gitee.dqcer.mcdull.framework.tag.core.TagLoadBalancer;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.core.task.TaskDecorator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 全链路场景一： http -> 服务器公共网关 -> 本地业务服务 A -> 异步线程 (@Async / 线程池) -> OpenFeign 调用 -> 服务器公共用户服务”
 * 编写的全链路端到端单元测试
 */
class FullLinkScenarioOneTest {

    private TagLoadBalancer serviceALoadBalancer;
    private TagLoadBalancer userServiceLoadBalancer;
    private Filter serviceAServletFilter;
    private RequestInterceptor feignInterceptor;
    private ThreadPoolTaskExecutor serviceAAsyncExecutor;

    // 1. 业务服务 A 的注册中心实例（包含：公共服 + 张三本地电脑）
    private final ServiceInstance publicServiceA = new DefaultServiceInstance(
        "service-a-public", "service-a", "10.0.1.10", 8081, false, Collections.emptyMap());
    private final ServiceInstance localZhangsanServiceA = new DefaultServiceInstance(
        "service-a-zhangsan", "service-a", "192.168.1.100", 8081, false, Map.of("env-tag", "zhangsan"));

    // 2. 用户服务的注册中心实例（仅包含：公共服，张三本地未启动用户服务）
    private final ServiceInstance publicUserService = new DefaultServiceInstance(
        "user-service-public", "system-bootstrap", "10.0.1.20", 8082, false, Collections.emptyMap());

    @BeforeEach
    void setUp() {
        TagProperties properties = new TagProperties();
        properties.setEnabled(true);
        properties.setStrict(false);

        // 初始化网关针对 service-a 的负载均衡器
        serviceALoadBalancer = createLoadBalancer("service-a", List.of(publicServiceA, localZhangsanServiceA), properties);
        // 初始化针对用户服务 system-bootstrap 的负载均衡器
        userServiceLoadBalancer = createLoadBalancer("system-bootstrap", List.of(publicUserService), properties);

        // 初始化框架组件（Servlet 过滤器、Feign 拦截器、带上下文传播的异步线程池）
        serviceAServletFilter = new TagAutoConfiguration.ServletTagConfig().envTagServletFilter().getFilter();
        feignInterceptor = new TagAutoConfiguration.FeignTagConfig().envTagFeignInterceptor();

        // 本模块不依赖 starter-web，这里按 AsyncContextSupport 的「捕获-回放-还原」就地复刻一层包装
        EnvTagContextPropagator propagator = new EnvTagContextPropagator();
        TaskDecorator taskDecorator = task -> {
            Object captured = propagator.capture();
            return () -> {
                Object previous = propagator.capture();
                propagator.apply(captured);
                try {
                    task.run();
                } finally {
                    propagator.apply(previous);
                }
            };
        };
        serviceAAsyncExecutor = new ThreadPoolTaskExecutor();
        serviceAAsyncExecutor.setCorePoolSize(2);
        serviceAAsyncExecutor.setMaxPoolSize(2);
        serviceAAsyncExecutor.setThreadNamePrefix("service-a-async-");
        serviceAAsyncExecutor.setTaskDecorator(taskDecorator);
        serviceAAsyncExecutor.initialize();
    }

    @AfterEach
    void tearDown() {
        EnvTagContextHolder.clear();
        if (serviceAAsyncExecutor != null) {
            serviceAAsyncExecutor.shutdown();
        }
    }

    @Test
    @DisplayName("场景一全链路：公共网关(带zhangsan标签) -> 命中本地服务A -> 异步线程 -> Feign透传标签 -> 兜底命中公共用户服务")
    void testScenarioOneFullLink() throws Exception {
        String developerTag = "zhangsan";

        // ==================== 步骤 1：公共网关路由选址 ====================
        HttpHeaders gatewayIncomingHeaders = new HttpHeaders();
        gatewayIncomingHeaders.add(EnvTagContextHolder.HEADER_ENV_TAG, developerTag);
        RequestData gatewayReqData = new RequestData(
            HttpMethod.GET, URI.create("http://service-a/order/create"), gatewayIncomingHeaders, null, Collections.emptyMap());

        Response<ServiceInstance> selectedServiceA = serviceALoadBalancer
            .choose(new DefaultRequest<>(new RequestDataContext(gatewayReqData)))
            .block();

        assertNotNull(selectedServiceA);
        assertEquals("service-a-zhangsan", selectedServiceA.getServer().getInstanceId(),
            "【断言1失败】网关必须精准路由到张三本地的业务服务 A！");

        // ==================== 步骤 2：请求进入本地服务 A 的 Servlet 过滤器 ====================
        MockHttpServletRequest serviceARequest = new MockHttpServletRequest();
        serviceARequest.addHeader(EnvTagContextHolder.HEADER_ENV_TAG, developerTag);
        MockHttpServletResponse serviceAResponse = new MockHttpServletResponse();

        // 用于记录异步子线程中执行 Feign 调用的结果
        AtomicReference<String> feignHeaderTagCaptured = new AtomicReference<>();
        AtomicReference<String> selectedUserServiceId = new AtomicReference<>();
        CompletableFuture<Void> asyncTaskFuture = new CompletableFuture<>();

        MockFilterChain serviceAFilterChain = new MockFilterChain() {
            @Override
            public void doFilter(ServletRequest req, ServletResponse res) {
                // 确认服务 A 主线程已成功提取标签
                assertEquals(developerTag, EnvTagContextHolder.getTag());

                // ==================== 步骤 3：服务 A 内部发起 @Async 异步调用 ====================
                serviceAAsyncExecutor.execute(() -> {
                    try {
                        // ==================== 步骤 4：异步线程内发起 OpenFeign 调用用户服务 ====================
                        RequestTemplate feignTemplate = new RequestTemplate();
                        feignInterceptor.apply(feignTemplate);

                        // 记录 Feign 发出的请求头是否携带了 zhangsan 标签
                        String headerVal = feignTemplate.headers()
                            .getOrDefault(EnvTagContextHolder.HEADER_ENV_TAG, Collections.emptyList())
                            .stream().findFirst().orElse(null);
                        feignHeaderTagCaptured.set(headerVal);

                        // ==================== 步骤 5：负载均衡器为用户服务选址（触发基准兜底） ====================
                        Response<ServiceInstance> selectedUserInst = userServiceLoadBalancer
                            .choose(new DefaultRequest<>())
                            .block();
                        if (selectedUserInst != null && selectedUserInst.hasServer()) {
                            selectedUserServiceId.set(selectedUserInst.getServer().getInstanceId());
                        }
                        asyncTaskFuture.complete(null);
                    } catch (Throwable t) {
                        asyncTaskFuture.completeExceptionally(t);
                    }
                });
            }
        };

        // 触发服务 A 过滤链执行
        serviceAServletFilter.doFilter(serviceARequest, serviceAResponse, serviceAFilterChain);

        // 等待异步子线程执行完毕（最多等 3 秒）
        asyncTaskFuture.get(3, TimeUnit.SECONDS);

        // ==================== 最终全链路断言验收 ====================
        assertEquals(developerTag, feignHeaderTagCaptured.get(),
            "【断言2失败】异步子线程发起 Feign 调用时，必须自动在请求头中透传 zhangsan 标签！");
        assertEquals("user-service-public", selectedUserServiceId.get(),
            "【断言3失败】张三本地未启动用户服务时，必须无缝兜底路由到公共用户服务实例！");
        assertNull(EnvTagContextHolder.getTag(),
            "【断言4失败】主线程请求结束后，ThreadLocal 上下文必须已清理干净！");
    }

    private TagLoadBalancer createLoadBalancer(String serviceId, List<ServiceInstance> instances, TagProperties props) {
        ServiceInstanceListSupplier supplier = mock(ServiceInstanceListSupplier.class);
        when(supplier.get(any())).thenReturn(Flux.just(instances));

        @SuppressWarnings("unchecked")
        ObjectProvider<ServiceInstanceListSupplier> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable(any())).thenReturn(supplier);

        return new TagLoadBalancer(provider, serviceId, props);
    }
}
