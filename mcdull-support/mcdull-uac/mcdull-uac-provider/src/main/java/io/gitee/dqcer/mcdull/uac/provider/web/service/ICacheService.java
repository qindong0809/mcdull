package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;

/**
 * cache
 *
 * @author qcer
 * @since 2024/05/23
 */
public interface ICacheService {

    List<String> cacheNames();

    void removeCache(String cacheName);

    List<String> cacheKey(String cacheName);
}
