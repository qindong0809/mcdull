package io.gitee.dqcer.mcdull.framework.external.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares an interface as an external system API client.
 * The framework generates a dynamic proxy implementation that handles
 * authentication, serialization, retry, and observability.
 *
 * <pre>
 * &#064;ExternalApi(name = "prodigy")
 * public interface ProdigyApi {
 *     &#064;ExternalPost("/upload/presign")
 *     PreSignVo presignUpload(&#064;Body PreSignRequest request);
 * }
 * </pre>
 *
 * @author dqcer
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExternalApi {

    /**
     * External system instance name. Must match the configuration key
     * under {@code mcdull.external.clients.{name}}.
     */
    String name();
}
