package io.gitee.dqcer.mcdull.system.provider.web.manager;

import cn.hutool.crypto.symmetric.AES;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.system.provider.config.DatasourceProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class DatasourceCryptoManager {

    private static final Logger logger = LoggerFactory.getLogger(DatasourceCryptoManager.class);

    @Resource
    private DatasourceProperties properties;

    private AES aes;

    @PostConstruct
    public void init() {
        String key = properties.getAesKey();
        if (key == null || key.getBytes(StandardCharsets.UTF_8).length != 32) {
            logger.warn("mcdull.security.datasource.aes-key 未配置或不是32字节，数据源密码功能不可用");
            return;
        }
        this.aes = new AES(key.getBytes(StandardCharsets.UTF_8));
    }

    public String encrypt(String plain) {
        checkAvailable();
        return Base64.getEncoder().encodeToString(aes.encrypt(plain.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String cipher) {
        checkAvailable();
        return new String(aes.decrypt(Base64.getDecoder().decode(cipher)), StandardCharsets.UTF_8);
    }

    private void checkAvailable() {
        if (aes == null) {
            throw new BusinessException("system.datasource.aes.key.invalid");
        }
    }
}
