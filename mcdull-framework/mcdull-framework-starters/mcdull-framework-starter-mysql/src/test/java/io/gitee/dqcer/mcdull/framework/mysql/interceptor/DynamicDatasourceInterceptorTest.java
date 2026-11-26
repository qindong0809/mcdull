package io.gitee.dqcer.mcdull.framework.mysql.interceptor;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.properties.DataSourceProperties;
import io.gitee.dqcer.mcdull.framework.mysql.routing.SwitchableDataSource;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DynamicDatasourceInterceptorTest {

    private SwitchableDataSource switchableDataSource;
    private DataSourceProperties properties;
    private ObjectProvider<TenantDataSourceResolver> resolverProvider;
    private TenantDataSourceResolver resolver;
    private DynamicDatasourceInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        switchableDataSource = mock(SwitchableDataSource.class);
        properties = new DataSourceProperties();
        resolver = TenantDataSourceResolver.NOOP;
        resolverProvider = mock(ObjectProvider.class);
        when(resolverProvider.orderedStream()).thenAnswer(inv -> Stream.of(resolver));

        interceptor = new DynamicDatasourceInterceptor(switchableDataSource, properties, resolverProvider);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        DynamicContextHolder.clear();
        UserContextHolder.clearSession();
    }

    @AfterEach
    void tearDown() {
        DynamicContextHolder.clear();
        UserContextHolder.clearSession();
    }

    private void session(String userId, Set<Integer> tenantIds) {
        UnifySession session = new UnifySession();
        session.setUserId(userId);
        session.setTenantIds(tenantIds);
        UserContextHolder.setSession(session);
    }

    @Test
    void shouldUseMasterWhenLocalMasterConfigured() {
        properties.setUrl("jdbc:mysql://localhost:3306/master");
        request.addHeader(HttpHeaderConstants.T_ID, "42");

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    void shouldThrowRequiredWhenTenantHeaderMissing() {
        session("1", Set.of(42));

        assertThrows(BusinessException.class,
                () -> interceptor.preHandle(request, response, new Object()));
    }

    @Test
    void shouldThrowForbiddenWhenTenantNotAuthorized() {
        session("1", Set.of(43));
        request.addHeader(HttpHeaderConstants.T_ID, "42");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> interceptor.preHandle(request, response, new Object()));
        assertEquals("system.tenant.forbidden", ex.getMessageCode());
    }

    @Test
    void shouldKeepCurrentWhenResolverReturnsNull() {
        session("1", Set.of(42));
        request.addHeader(HttpHeaderConstants.T_ID, "42");
        DynamicContextHolder.push("master");

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals("master", DynamicContextHolder.peek());
    }

    @Test
    void shouldUseSessionAuthorizedTenantsWhenResolverReturns() {
        session("1", Set.of(42));
        request.addHeader(HttpHeaderConstants.T_ID, "42");
        resolver = new TenantDataSourceResolver() {
            @Override
            public Set<Integer> getAuthorizedTenantIds(String userId) {
                throw new AssertionError("should not be called when session has tenantIds");
            }

            @Override
            public io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSource resolve(Integer tenantId) {
                return null;
            }
        };

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }
}
