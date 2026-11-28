package io.gitee.dqcer.mcdull.framework.mysql.interceptor;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.Database;
import io.gitee.dqcer.mcdull.framework.mysql.config.DataSourceBuilder;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.config.RoutingDataSource;
import io.gitee.dqcer.mcdull.framework.mysql.properties.DataSourceProperties;
import io.gitee.dqcer.mcdull.framework.mysql.routing.SwitchableDataSource;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceInfo;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

/**
 * 租户数据源拦截器：仅按 tenantId 切库的唯一切库点。
 * <p>
 * 决策顺序：
 * <ol>
 *     <li>本地配置了 master 数据源（system 服务，含登录接口）→ 用 master，不切租户库；</li>
 *     <li>取请求头 tid；空 → {@code system.tenant.required}；</li>
 *     <li>校验 tid ∈ 服务端会话的授权租户集合，否则 {@code system.tenant.forbidden}；</li>
 *     <li>经 SPI 解析数据源（缓存未命中则 system-feign 查 master 库）；</li>
 *     <li>注册物理池并入栈，按 key 精确出栈。</li>
 * </ol>
 * 身份只取服务端会话，请求头 uid 不可信、tid 仅表示用户当前选择。
 *
 * @author dqcer
 * @since 2022/01/11
 */
public class DynamicDatasourceInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DynamicDatasourceInterceptor.class);

    private static final String ATTR_TENANT_KEY = DynamicDatasourceInterceptor.class.getName() + ".TENANT_KEY";

    private final SwitchableDataSource switchableDataSource;
    private final DataSourceProperties properties;
    private final ObjectProvider<TenantDataSourceResolver> resolverProvider;

    public DynamicDatasourceInterceptor(SwitchableDataSource switchableDataSource,
                                        DataSourceProperties properties,
                                        ObjectProvider<TenantDataSourceResolver> resolverProvider) {
        this.switchableDataSource = switchableDataSource;
        this.properties = properties;
        this.resolverProvider = resolverProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // ① 本地配置了 master（system 服务、登录接口等）：用 master，不切租户库
        String masterUrl = properties.getUrl();
        if (masterUrl != null && !masterUrl.isBlank()) {
            return true;
        }

        // ② 取用户当前选择的租户
        String headerTenant = request.getHeader(HttpHeaderConstants.T_ID);
        if (headerTenant == null || headerTenant.isBlank()) {
            throw new BusinessException("system.tenant.required");
        }
        Integer tenantId;
        try {
            tenantId = Integer.valueOf(headerTenant);
        } catch (NumberFormatException ex) {
            throw new BusinessException("system.tenant.required");
        }

        // ③ 授权校验：tid 必须属于服务端会话中的授权租户集合
        TenantDataSourceResolver resolver = resolverProvider.orderedStream().findFirst()
                .orElse(TenantDataSourceResolver.NOOP);
        UnifySession session = UserContextHolder.getSession();
        if (session == null || session.getUserId() == null) {
            throw new BusinessException("system.tenant.forbidden");
        }
        Set<Integer> authorizedTenants = session.getTenantIds();
        if (authorizedTenants == null || authorizedTenants.isEmpty()) {
            authorizedTenants = resolver.getAuthorizedTenantIds(session.getUserId());
        }
        if (authorizedTenants == null || !authorizedTenants.contains(tenantId)) {
            LogHelp.warn(log, "租户越权访问. userId={}, tid={}", session.getUserId(), tenantId);
            throw new BusinessException("system.tenant.forbidden");
        }

        // ④ 解析数据源（SPI 内部：缓存命中直接返回，未命中走 system-feign）
        TenantDataSourceInfo tenantDataSourceInfo = resolver.resolve(tenantId);
        if (tenantDataSourceInfo == null || tenantDataSourceInfo.getKey() == null || tenantDataSourceInfo.getUrl() == null) {
            LogHelp.debug(log, "租户 {} 无独立数据源，沿用当前数据源", tenantId);
            return true;
        }

        if (!switchableDataSource.containsDataSource(tenantDataSourceInfo.getKey())
                && switchableDataSource.dataSourceCount() >= RoutingDataSource.MAX_DATASOURCE_COUNT) {
            LogHelp.warn(log, "数据源数量超过上限 {}，拒绝注册 key={}",
                    RoutingDataSource.MAX_DATASOURCE_COUNT, tenantDataSourceInfo.getKey());
            throw new BusinessException("system.datasource.limit");
        }

        // ⑤ 注册物理池并入栈
        registerIfAbsent(tenantDataSourceInfo);
        DynamicContextHolder.push(tenantDataSourceInfo.getKey());
        request.setAttribute(ATTR_TENANT_KEY, tenantDataSourceInfo.getKey());
        LogHelp.debug(log, "租户 {} 绑定数据源 {}，当前路由 {}",
                tenantId, tenantDataSourceInfo.getKey(), DynamicContextHolder.peek());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object key = request.getAttribute(ATTR_TENANT_KEY);
        if (key instanceof String lookupKey) {
            DynamicContextHolder.remove(lookupKey);
            request.removeAttribute(ATTR_TENANT_KEY);
        }
    }

    private void registerIfAbsent(TenantDataSourceInfo ds) {
        if (switchableDataSource.containsDataSource(ds.getKey())) {
            return;
        }
        Database database = new Database();
        database.setUrl(ds.getUrl());
        database.setUsername(ds.getUsername());
        database.setPassword(ds.getPassword());
        switchableDataSource.addDataSource(ds.getKey(), DataSourceBuilder.convertDruid(properties, database));
    }
}
