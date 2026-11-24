package io.gitee.dqcer.mcdull.framework.web.filter;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.spi.RequestDataRouting;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTraceLogFilterTest {

    private final HttpTraceLogFilter filter = new HttpTraceLogFilter(RequestDataRouting.NOOP);

    @AfterEach
    void clean() {
        UserContextHolder.clearSession();
        MDC.remove(HttpHeaderConstants.LOG_TRACE_ID);
    }

    @Test
    void validHeaderIsKeptInSessionAndMdc() throws Exception {
        String incoming = "0123456789ABCDEF0123456789ABCDEF";
        String[] captured = capture(request(incoming));

        assertEquals(incoming.toLowerCase(), captured[0]);
        assertEquals(incoming.toLowerCase(), captured[1]);
    }

    @Test
    void missingHeaderGeneratesValidTraceId() throws Exception {
        String[] captured = capture(request(null));

        assertNotNull(captured[0]);
        assertTrue(captured[0].matches(HttpHeaderConstants.TRACE_ID_PATTERN));
        assertEquals(captured[0], captured[1]);
    }

    @Test
    void malformedHeaderIsRegenerated() throws Exception {
        String incoming = "not-a-valid-trace-id";
        String[] captured = capture(request(incoming));

        assertNotNull(captured[0]);
        assertTrue(captured[0].matches(HttpHeaderConstants.TRACE_ID_PATTERN));
        assertNotEquals(incoming, captured[0]);
        assertEquals(captured[0], captured[1]);
    }

    @Test
    void contextIsClearedAfterFilter() throws Exception {
        filter.doFilter(request("0123456789abcdef0123456789abcdef"),
                new MockHttpServletResponse(), new MockFilterChain());

        assertNull(UserContextHolder.getSession());
        assertNull(MDC.get(HttpHeaderConstants.LOG_TRACE_ID));
    }

    private String[] capture(MockHttpServletRequest request) throws Exception {
        String[] captured = new String[2];
        FilterChain chain = (req, res) -> {
            if (UserContextHolder.getSession() != null) {
                captured[0] = UserContextHolder.getSession().getTraceId();
            }
            captured[1] = MDC.get(HttpHeaderConstants.LOG_TRACE_ID);
        };
        filter.doFilter(request, new MockHttpServletResponse(), chain);
        return captured;
    }

    private MockHttpServletRequest request(String header) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        if (header != null) {
            request.addHeader(HttpHeaderConstants.TRACE_ID_HEADER, header);
        }
        return request;
    }
}
