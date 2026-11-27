package io.gitee.dqcer.mcdull.system.facade.service.adapter;

import cn.hutool.crypto.symmetric.AES;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceCipherResolver;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 数据源密码 AES 加解密实现。
 *
 * <p>算法与 system 服务原有口令加密保持一致：AES/ECB/PKCS5Padding + Base64。
 * 密钥由 {@code mcdull.security.datasource.aes-key} 注入，不硬编码在代码中。</p>
 *
 * @author dqcer
 */
@Component
public class AesTenantDataSourceCipherResolver implements TenantDataSourceCipherResolver {

    private static final Logger log = LoggerFactory.getLogger(AesTenantDataSourceCipherResolver.class);

    private final String configuredKey;

    private AES aes;

    public AesTenantDataSourceCipherResolver(
            @Value("${mcdull.security.datasource.aes-key:}") String configuredKey) {
        this.configuredKey = configuredKey;
    }

    @PostConstruct
    public void init() {
        if (configuredKey == null || configuredKey.getBytes(StandardCharsets.UTF_8).length != 32) {
            log.warn("mcdull.security.datasource.aes-key 未配置或不是32字节，租户数据源加解密不可用");
            return;
        }
        this.aes = new AES(configuredKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String encrypt(String plain) {
        checkAvailable();
        return Base64.getEncoder().encodeToString(aes.encrypt(plain.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public String decrypt(String cipher) {
        checkAvailable();
        return new String(aes.decrypt(Base64.getDecoder().decode(cipher)), StandardCharsets.UTF_8);
    }

    private void checkAvailable() {
        if (aes == null) {
            throw new IllegalStateException("mcdull.security.datasource.aes-key is missing or invalid");
        }
    }
}
