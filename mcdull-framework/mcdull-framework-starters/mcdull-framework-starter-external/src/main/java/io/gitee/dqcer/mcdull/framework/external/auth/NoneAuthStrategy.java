package io.gitee.dqcer.mcdull.framework.external.auth;

import java.util.Collections;
import java.util.Map;

/**
 * No authentication. Used for public APIs or presigned URLs.
 *
 * @author dqcer
 * @since 1.0.0
 */
public class NoneAuthStrategy implements AuthStrategy {

    @Override
    public Map<String, String> getAuthHeaders() {
        return Collections.emptyMap();
    }
}
