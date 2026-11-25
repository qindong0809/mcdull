package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.crypto.symmetric.AES;
import io.gitee.dqcer.mcdull.framework.security.PasswordUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.system.provider.config.PasswordProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordManagerImplTest {

    private static final String RAW = "mcdull@2026";

    private PasswordManagerImpl manager;
    private PasswordProperties properties;

    @BeforeEach
    void setUp() {
        properties = new PasswordProperties();
        manager = new PasswordManagerImpl();
        ReflectionTestUtils.setField(manager, "properties", properties);
    }

    @Test
    void webProtocolIsBase64AesAndUnchanged() {
        String web = webPassword(RAW, properties.getAesKey());
        assertEquals(RAW, manager.decrypt(web));
        assertEquals("", manager.decrypt("not-base64"));
    }

    @Test
    void matchesLegacySha1() {
        String web = webPassword(RAW, properties.getAesKey());
        assertTrue(manager.matches(web, Sha1Util.getSha1(RAW)));
        assertFalse(manager.matches(web, Sha1Util.getSha1("another")));
    }

    @Test
    void matchesBcrypt() {
        String web = webPassword(RAW, properties.getAesKey());
        assertTrue(manager.matches(web, PasswordUtil.encode(RAW)));
        assertFalse(manager.matches(web, PasswordUtil.encode("another")));
    }

    @Test
    void rejectLegacyWhenDisabled() {
        properties.setLegacyEnabled(false);
        String web = webPassword(RAW, properties.getAesKey());
        assertFalse(manager.matches(web, Sha1Util.getSha1(RAW)));
    }

    @Test
    void rejectUnknownFormat() {
        String web = webPassword(RAW, properties.getAesKey());
        assertFalse(manager.matches(web, "unknown-format-value"));
    }

    @Test
    void needsUpgradeOnlyForLegacy() {
        assertTrue(manager.needsUpgrade(Sha1Util.getSha1(RAW)));
        assertFalse(manager.needsUpgrade(PasswordUtil.encode(RAW)));
        properties.setLegacyEnabled(false);
        assertFalse(manager.needsUpgrade(Sha1Util.getSha1(RAW)));
    }

    @Test
    void encodeAndEncodeFromWebProduceBcrypt() {
        String encoded = manager.encode(RAW);
        assertTrue(encoded.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$"));
        assertTrue(PasswordUtil.matches(RAW, encoded));

        String web = webPassword(RAW, properties.getAesKey());
        String fromWeb = manager.encodeFromWeb(web);
        assertNotEquals(fromWeb, web);
        assertTrue(PasswordUtil.matches(RAW, fromWeb));
    }

    private String webPassword(String raw, String key) {
        byte[] encrypted = new AES(key.getBytes(StandardCharsets.UTF_8))
                .encrypt(raw.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }
}
