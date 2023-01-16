package io.gitee.dqcer.mdc.provider.config.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 下拉字段设置
 *
 * @author dqcer
 * @date 2022/11/20 22:11:72
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DropDownSetField {



    SelectTypeEnum value();
}