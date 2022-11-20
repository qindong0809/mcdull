package com.dqcer.mcdull.mdc.provider.config.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel长度
 *
 * @author dqcer
 * @date 2022/11/20 22:11:27
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelLength {

    /**
     * min
     * @return
     */
    int min();

    /**
     * max
     * @return
     */
    int max();
}
