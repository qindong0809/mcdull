package io.gitee.dqcer.mcdull.framework.external.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps a method to an HTTP POST request on the external system.
 *
 * @author dqcer
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExternalPost {

    /**
     * URL path template. Supports {@code {variable}} placeholders
     * resolved from {@link PathVar} annotated parameters.
     */
    String value();
}
