package io.gitee.dqcer.mcdull.framework.external.annotation;

import io.gitee.dqcer.mcdull.framework.external.config.ExternalClientRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables scanning and registration of {@link ExternalApi} annotated interfaces.
 * Place on your Spring Boot application class or a configuration class.
 *
 * <pre>
 * &#064;EnableExternalClients(basePackages = "com.example.api")
 * &#064;SpringBootApplication
 * public class MyApplication { }
 * </pre>
 *
 * @author dqcer
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ExternalClientRegistrar.class)
public @interface EnableExternalClients {

    /**
     * Base packages to scan for {@link ExternalApi} interfaces.
     * If empty, defaults to the package of the annotated class.
     */
    String[] basePackages() default {};
}
