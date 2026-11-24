package io.gitee.dqcer.mcdull.gateway.filter;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogFilterTest {

    private final LogFilter filter = new LogFilter();

    @AfterEach
    void cleanMdc() {
        MDC.remove(HttpHeaderConstants.LOG_TRACE_ID);
    }

    @Test
    void noIncomingHeaderGeneratesValidTraceId() {
        AtomicReference<String> output = new AtomicReference<>();
        AtomicReference<String> mdc = new AtomicReference<>();
        GatewayFilterChain chain = exchange -> {
            output.set(exchange.getRequest().getHeaders().getFirst(HttpHeaderConstants.TRACE_ID_HEADER));
            mdc.set(MDC.get(HttpHeaderConstants.LOG_TRACE_ID));
            return Mono.empty();
        };

        filter.filter(MockServerWebExchange.from(MockServerHttpRequest.get("/test")), chain).block();

        assertNotNull(output.get());
        assertTrue(output.get().matches(HttpHeaderConstants.TRACE_ID_PATTERN));
        assertEquals(output.get(), mdc.get());
        assertNull(MDC.get(HttpHeaderConstants.LOG_TRACE_ID));
    }

    @Test
    void validLowercaseHeaderIsPropagated() {
        String incoming = "0123456789abcdef0123456789abcdef";
        assertEquals(incoming, runFilterWithHeader(incoming));
    }

    @Test
    void validUppercaseHeaderIsLowercased() {
        String incoming = "0123456789ABCDEF0123456789ABCDEF";
        assertEquals(incoming.toLowerCase(), runFilterWithHeader(incoming));
    }

    @Test
    void malformedHeaderIsRegenerated() {
        assertRegenerated("01234567-89ab-cdef-0123-456789abcdef");
        assertRegenerated("not-a-valid-trace-id");
        assertRegenerated("0123456789abcdef0123456789abcde ");
        assertRegenerated("0123456789abcdef0123456789abcdef0123456789abcdef");
    }

    @Test
    void duplicateHeadersAreReplacedWithSingleValue() {
        AtomicReference<String> size = new AtomicReference<>();
        GatewayFilterChain chain = exchange -> {
            List<String> values = exchange.getRequest().getHeaders().get(HttpHeaderConstants.TRACE_ID_HEADER);
            size.set(String.valueOf(values.size()));
            return Mono.empty();
        };
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                        .header(HttpHeaderConstants.TRACE_ID_HEADER,
                                "0123456789abcdef0123456789abcdef",
                                "ffffffffffffffffffffffffffffffff"));

        filter.filter(exchange, chain).block();

        assertEquals("1", size.get());
    }

    private String runFilterWithHeader(String incoming) {
        AtomicReference<String> output = new AtomicReference<>();
        GatewayFilterChain chain = exchange -> {
            output.set(exchange.getRequest().getHeaders().getFirst(HttpHeaderConstants.TRACE_ID_HEADER));
            return Mono.empty();
        };
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").header(HttpHeaderConstants.TRACE_ID_HEADER, incoming));
        filter.filter(exchange, chain).block();
        return output.get();
    }

    private void assertRegenerated(String incoming) {
        String output = runFilterWithHeader(incoming);
        assertNotNull(output);
        assertTrue(output.matches(HttpHeaderConstants.TRACE_ID_PATTERN));
        assertNotEquals(incoming, output);
    }
}
