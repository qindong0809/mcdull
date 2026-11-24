package io.gitee.dqcer.mcdull.system.provider.config.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CacheConstantsTest {

    @Test
    void businessCacheNameIsRedisSemantic() {
        assertEquals("businessCache", CacheConstants.BUSINESS_CACHE);
        assertFalse(CacheConstants.BUSINESS_CACHE.toLowerCase().contains("caffeine"));
    }
}
