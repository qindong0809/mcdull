package io.gitee.dqcer.mcdull.gateway.filter;

import cn.dev33.satoken.same.SaSameUtil;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ForwardAuthFilterTest {

    private final ForwardAuthFilter filter = new ForwardAuthFilter();

    @Test
    void addsSameTokenWhenAbsent() {
        List<String> values = runFilter(null);

        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals(SaSameUtil.getToken(), values.get(0));
    }

    @Test
    void overwritesClientProvidedSameToken() {
        String forged = "client-forged-token";
        List<String> values = runFilter(List.of(forged));

        assertNotNull(values);
        assertEquals(1, values.size());
        assertNotEquals(forged, values.get(0));
    }

    @Test
    void multipleClientTokensAreCollapsedToSingleValue() {
        List<String> values = runFilter(List.of("forged-1", "forged-2"));

        assertNotNull(values);
        assertEquals(1, values.size());
    }

    private List<String> runFilter(List<String> incoming) {
        AtomicReference<List<String>> result = new AtomicReference<>();
        GatewayFilterChain chain = exchange -> {
            result.set(exchange.getRequest().getHeaders().get(SaSameUtil.SAME_TOKEN));
            return Mono.empty();
        };
        var builder = MockServerHttpRequest.get("/test");
        if (incoming != null) {
            for (String value : incoming) {
                builder.header(SaSameUtil.SAME_TOKEN, value);
            }
        }
        filter.filter(MockServerWebExchange.from(builder), chain).block();
        return result.get();
    }
}
