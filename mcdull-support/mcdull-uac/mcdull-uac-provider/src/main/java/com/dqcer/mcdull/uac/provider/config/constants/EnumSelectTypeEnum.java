package com.dqcer.mcdull.uac.provider.config.constants;

import com.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 枚举类型
 *
 * @author dqcer
 * @version 2022/12/25
 */
public enum EnumSelectTypeEnum implements IEnum<String> {

    /**
     * 状态
     */
    STATUS("status_type", "状态"),

    ;

    EnumSelectTypeEnum(String selectType, String text) {
        init(selectType, text);
    }
}
