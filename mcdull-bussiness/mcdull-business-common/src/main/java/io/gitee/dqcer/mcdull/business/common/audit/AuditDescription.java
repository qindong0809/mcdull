package io.gitee.dqcer.mcdull.business.common.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Audit Log field
 * @author dqcer
 * @since 2023/06/14
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditDescription {


    String label ();


    String[] tagCharacter() default {"[", "]"};


    String to () default "更新为";
}
