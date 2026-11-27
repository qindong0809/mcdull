package io.gitee.dqcer.mcdull.system.facade.service.adapter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AesTenantDataSourceCipherResolverTest {

    private static final String KEY = "1024abcd1024abcd1024abcd1024abcd";

    @Test
    void encryptThenDecrypt() {
        AesTenantDataSourceCipherResolver resolver = new AesTenantDataSourceCipherResolver(KEY);
        resolver.init();

        String cipher = resolver.encrypt("p@ssw0rd");

        assertNotEquals("p@ssw0rd", cipher);
        assertEquals("p@ssw0rd", resolver.decrypt(cipher));
    }

    @Test
    void invalidKeyIsRejected() {
        AesTenantDataSourceCipherResolver resolver = new AesTenantDataSourceCipherResolver("short");
        resolver.init();

        assertThrows(IllegalStateException.class, () -> resolver.encrypt("p@ssw0rd"));
    }
}
