package io.gitee.dqcer.mcdull.system.facade.service.adapter;

import io.gitee.dqcer.mcdull.framework.redis.cache.ICache;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSource;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceCipherResolver;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteTenantDataSourceVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantDataSourceFeignResolverTest {

    @Mock
    private ICache cache;

    @Mock
    private TenantDataSourceCipherResolver cipherResolver;

    @InjectMocks
    private TenantDataSourceFeignResolver resolver;

    @Test
    void cachedValueStaysCipheredAndReturnedValueIsDecrypted() {
        RemoteTenantDataSourceVO cached = cachedVo();
        stubCache(cached);
        when(cipherResolver.decrypt("CIPHER")).thenReturn("PLAIN");

        TenantDataSource result = resolver.resolve(12);

        assertEquals("PLAIN", result.getPassword());
        assertEquals("CIPHER", cached.getPassword());
        assertEquals("tenant_12", result.getKey());
    }

    @Test
    void decryptFailureDegradesToNoRouting() {
        stubCache(cachedVo());
        when(cipherResolver.decrypt("CIPHER")).thenThrow(new IllegalStateException("bad key"));

        assertNull(resolver.resolve(12));
    }

    private void stubCache(RemoteTenantDataSourceVO cached) {
        when(cache.getOrSetPrefixed(eq("tenant:ds:12"), eq(RemoteTenantDataSourceVO.class),
                any(Supplier.class), anyLong())).thenReturn(cached);
    }

    private RemoteTenantDataSourceVO cachedVo() {
        RemoteTenantDataSourceVO vo = new RemoteTenantDataSourceVO();
        vo.setKey("tenant_12");
        vo.setTenantId(12);
        vo.setUrl("jdbc:mysql://localhost:3306/tenant_12");
        vo.setUsername("root");
        vo.setPassword("CIPHER");
        return vo;
    }
}
