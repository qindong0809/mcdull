package io.gitee.dqcer.mcdull.framework.external.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuration properties for external system clients.
 *
 * <pre>
 * mcdull:
 *   external:
 *     clients:
 *       prodigy:
 *         base-url: https://prodigy.example.com
 *         auth:
 *           type: jwt-account
 *           account-name: ctms
 *           secret-key: xxx
 *           token-path: /api/prodigy/auth/token
 * </pre>
 *
 * @author dqcer
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "mcdull.external")
public class ExternalClientProperties {

    /**
     * Map of client name → client configuration.
     */
    private Map<String, ClientConfig> clients = new LinkedHashMap<>();

    @Getter
    @Setter
    public static class ClientConfig {

        /** Base URL of the external system */
        private String baseUrl;

        /** Connect timeout in seconds */
        private int connectTimeout = 30;

        /** Read timeout in seconds */
        private int readTimeout = 120;

        /** Write timeout in seconds */
        private int writeTimeout = 60;

        /** Enable HTTP request/response logging */
        private boolean logEnabled = false;

        /** Retry configuration */
        private RetryConfig retry = new RetryConfig();

        /** Authentication configuration */
        private AuthConfig auth = new AuthConfig();
    }

    @Getter
    @Setter
    public static class RetryConfig {
        /** Maximum number of retry attempts (0 = no retry) */
        private int maxAttempts = 0;

        /** Backoff interval in milliseconds */
        private long backoffMs = 2000;
    }

    @Getter
    @Setter
    public static class AuthConfig {
        /**
         * Authentication type: none, jwt-account, credentials, api-key
         */
        private String type = "none";

        /** Account name (for jwt-account) */
        private String accountName;

        /** Secret key (for jwt-account) */
        private String secretKey;

        /** Token endpoint path (for jwt-account and credentials) */
        private String tokenPath = "/api/auth/token";

        /** JSON field name containing the token in the response (default: payload) */
        private String tokenField = "payload";

        /** Username (for credentials type) */
        private String username;

        /** Password (for credentials type) */
        private String password;

        /** Request body extra fields (for credentials type, e.g. grant_type) */
        private java.util.Map<String, String> extraParams = new java.util.LinkedHashMap<>();

        /** API key value (for api-key) */
        private String apiKey;

        /** API key header name (for api-key) */
        private String headerName = "X-API-Key";
    }
}
