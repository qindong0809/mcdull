package io.gitee.dqcer.mcdull.framework.external.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JWT service-account authentication.
 *
 * <p>Token lifecycle (fully transparent to business code):
 * <pre>
 * Step 1 — Sign a short-lived JWT with the secret key (proves identity)
 * Step 2 — POST to token endpoint: {accountName, accountSecret: jwt}
 * Step 3 — Cache the returned access token (default 4 min)
 * Step 4 — Inject "Authorization: {accountName} {token}" on every business request
 * Step 5 — Auto-refresh when token is within REFRESH_MARGIN_MS of expiry
 * </pre>
 *
 * @author dqcer
 * @since 1.0.0
 */
public class JwtAccountAuthStrategy implements AuthStrategy {

    private static final Logger log = LoggerFactory.getLogger(JwtAccountAuthStrategy.class);
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    /** Refresh the token this many ms before it actually expires */
    private static final long REFRESH_MARGIN_MS = 60_000L;

    /** How long we cache the token (ms). Should be less than real token TTL. */
    private static final long TOKEN_CACHE_TTL_MS = 4 * 60 * 1000L;

    /** TTL of the credential JWT we sign ourselves (ms) */
    private static final long CREDENTIAL_JWT_TTL_MS = 5 * 60 * 1000L;

    private final String baseUrl;
    private final String tokenPath;
    private final String accountName;
    private final String secretKey;
    private final TokenExtractor tokenExtractor;
    private final OkHttpClient authHttpClient;
    private final ObjectMapper objectMapper;

    /** Cached access token — volatile for visibility across threads */
    private volatile String cachedToken;

    /** Absolute time (epoch ms) after which the cached token must be refreshed */
    private volatile long tokenRefreshAfter = 0;

    /**
     * Creates a strategy with the default token extractor (reads the {@code payload} field).
     */
    public JwtAccountAuthStrategy(String baseUrl, String tokenPath, String accountName,
                                   String secretKey, ObjectMapper objectMapper) {
        this(baseUrl, tokenPath, accountName, secretKey,
                TokenExtractor.ofField("payload"), objectMapper);
    }

    /**
     * Creates a strategy with a custom token extractor.
     */
    public JwtAccountAuthStrategy(String baseUrl, String tokenPath, String accountName,
                                   String secretKey, TokenExtractor tokenExtractor, ObjectMapper objectMapper) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.tokenPath = tokenPath;
        this.accountName = accountName;
        this.secretKey = secretKey;
        this.tokenExtractor = tokenExtractor;
        this.objectMapper = objectMapper;
        // Dedicated client for auth requests only — no retry/auth interceptors
        this.authHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public Map<String, String> getAuthHeaders() {
        return Collections.singletonMap("Authorization", accountName + " " + getToken());
    }

    /**
     * Returns a valid access token.
     * Uses double-checked locking to ensure only one thread fetches a new token.
     */
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
     * Fetches a fresh access token from the external system:
     * 1. Sign a short-lived JWT with the secret key
     * 2. POST to the token endpoint
     * 3. Extract and cache the token
     */
    private String fetchAndCacheToken() {
        try {
            String credentialJwt = signCredentialJwt();

            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "accountName", accountName,
                    "accountSecret", credentialJwt
            ));

            Request request = new Request.Builder()
                    .url(baseUrl + tokenPath)
                    .post(RequestBody.create(JSON_MEDIA_TYPE, requestBody))
                    .build();

            try (Response response = authHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Token endpoint returned HTTP " + response.code());
                }
                JsonNode body = objectMapper.readTree(response.body().string());
                String token = tokenExtractor.extract(body);
                if (token == null || token.isEmpty()) {
                    throw new RuntimeException("Token extractor returned empty token. Response: " + body);
                }

                cachedToken = token;
                // Schedule refresh before actual expiry to avoid window of expired requests
                tokenRefreshAfter = System.currentTimeMillis() + TOKEN_CACHE_TTL_MS - REFRESH_MARGIN_MS;
                log.debug("[ExternalAuth] Token refreshed for account={}, refreshAfter={}ms from now",
                        accountName, TOKEN_CACHE_TTL_MS - REFRESH_MARGIN_MS);
                return cachedToken;
            }
        } catch (Exception e) {
            log.error("[ExternalAuth] Failed to fetch token for account={}", accountName, e);
            throw new RuntimeException("Failed to fetch external auth token for account=" + accountName, e);
        }
    }

    /**
     * Signs a short-lived JWT using the secret key.
     * This JWT is sent as the credential to the token endpoint.
     */
    private String signCredentialJwt() {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claim("account", accountName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + CREDENTIAL_JWT_TTL_MS))
                .signWith(key)
                .compact();
    }
}
