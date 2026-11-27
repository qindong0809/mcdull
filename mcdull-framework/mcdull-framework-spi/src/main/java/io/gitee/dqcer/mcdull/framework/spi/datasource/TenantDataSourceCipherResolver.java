package io.gitee.dqcer.mcdull.framework.spi.datasource;

/**
 * 租户数据源密码加解密契约。
 *
 * <p>缓存中只保留密文；resolver 从缓存读出后、构造 {@link TenantDataSource} 返回前调用本契约解密，
 * 避免明文进入 Redis 或 Feign 缓存。</p>
 *
 * @author dqcer
 */
public interface TenantDataSourceCipherResolver {

    /**
     * 加密明文。
     *
     * @param plain 明文
     * @return Base64 密文
     */
    String encrypt(String plain);

    /**
     * 解密密文。
     *
     * @param cipher Base64 密文
     * @return 明文
     */
    String decrypt(String cipher);
}
