package com.dqcer.framework.base.mq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主题定义
 *
 * @author dqcer
 * @date 2022/10/05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@SuppressWarnings("unused")
public @interface Topic {

    String value();
}
