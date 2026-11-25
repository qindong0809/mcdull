package io.gitee.dqcer.mcdull.framework.mysql.service;

import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.mysql.Database;
import io.gitee.dqcer.mcdull.framework.mysql.config.DataSourceBuilder;
import io.gitee.dqcer.mcdull.framework.mysql.properties.DataSourceProperties;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.routing.SwitchableDataSource;
import io.gitee.dqcer.mcdull.framework.spi.datasource.UserDataSource;
import io.gitee.dqcer.mcdull.framework.spi.datasource.UserDataSourceResolver;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户绑定数据源的运行时装配：按已认证用户解析数据源、校验请求头租户、注册物理数据源并入栈。
 * <p>
 * 本类只做数据源领域的事，不侵入鉴权模块。调用方（如 UserDataSourceBindFilter）从会话取到
 * 已认证的用户 ID 后传入，绝不使用客户端请求参数作为身份依据。
 * <p>
 * 设计原则：
 * <ol>
 *     <li>用户 ID 由调用方从服务端会话取得，本类不信任任何外部传参；</li>
 *     <li>请求头 tid 仅作一致性校验，不参与选库——防止客户端伪造 tid 越权访问他人库；</li>
 *     <li>解析结果缓存，避免每个请求都 Feign 调用 system；</li>
 *     <li>system 不可用或用户未绑定时降级到默认数据源，不阻断业务；</li>
 *     <li>数据源注册上限保护，防止构造大量 userId 打爆连接池。</li>
 * </ol>
 *
 * @author dqcer
 */
@Component
public class UserDataSourceBindingService {

    private static final Logger log = LoggerFactory.getLogger(UserDataSourceBindingService.class);

    /** 数据源注册上限：租户数超过此值时拒绝新建，防止连接池被撑爆 */
    private static final int MAX_DATASOURCE_COUNT = 100;

    @Resource
    private ObjectProvider<UserDataSourceResolver> resolverProvider;

    @Resource
    private SwitchableDataSource switchableDataSource;

    @Resource
    private DataSourceProperties dataSourceProperties;

    /** userId -> 解析结果；null 结果用 NO_DATASOURCE 占位避免反复 Feign 调用 */
    private static final UserDataSource NO_DATASOURCE = new NoDataSource();

    private final Map<String, UserDataSource> resolvedCache = new ConcurrentHashMap<>();

    /** 无绑定数据源的哨兵实现：所有字段返回 null，接口语义为「无需切库」 */
    private static final class NoDataSource implements UserDataSource {
        @Override
        public String getKey() {
            return null;
        }

        @Override
        public String getTenantId() {
            return null;
        }

        @Override
        public String getUrl() {
            return null;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }
    }

    /**
     * 为当前请求绑定用户数据源。应在会话加载完成后调用。
     *
     * @param userId          已认证会话中的用户 ID（由调用方从 UserContextHolder 取得）
     * @param requestTenantId 请求头 tid，可为 null（未传时跳过一致性校验）
     */
    public void bind(String userId, String requestTenantId) {
        if (userId == null || userId.isBlank() || "0".equals(userId)) {
            return;
        }
        UserDataSourceResolver resolver = resolverProvider.getIfAvailable(() -> UserDataSourceResolver.NOOP);
        if (resolver == UserDataSourceResolver.NOOP) {
            return;
        }

        UserDataSource ds = resolvedCache.computeIfAbsent(userId,
                id -> {
                    UserDataSource resolved = resolver.resolve(id);
                    return resolved == null ? NO_DATASOURCE : resolved;
                });
        if (ds == NO_DATASOURCE || ds.getKey() == null || ds.getUrl() == null) {
            LogHelp.debug(log, "用户 {} 无绑定数据源，使用默认数据源", userId);
            return;
        }

        // 请求头租户仅校验一致性，不参与选库
        if (requestTenantId != null && !requestTenantId.isBlank()
                && ds.getTenantId() != null && !ds.getTenantId().equals(requestTenantId)) {
            LogHelp.warn(log, "租户校验失败. userId={}, headerTid={}, boundTenant={}",
                    userId, requestTenantId, ds.getTenantId());
            throw new BusinessException("system.tenant.mismatch");
        }

        if (!switchableDataSource.containsDataSource(ds.getKey())
                && switchableDataSource.dataSourceCount() >= MAX_DATASOURCE_COUNT) {
            LogHelp.warn(log, "数据源数量超过上限 {}，拒绝注册 key={}", MAX_DATASOURCE_COUNT, ds.getKey());
            throw new BusinessException("system.datasource.limit");
        }

        registerIfAbsent(ds);
        DynamicContextHolder.push(ds.getKey());
        LogHelp.debug(log, "用户 {} 绑定数据源 {}，当前路由 {}", userId, ds.getKey(), DynamicContextHolder.peek());
    }

    /** 使缓存失效，供管理端在用户数据源变更后调用 */
    public void evict(String userId) {
        resolvedCache.remove(userId);
    }

    private void registerIfAbsent(UserDataSource ds) {
        if (switchableDataSource.containsDataSource(ds.getKey())) {
            return;
        }
        Database database = new Database();
        database.setUrl(ds.getUrl());
        database.setUsername(ds.getUsername());
        database.setPassword(ds.getPassword());
        switchableDataSource.addDataSource(ds.getKey(),
                DataSourceBuilder.convertDruid(dataSourceProperties, database));
    }
}
