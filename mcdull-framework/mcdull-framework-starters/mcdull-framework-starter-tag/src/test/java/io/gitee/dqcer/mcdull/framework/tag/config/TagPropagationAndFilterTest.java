package io.gitee.dqcer.mcdull.framework.tag.config;

import feign.RequestTemplate;
import io.gitee.dqcer.mcdull.framework.tag.context.EnvTagContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskDecorator;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilter;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TagPropagationAndFilterTest {

    private final TagAutoConfiguration autoConfig = new TagAutoConfiguration();

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
    @DisplayName("3. 异步装饰器支持子线程继承，且在同步退化(CallerRunsPolicy)后精准还原主线程现场")
    void testTaskDecoratorCaptureAndRestore() throws Exception {
        TaskDecorator decorator = autoConfig.envTagTaskDecorator();

        // 主线程提交任务时的标签为 "zhangsan"
        EnvTagContextHolder.setTag("zhangsan");
        AtomicReference<String> tagInsideAsyncTask = new AtomicReference<>();

        Runnable decoratedTask = decorator.decorate(() -> {
            tagInsideAsyncTask.set(EnvTagContextHolder.getTag());
        });

        // 场景 A：模拟异步子线程执行（初始无标签）
        EnvTagContextHolder.clear();
        Thread workerThread = new Thread(decoratedTask);
        workerThread.start();
        workerThread.join();
        assertEquals("zhangsan", tagInsideAsyncTask.get(), "异步子线程必须成功继承主线程标签");

        // 场景 B：模拟队列打满触发 CallerRunsPolicy，在另一个拥有 "lisi" 标签的线程上同步退化执行
        EnvTagContextHolder.setTag("lisi");
        decoratedTask.run();
        assertEquals("zhangsan", tagInsideAsyncTask.get(), "任务执行期间应使用捕获的 zhangsan 标签");
        assertEquals("lisi", EnvTagContextHolder.getTag(), "任务执行完毕后必须精准还原执行线程原有的 lisi 标签！");
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
