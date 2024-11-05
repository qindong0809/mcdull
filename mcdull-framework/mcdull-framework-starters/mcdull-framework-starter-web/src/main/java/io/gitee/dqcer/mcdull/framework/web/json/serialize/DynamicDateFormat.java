package io.gitee.dqcer.mcdull.framework.web.json.serialize;


import cn.hutool.core.date.DatePattern;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 动态日期格式
 *
 * @author dqcer
 * @since 2024/06/25
 */
@Documented
@Retention(RUNTIME)
@Target(value = { ElementType.FIELD})
public @interface DynamicDateFormat {

    /**
     * 启用时区
     *
     * @return boolean
     */
    boolean enableTimezone() default false;

    /**
     * 附加时区样式
     * 2024-06-25 09:00:00(+08:00)
     *
     * @return boolean
     */
    boolean appendTimezoneStyle() default true;

    /**
     * 日期格式
     *
     * @return {@link String }
     */
    String dateFormat() default DatePattern.NORM_DATE_PATTERN;
}
