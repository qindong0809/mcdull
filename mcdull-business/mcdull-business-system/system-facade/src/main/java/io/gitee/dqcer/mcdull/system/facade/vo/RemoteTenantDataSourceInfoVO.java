package io.gitee.dqcer.mcdull.system.facade.vo;

import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceInfo;
import lombok.Data;

/**
 * 租户数据源远程 VO。
 *
 * @author dqcer
 */
@Data
public class RemoteTenantDataSourceInfoVO implements TenantDataSourceInfo {

    private String key;

    private Integer tenantId;

    private String url;

    private String username;

    /** AES+Base64 密文；缓存与 Feign 传输中始终是密文，由 resolver 使用前解密 */
    private String password;
}
