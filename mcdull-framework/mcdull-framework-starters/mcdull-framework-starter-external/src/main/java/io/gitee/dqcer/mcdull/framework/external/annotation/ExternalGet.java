package io.gitee.dqcer.mcdull.framework.external.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps a method to an HTTP GET request on the external system.
 *
 * @author dqcer
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExternalGet {

    /**
     * URL path template. Supports {@code {variable}} placeholders.
     */
    String value();
}
