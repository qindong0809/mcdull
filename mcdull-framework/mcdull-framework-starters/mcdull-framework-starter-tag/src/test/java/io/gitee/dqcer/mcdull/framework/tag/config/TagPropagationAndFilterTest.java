package io.gitee.dqcer.mcdull.framework.tag.config;

import feign.RequestTemplate;
import io.gitee.dqcer.mcdull.framework.tag.context.EnvTagContextPropagator;
import io.gitee.dqcer.mcdull.framework.tag.context.EnvTagContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilter;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TagPropagationAndFilterTest {

    @AfterEach
    void tearDown() {
        EnvTagContextHolder.clear();
    }

    @Test
    @DisplayName("1. Servlet 过滤器提取标签并在结束后自动清理防泄漏")
    void testServletFilterExtractAndClean() throws Exception {
        var filterBean = new TagAutoConfiguration.ServletTagConfig().envTagServletFilter().getFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(EnvTagContextHolder.HEADER_ENV_TAG, "feat-order");
        MockHttpServletResponse response = new MockHttpServletResponse();

        AtomicReference<String> tagInsideController = new AtomicReference<>();
        MockFilterChain chain = new MockFilterChain() {
            @Override
            public void doFilter(jakarta.servlet.ServletRequest req, jakarta.servlet.ServletResponse res) {
                // 模拟 Controller 内部读取标签
                tagInsideController.set(EnvTagContextHolder.getTag());
            }
        };

        filterBean.doFilter(request, response, chain);

        assertEquals("feat-order", tagInsideController.get(), "请求处理期间必须能拿到标签");
        assertNull(EnvTagContextHolder.getTag(), "请求结束后必须自动清理 ThreadLocal 防止内存泄漏");
    }

    @Test
    @DisplayName("2. OpenFeign 拦截器自动向下游透传 X-Env-Tag")
    void testFeignInterceptorPropagation() {
        EnvTagContextHolder.setTag("zhangsan");
        var interceptor = new TagAutoConfiguration.FeignTagConfig().envTagFeignInterceptor();

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertTrue(template.headers().containsKey(EnvTagContextHolder.HEADER_ENV_TAG));
        assertEquals("zhangsan", template.headers().get(EnvTagContextHolder.HEADER_ENV_TAG).iterator().next());
    }

    @Test
    @DisplayName("3. 上下文传播器：捕获快照、回放到别的线程、空标签按无标签处理")
    void testPropagatorCaptureAndApply() throws Exception {
        var propagator = new EnvTagContextPropagator();

        EnvTagContextHolder.setTag("zhangsan");
        Object snapshot = propagator.capture();

        // 场景 A：执行线程原本无标签，回放后必须看得到提交线程的标签
        AtomicReference<String> tagInWorker = new AtomicReference<>();
        Thread workerThread = new Thread(() -> {
            propagator.apply(snapshot);
            tagInWorker.set(EnvTagContextHolder.getTag());
        });
        workerThread.start();
        workerThread.join();
        assertEquals("zhangsan", tagInWorker.get(), "异步子线程必须成功继承主线程标签");

        // 场景 B：快照为空时按「无标签」处理，回放要清掉执行线程的残留值
        EnvTagContextHolder.setTag("feat-order");
        propagator.apply(null);
        assertNull(EnvTagContextHolder.getTag(), "回放空快照必须清空，否则任务会沿用上一个任务的标签");

        // 场景 C：空白串不构成标签
        EnvTagContextHolder.setTag("  ");
        assertNull(propagator.capture(), "空白标签应被视为无标签");

        // 捕获-替换-还原中的「还原」由 starter-web 的 AsyncContextSupport 负责，
        // 其 CallerRunsPolicy 现场还原语义在 starter-web 的 ThreadPoolDecoratorTest 中覆盖
    }

    @Test
    @DisplayName("4. 生产网关 WebFilter 自动剥离外部客户端伪造的 X-Env-Tag 请求头")
    void testProdGatewayWebFilterCleanHeader() {
        WebFilter prodFilter = new TagAutoConfiguration.ReactiveTagCleanConfig().prodEnvTagCleanFilter();

        MockServerHttpRequest request = MockServerHttpRequest.get("/uac/user/info")
            .header(EnvTagContextHolder.HEADER_ENV_TAG, "canary-hacker")
            .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        AtomicReference<Boolean> hasTagHeaderDownstream = new AtomicReference<>(true);
        prodFilter.filter(exchange, mutatedExchange -> {
            hasTagHeaderDownstream.set(
                mutatedExchange.getRequest().getHeaders().containsKey(EnvTagContextHolder.HEADER_ENV_TAG));
            return reactor.core.publisher.Mono.empty();
        }).block();

        assertFalse(hasTagHeaderDownstream.get(), "生产环境网关必须在入口处彻底移除外部传入的 X-Env-Tag！");
    }
}
