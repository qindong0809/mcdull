package io.gitee.dqcer.mcdull.system.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 口令体系配置。
 *
 * <p>aes-key 默认值与现有前端硬编码密钥一致，保证不改动前端时登录协议不变；
 * 生产环境应通过 MCDULL_PASSWORD_AES_KEY 覆盖，启动时会对默认值告警。
 */
@Data
@Component
@ConfigurationProperties(prefix = "mcdull.security.password")
public class PasswordProperties {

    /** 前端密码密文的 AES 密钥，必须 32 字节 */
    private String aesKey = "1024abcd1024abcd1024abcd1024abcd";

    /** 是否兼容存量 SHA1 口令，迁移完成后关闭 */
    private boolean legacyEnabled = true;
}
