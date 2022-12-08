package com.dqcer.mcdull.uac.provider.config.constants;

import com.dqcer.framework.base.enums.IEnum;

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
