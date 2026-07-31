package io.gitee.dqcer.mcdull.framework.external.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binds a method parameter to a path variable in the URL template.
 *
 * @author dqcer
 * @since 1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVar {

    /**
     * The path variable name to bind (must match {@code {name}} in the URL template).
     */
    String value();
}
