package io.gitee.dqcer.mcdull.system.facade.service.adapter;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.redis.cache.ICache;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceInfo;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceCipherResolver;
import io.gitee.dqcer.mcdull.framework.spi.datasource.TenantDataSourceResolver;
import io.gitee.dqcer.mcdull.system.facade.service.TenantDataSourceApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteTenantDataSourceInfoVO;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 租户数据源 SPI 适配器：缓存 + system-feign 回源。
 * <p>
 * system 服务不可用或接口未实现时，捕获异常后返回空/ null，框架据此降级为不切租户库。
 *
 * @author dqcer
 */
@Component
public class TenantDataSourceFeignResolver implements TenantDataSourceResolver {

    private static final Logger log = LoggerFactory.getLogger(TenantDataSourceFeignResolver.class);

    /** 租户数据源缓存 key 前缀 */
    private static final String CACHE_TENANT_DS = "tenant:ds:";

    /** 用户授权租户集合缓存 key 前缀 */
    private static final String CACHE_USER_TENANTS = "tenant:user:";

    /** 缓存过期时间（秒） */
    private static final long CACHE_EXPIRE = 300L;

    @Resource
    private TenantDataSourceApi tenantDataSourceApi;

    @Resource
    private ICache cache;

    @Resource
    private TenantDataSourceCipherResolver cipherResolver;

    @Override
    public Set<Integer> getAuthorizedTenantIds(String userId) {
        if (userId == null || userId.isBlank()) {
            return Set.of();
        }
        List<Integer> ids = cache.getListOrSetPrefixed(CACHE_USER_TENANTS + userId, Integer.class,
                () -> {
                    try {
                        InnerResult<Set<Integer>> result = tenantDataSourceApi.getAuthorizedTenantIds(userId);
                        Set<Integer> set = result.unwrapOrNull();
                        return set == null ? Collections.emptyList() : List.copyOf(set);
                    } catch (Exception ex) {
                        LogHelp.warn(log, "解析授权租户集合失败，降级为空. userId={}, cause={}", userId, ex.getMessage());
                        return Collections.emptyList();
                    }
                }, CACHE_EXPIRE, false);
        return ids == null ? Set.of() : ids.stream().collect(Collectors.toSet());
    }

    @Override
    public TenantDataSourceInfo resolve(Integer tenantId) {
        if (tenantId == null || tenantId <= 0) {
            return null;
        }
        RemoteTenantDataSourceInfoVO cached = cache.getOrSetPrefixed(CACHE_TENANT_DS + tenantId,
                RemoteTenantDataSourceInfoVO.class,
                () -> {
                    try {
                        InnerResult<RemoteTenantDataSourceInfoVO> result = tenantDataSourceApi.getByTenantId(tenantId);
                        return result.unwrapOrNull();
                    } catch (Exception ex) {
                        LogHelp.warn(log, "解析租户数据源失败，降级为不切库. tenantId={}, cause={}", tenantId, ex.getMessage());
                        return null;
                    }
                }, CACHE_EXPIRE);
        return decrypt(cached, tenantId);
    }

    /**
     * 复制缓存对象后解密，避免改写到缓存中的密文对象。
     */
    private TenantDataSourceInfo decrypt(RemoteTenantDataSourceInfoVO cached, Integer tenantId) {
        if (cached == null) {
            return null;
        }
        RemoteTenantDataSourceInfoVO vo = new RemoteTenantDataSourceInfoVO();
        vo.setKey(cached.getKey());
        vo.setTenantId(cached.getTenantId());
        vo.setUrl(cached.getUrl());
        vo.setUsername(cached.getUsername());
        try {
            vo.setPassword(cipherResolver.decrypt(cached.getPassword()));
        } catch (Exception ex) {
            LogHelp.warn(log, "租户数据源密码解密失败，降级为不切库. tenantId={}, cause={}", tenantId, ex.getMessage());
            return null;
        }
        return vo;
    }
}
