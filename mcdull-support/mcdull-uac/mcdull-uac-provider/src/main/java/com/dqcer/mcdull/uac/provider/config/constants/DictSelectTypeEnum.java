package com.dqcer.mcdull.uac.provider.config.constants;

import com.dqcer.framework.base.dict.IDict;

public enum DictSelectTypeEnum implements IDict<String> {

    /**
     * 状态
     */
    STATUS("status_type", "状态"),

    ;

    DictSelectTypeEnum(String selectType, String text) {
        init(selectType, text);
    }
}
