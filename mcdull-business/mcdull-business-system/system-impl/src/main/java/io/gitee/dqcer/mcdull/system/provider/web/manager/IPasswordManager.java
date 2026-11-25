package io.gitee.dqcer.mcdull.system.provider.web.manager;

/**
 * 口令编解码与校验契约。
 *
 * <p>前端协议保持不变：仍传 Base64(AES(明文))；存储格式由无盐 SHA1 平滑迁移到 BCrypt。
 */
public interface IPasswordManager {

    /** 前端密文解密为明文。解密失败返回空串，调用方必须判空。 */
    String decrypt(String webPassword);

    /** 明文编码为 BCrypt 存储值，用于默认密码/明文入口。 */
    String encode(String rawPassword);

    /** 前端密文编码为 BCrypt 存储值，用于改密/重置等前端入口。 */
    String encodeFromWeb(String webPassword);

    /** 前端密文与存储值比对，兼容 SHA1 与 BCrypt 双格式。 */
    boolean matches(String webPassword, String stored);

    /** 存储值是否为需要升级的存量 SHA1。 */
    boolean needsUpgrade(String stored);
}
