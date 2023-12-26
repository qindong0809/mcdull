package io.gitee.dqcer.mcdull.mdc.provider.config.excel;

import java.lang.annotation.*;

/**
 * 下拉字段设置
 *
 * @author dqcer
 * @since 2022/11/20 22:11:72
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DropDownSetField {



    SelectTypeEnum value();
}