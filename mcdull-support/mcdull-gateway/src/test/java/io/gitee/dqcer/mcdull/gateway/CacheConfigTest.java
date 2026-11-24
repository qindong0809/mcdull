package io.gitee.dqcer.mcdull.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CacheConfigTest {

    @Test
    void gatewayHasNoCaffeineCacheConfig() throws Exception {
        try (InputStream in = new ClassPathResource("application.yml").getInputStream()) {
            String yaml = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            assertFalse(yaml.contains("caffeine"));
            assertFalse(yaml.contains("spring.cache"));
        }
    }
}
