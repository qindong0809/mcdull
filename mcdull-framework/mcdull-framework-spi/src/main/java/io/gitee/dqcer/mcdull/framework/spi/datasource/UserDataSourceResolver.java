package io.gitee.dqcer.mcdull.framework.spi.datasource;

/**
 * 用户数据源解析器：根据用户身份解析其绑定的数据库。
 * <p>
 * Provider: system-facade 的 Feign 适配器（微服务形态）/ system-impl 本地实现（单体形态）。
 * Consumer: starter-mysql 的 UserDataSourceBindFilter。
 * <p>
 * 框架（starter-mysql）依赖本接口，实现由业务侧提供。依赖方向始终 framework ← SPI → 业务实现，
 * 框架不需要知道 system 的包名。未启用多租户时不提供实现，框架降级为只使用默认数据源。
 *
 * @author dqcer
 */
public interface UserDataSourceResolver {

    /**
     * 未配置任何实现时的空实现：返回 null，框架据此使用默认数据源。
     */
    UserDataSourceResolver NOOP = userId -> null;

    /**
     * 按用户 ID 解析其绑定的数据源。
     *
     * @param userId 已认证会话中的用户 ID（绝不可使用客户端请求参数）
     * @return 数据源描述；用户无绑定数据源时返回 null
     */
    UserDataSource resolve(String userId);
}
