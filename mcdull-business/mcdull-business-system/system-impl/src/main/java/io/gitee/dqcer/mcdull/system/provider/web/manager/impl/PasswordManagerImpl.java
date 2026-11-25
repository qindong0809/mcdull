package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.security.PasswordUtil;
import io.gitee.dqcer.mcdull.system.provider.config.PasswordProperties;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IPasswordManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 口令 Manager：前端 AES 协议不变，存储由 SHA1 平滑迁移到 BCrypt。
 *
 * <p>BCrypt 值不带前缀，直接以 $2a$/$2b$ 开头，与 administrator_user 保持一致，
 * 通过格式正则区分存量 SHA1。
 */
@Slf4j
@Service
public class PasswordManagerImpl implements IPasswordManager {

    private static final String DEFAULT_AES_KEY = "1024abcd1024abcd1024abcd1024abcd";
    private static final String BCRYPT_PREFIX_REGEX = "^\\$2[aby]\\$\\d{2}\\$.{53}$";
    private static final String SHA1_REGEX = "^[0-9a-fA-F]{40}$";

    @Resource
    private PasswordProperties properties;

    @PostConstruct
    public void init() {
        String key = properties.getAesKey();
        if (StrUtil.isBlank(key) || key.getBytes(StandardCharsets.UTF_8).length != 32) {
            throw new IllegalStateException("mcdull.security.password.aes-key must be 32 bytes");
        }
        if (DEFAULT_AES_KEY.equals(key)) {
            LogHelp.warn(log, "mcdull.security.password.aes-key is using the built-in default value; "
                    + "set MCDULL_PASSWORD_AES_KEY in production after coordinating with the front end");
        }
    }

    @Override
    public String decrypt(String webPassword) {
        if (StrUtil.isBlank(webPassword)) {
            return StrUtil.EMPTY;
        }
        try {
            byte[] base64Decode = Base64.getDecoder().decode(webPassword);
            AES aes = new AES(properties.getAesKey().getBytes(StandardCharsets.UTF_8));
            return new String(aes.decrypt(base64Decode), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LogHelp.error(log, "密码解密失败", e);
            return StrUtil.EMPTY;
        }
    }

    @Override
    public String encode(String rawPassword) {
        return PasswordUtil.encode(rawPassword);
    }

    @Override
    public String encodeFromWeb(String webPassword) {
        String raw = this.decrypt(webPassword);
        if (StrUtil.isEmpty(raw)) {
            throw new BusinessException("user.password.incorrect");
        }
        return this.encode(raw);
    }

    @Override
    public boolean matches(String webPassword, String stored) {
        if (StrUtil.isBlank(stored)) {
            return false;
        }
        String raw = this.decrypt(webPassword);
        if (StrUtil.isEmpty(raw)) {
            return false;
        }
        if (stored.matches(BCRYPT_PREFIX_REGEX)) {
            return PasswordUtil.matches(raw, stored);
        }
        if (properties.isLegacyEnabled() && stored.matches(SHA1_REGEX)) {
            String sha1 = Sha1Util.getSha1(raw);
            return StrUtil.isNotBlank(sha1)
                    && MessageDigest.isEqual(stored.getBytes(StandardCharsets.UTF_8),
                    sha1.getBytes(StandardCharsets.UTF_8));
        }
        LogHelp.error(log, "未知口令格式，拒绝校验");
        return false;
    }

    @Override
    public boolean needsUpgrade(String stored) {
        return properties.isLegacyEnabled()
                && StrUtil.isNotBlank(stored)
                && stored.matches(SHA1_REGEX);
    }
}
