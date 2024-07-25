package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CacheQueryDTO;

/**
 * Cache service
 *
 * @author qcer
 * @since 2024/05/23
 */
public interface ICacheService {

    PagedVO<KeyValueVO<String, String>> cacheNames(CacheQueryDTO dto);

    void removeCache(Boolean caffeineCache, String name);

    Object cacheKey(Boolean caffeineCacheFlag, String cacheName);
}
