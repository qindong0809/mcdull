package io.gitee.dqcer.mcdull.framework.external.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Credentials-based authentication (username + password → token).
 *
 * <p>This is the most common third-party integration pattern:
 * <pre>
 * Step 1 — POST /login  { "username": "xxx", "password": "yyy" }
 * Step 2 — Third party returns { "token": "eyJxxx..." }  (field name is configurable)
 * Step 3 — Cache the token in memory
 * Step 4 — Inject "Authorization: Bearer {token}" on every subsequent request
 * Step 5 — Auto-refresh when token is near expiry
 * </pre>
 *
 * <p>Example config:
 * <pre>
 * auth:
 *   type: credentials
 *   username: my-service-account
 *   password: my-password
 *   token-path: /api/login
 *   token-field: token          # which JSON field holds the token (default: token)
 *   token-ttl-ms: 3600000       # how long to cache (default: 30 min)
 * </pre>
 *
 * @author dqcer
 * @since 1.0.0
 */
public class CredentialsAuthStrategy implements AuthStrategy {

    private static final Logger log = LoggerFactory.getLogger(CredentialsAuthStrategy.class);
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    /** Refresh 1 minute before expiry */
    private static final long REFRESH_MARGIN_MS = 60_000L;

    private final String baseUrl;
    private final String tokenPath;
    private final String username;
    private final String password;
    private final Map<String, String> extraParams;
    private final TokenExtractor tokenExtractor;
    private final long tokenTtlMs;
    private final OkHttpClient authHttpClient;
    private final ObjectMapper objectMapper;

    /** Cached access token */
    private volatile String cachedToken;

    /** Absolute epoch ms — refresh when now > this value */
    private volatile long tokenRefreshAfter = 0;

    /**
     * Creates a credentials strategy with default 30-minute TTL.
     *
     * @param baseUrl       base URL of the external system
     * @param tokenPath     login endpoint path (e.g. "/api/login")
     * @param username      username / client_id
     * @param password      password / client_secret
     * @param tokenField    JSON field name in the response containing the token (e.g. "token", "access_token")
     * @param extraParams   additional request body fields (e.g. {"grant_type": "password"})
     * @param objectMapper  Jackson mapper
     */
    public CredentialsAuthStrategy(String baseUrl, String tokenPath,
                                    String username, String password,
                                    String tokenField, Map<String, String> extraParams,
                                    ObjectMapper objectMapper) {
        this(baseUrl, tokenPath, username, password,
                TokenExtractor.ofField(tokenField), extraParams,
                30 * 60 * 1000L, objectMapper);
    }

    /**
     * Full constructor with custom token TTL.
     */
    public CredentialsAuthStrategy(String baseUrl, String tokenPath,
                                    String username, String password,
                                    TokenExtractor tokenExtractor, Map<String, String> extraParams,
                                    long tokenTtlMs, ObjectMapper objectMapper) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.tokenPath = tokenPath;
        this.username = username;
        this.password = password;
        this.tokenExtractor = tokenExtractor;
        this.extraParams = extraParams != null ? extraParams : Collections.emptyMap();
        this.tokenTtlMs = tokenTtlMs;
        this.objectMapper = objectMapper;
        this.authHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public Map<String, String> getAuthHeaders() {
        return Collections.singletonMap("Authorization", "Bearer " + getToken());
    }

    private String getToken() {
        if (isTokenValid()) {
            return cachedToken;
        }
        synchronized (this) {
            if (isTokenValid()) {
                return cachedToken;
            }
            return fetchAndCacheToken();
        }
    }

    private boolean isTokenValid() {
        return cachedToken != null && System.currentTimeMillis() < tokenRefreshAfter;
    }

    /**
     * Sends username + password to the token endpoint and caches the result.
     */
    private String fetchAndCacheToken() {
        try {
            // Build request body: username + password + any extra params
            Map<String, String> bodyMap = new LinkedHashMap<>();
            bodyMap.put("username", username);
            bodyMap.put("password", password);
            bodyMap.putAll(extraParams);

            String requestBody = objectMapper.writeValueAsString(bodyMap);

            Request request = new Request.Builder()
                    .url(baseUrl + tokenPath)
                    .post(RequestBody.create(JSON_MEDIA_TYPE, requestBody))
                    .build();

            try (Response response = authHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Login endpoint returned HTTP " + response.code());
                }
                JsonNode body = objectMapper.readTree(response.body().string());
                String token = tokenExtractor.extract(body);
                if (token == null || token.isEmpty()) {
                    throw new RuntimeException("Token extractor returned empty token. Response: " + body);
                }

                cachedToken = token;
                tokenRefreshAfter = System.currentTimeMillis() + tokenTtlMs - REFRESH_MARGIN_MS;
                log.debug("[ExternalAuth] Token refreshed for username={}, ttl={}ms", username, tokenTtlMs);
                return cachedToken;
            }
        } catch (Exception e) {
            log.error("[ExternalAuth] Failed to fetch token for username={}", username, e);
            throw new RuntimeException("Failed to fetch external auth token for username=" + username, e);
        }
    }
}
