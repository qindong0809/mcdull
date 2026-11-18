package io.gitee.dqcer.mcdull.framework.tag;

import io.gitee.dqcer.mcdull.framework.tag.config.TagProperties;
import io.gitee.dqcer.mcdull.framework.tag.context.EnvTagContextHolder;
import io.gitee.dqcer.mcdull.framework.tag.core.TagLoadBalancer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TagLoadBalancerTest {

    private TagProperties tagProperties;
    private TagLoadBalancer loadBalancer;

    // 模拟注册中心的 3 个实例：公共基准服、张三本机、李四本机
    private final ServiceInstance baseInstance = new DefaultServiceInstance(
        "inst-base", "system-bootstrap", "10.0.1.10", 8080, false, Collections.emptyMap());
    private final ServiceInstance zhangsanInstance = new DefaultServiceInstance(
        "inst-zs", "system-bootstrap", "192.168.1.101", 8080, false, Map.of("env-tag", "zhangsan"));
    private final ServiceInstance lisiInstance = new DefaultServiceInstance(
        "inst-ls", "system-bootstrap", "192.168.1.102", 8080, false, Map.of("env-tag", "lisi"));

    @BeforeEach
    void setUp() {
        tagProperties = new TagProperties();
        tagProperties.setEnabled(true);
        tagProperties.setStrict(false);

        ServiceInstanceListSupplier supplier = mock(ServiceInstanceListSupplier.class);
        when(supplier.get(any())).thenReturn(Flux.just(List.of(baseInstance, zhangsanInstance, lisiInstance)));

        @SuppressWarnings("unchecked")
        ObjectProvider<ServiceInstanceListSupplier> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable(any())).thenReturn(supplier);

        loadBalancer = new TagLoadBalancer(provider, "system-bootstrap", tagProperties);
    }

    @AfterEach
    void tearDown() {
        EnvTagContextHolder.clear();
    }

    @Test
    @DisplayName("1. 网关场景：通过 RequestDataContext 请求头精准命中张三实例")
    void testGatewayHeaderExactMatch() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(EnvTagContextHolder.HEADER_ENV_TAG, "zhangsan");
        RequestData requestData = new RequestData(HttpMethod.GET, URI.create("http://system-bootstrap/info"), headers, null, Collections.emptyMap());
        Request<RequestDataContext> request = new DefaultRequest<>(new RequestDataContext(requestData));

        Response<ServiceInstance> response = loadBalancer.choose(request).block();
        assertNotNull(response);
        assertTrue(response.hasServer());
        assertEquals("inst-zs", response.getServer().getInstanceId());
    }

    @Test
    @DisplayName("2. 微服务场景：通过 ThreadLocal 上下文精准命中李四实例")
    void testThreadLocalContextExactMatch() {
        EnvTagContextHolder.setTag("lisi");
        Response<ServiceInstance> response = loadBalancer.choose(new DefaultRequest<>()).block();

        assertNotNull(response);
        assertEquals("inst-ls", response.getServer().getInstanceId());
    }

    @Test
    @DisplayName("3. 基准兜底场景：目标标签(wangwu)不存在时，自动降级走无标签公共实例")
    void testFallbackToDefaultBaseInstance() {
        EnvTagContextHolder.setTag("wangwu");
        Response<ServiceInstance> response = loadBalancer.choose(new DefaultRequest<>()).block();

        assertNotNull(response);
        assertEquals("inst-base", response.getServer().getInstanceId());
    }

    @Test
    @DisplayName("4. 防串台保留字：传入 default 时，强制跳过带标签实例走公共实例")
    void testDefaultKeywordBypassLocalInstance() {
        EnvTagContextHolder.setTag("default");
        Response<ServiceInstance> response = loadBalancer.choose(new DefaultRequest<>()).block();

        assertNotNull(response);
        assertEquals("inst-base", response.getServer().getInstanceId());
    }

    @Test
    @DisplayName("5. 严格模式：strict=true 且无匹配实例时，阻断回流并返回空响应")
    void testStrictModeBlocksFallback() {
        tagProperties.setStrict(true);
        EnvTagContextHolder.setTag("canary-v2");

        Response<ServiceInstance> response = loadBalancer.choose(new DefaultRequest<>()).block();
        assertNotNull(response);
        assertFalse(response.hasServer(), "严格模式下找不到目标标签实例时，严禁回流到公共实例！");
    }
}
