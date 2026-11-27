package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceCipherResolver;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 数据源密码加解密入口。
 *
 * <p>具体算法由 SPI {@link TenantDataSourceCipherResolver} 统一实现，
 * 保证 system 服务写入的密文与各业务服务 resolver 读出的解密结果一致。</p>
 *
 * @author dqcer
 */
@Component
public class DatasourceCryptoManager {

    @Resource
    private TenantDataSourceCipherResolver cipherResolver;

    public String encrypt(String plain) {
        try {
            return cipherResolver.encrypt(plain);
        } catch (Exception ex) {
            throw new BusinessException("system.datasource.aes.key.invalid");
        }
    }

    public String decrypt(String cipher) {
        try {
            return cipherResolver.decrypt(cipher);
        } catch (Exception ex) {
            throw new BusinessException("system.datasource.aes.key.invalid");
        }
    }
}
