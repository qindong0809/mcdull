package io.gitee.dqcer.mcdull.framework.external.auth;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Strategy for extracting the access token from the auth response body.
 * Different external systems return tokens in different JSON structures.
 *
 * <p>Examples:
 * <ul>
 *   <li>{@code {"payload": "xxx"}} → use field {@code payload}</li>
 *   <li>{@code {"access_token": "xxx"}} → use field {@code access_token}</li>
 *   <li>{@code {"data": {"token": "xxx"}}} → use nested path {@code data.token}</li>
 * </ul>
 *
 * @author dqcer
 * @since 1.0.0
 */
@FunctionalInterface
public interface TokenExtractor {

    /**
     * Extract the access token string from the parsed JSON response.
     *
     * @param responseBody parsed JSON body of the token endpoint response
     * @return access token string
     */
    String extract(JsonNode responseBody);

    /** Default: reads {@code payload} field (mcdull/prodigy convention) */
    static TokenExtractor ofField(String fieldName) {
        return body -> body.path(fieldName).asText();
    }

    /** Reads a nested path, e.g. "data.token" */
    static TokenExtractor ofPath(String... paths) {
        return body -> {
            JsonNode node = body;
            for (String path : paths) {
                node = node.path(path);
            }
            return node.asText();
        };
    }
}
