package io.gitee.dqcer.mcdull.gateway.filter;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraceIdPropagationContractTest {

    private final LogFilter filter = new LogFilter();

    @Test
    void gatewayOutputIsAlwaysAcceptedByServerRule() {
        String generated = runGateway(null);
        assertNotNull(generated);
        assertServerAccepts(generated);

        String valid = "0123456789abcdef0123456789abcdef";
        assertEquals(valid, runGateway(valid));
        assertServerAccepts(runGateway(valid));

        String upper = "0123456789ABCDEF0123456789ABCDEF";
        assertEquals(upper.toLowerCase(Locale.ROOT), runGateway(upper));
        assertServerAccepts(runGateway(upper));
    }

    @Test
    void malformedUpstreamIdNeverReachesServer() {
        assertServerAccepts(runGateway("01234567-89ab-cdef-0123-456789abcdef"));
        assertServerAccepts(runGateway("not-a-valid-trace-id"));
    }

    private String runGateway(String incoming) {
        AtomicReference<String> output = new AtomicReference<>();
        GatewayFilterChain chain = exchange -> {
            output.set(exchange.getRequest().getHeaders().getFirst(HttpHeaderConstants.TRACE_ID_HEADER));
            return Mono.empty();
        };
        MockServerWebExchange exchange = incoming == null
                ? MockServerWebExchange.from(MockServerHttpRequest.get("/test"))
                : MockServerWebExchange.from(MockServerHttpRequest.get("/test")
                        .header(HttpHeaderConstants.TRACE_ID_HEADER, incoming));
        filter.filter(exchange, chain).block();
        return output.get();
    }

    private void assertServerAccepts(String value) {
        assertNotNull(value);
        assertTrue(value.trim().matches(HttpHeaderConstants.TRACE_ID_PATTERN));
        assertEquals(value.trim().toLowerCase(Locale.ROOT), value);
    }
}
