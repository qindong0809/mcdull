package io.gitee.dqcer.mcdull.framework.external.auth;

import java.util.Collections;
import java.util.Map;

/**
 * Static API Key authentication. Injects a fixed header on every request.
 *
 * @author dqcer
 * @since 1.0.0
 */
public class ApiKeyAuthStrategy implements AuthStrategy {

    private final String headerName;
    private final String apiKey;

    public ApiKeyAuthStrategy(String headerName, String apiKey) {
        this.headerName = headerName;
        this.apiKey = apiKey;
    }

    @Override
    public Map<String, String> getAuthHeaders() {
        return Collections.singletonMap(headerName, apiKey);
    }
}
