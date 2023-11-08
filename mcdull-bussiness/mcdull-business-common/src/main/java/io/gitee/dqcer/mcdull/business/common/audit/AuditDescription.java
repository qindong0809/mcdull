package io.gitee.dqcer.mcdull.business.common.audit;

import cn.hutool.core.date.DatePattern;

import java.lang.annotation.*;


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

    String[] tagCharacter() default {"\"", "\""};

    String to () default "更新为";

    int sort() default 0;

    String datePattern() default DatePattern.NORM_DATETIME_PATTERN;
}
