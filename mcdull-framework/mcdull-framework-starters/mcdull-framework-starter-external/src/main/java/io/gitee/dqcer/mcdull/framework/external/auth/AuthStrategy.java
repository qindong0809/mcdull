package io.gitee.dqcer.mcdull.framework.external.auth;

import java.util.Map;

/**
 * Strategy interface for external system authentication.
 * Implementations provide the appropriate HTTP headers for each request.
 *
 * @author dqcer
 * @since 1.0.0
 */
public interface AuthStrategy {

    /**
     * Returns the authentication headers to be injected into each outgoing request.
     * Implementations are responsible for token caching and refresh.
     *
     * @return header name-value pairs (e.g., {"Authorization": "Bearer xxx"})
     */
    Map<String, String> getAuthHeaders();
}
