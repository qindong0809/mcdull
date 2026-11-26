package io.gitee.dqcer.mcdull.framework.spi.datasource;

/**
 * 租户数据源描述契约。
 * <p>
 * Provider: system 服务（按 tenantId 查 master 库，经缓存返回）。
 * Consumer: starter-mysql 的 DynamicDatasourceInterceptor。
 * <p>
 * 框架只消费本契约的四个字段，不关心数据从哪里来。
 *
 * @author dqcer
 */
public interface TenantDataSource {

    /** 路由 lookupKey，如 tenant_42 */
    String getKey();

    /** 租户 ID */
    Integer getTenantId();

    /** JDBC URL */
    String getUrl();

    /** 数据库账号 */
    String getUsername();

    /** 数据库密码 */
    String getPassword();
}
