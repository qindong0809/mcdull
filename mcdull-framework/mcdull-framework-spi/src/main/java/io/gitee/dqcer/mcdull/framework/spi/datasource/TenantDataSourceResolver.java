package io.gitee.dqcer.mcdull.framework.spi.datasource;

import java.util.Set;

/**
 * 租户数据源解析 SPI。
 * <p>
 * Provider: 业务侧（system-facade 的 Feign 适配器），实现"授权校验 + 数据源解析"：
 * <ol>
 *     <li>{@link #getAuthorizedTenantIds(String)}：取用户被授权的租户集合（缓存 + 登录时写入）；</li>
 *     <li>{@link #resolve(Integer)}：按 tenantId 解析数据源（缓存未命中则经 system-feign 查 master 库）。</li>
 * </ol>
 * Consumer: starter-mysql 的 DynamicDatasourceInterceptor。
 * <p>
 * 未配置实现时返回 NOOP，框架据此不做租户级切库，沿用 master。
 *
 * @author dqcer
 */
public interface TenantDataSourceResolver {

    /** 未配置实现时的空实现：授权集合为空、解析返回 null */
    TenantDataSourceResolver NOOP = new TenantDataSourceResolver() {
        @Override
        public Set<Integer> getAuthorizedTenantIds(String userId) {
            return Set.of();
        }

        @Override
        public TenantDataSourceInfo resolve(Integer tenantId) {
            return null;
        }
    };

    /**
     * 取用户被授权的租户 ID 集合。
     *
     * @param userId 已认证用户 ID（来自服务端会话，非请求头）
     * @return 授权租户集合；无数据返回空集合
     */
    Set<Integer> getAuthorizedTenantIds(String userId);

    /**
     * 按租户 ID 解析数据源。
     *
     * @param tenantId 已通过授权校验的租户 ID
     * @return 租户数据源；无独立数据源时返回 null
     */
    TenantDataSourceInfo resolve(Integer tenantId);
}
