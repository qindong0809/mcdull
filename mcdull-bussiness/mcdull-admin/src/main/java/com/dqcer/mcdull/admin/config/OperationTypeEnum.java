package com.dqcer.mcdull.admin.config;

import com.dqcer.mcdull.framework.base.enums.IEnum;

public enum OperationTypeEnum implements IEnum<String> {

    INSERT("insert", "新增"),
    UPDATE("update", "修改"),
    DELETE("delete", "删除"),

    ;

    OperationTypeEnum(String code, String text) {
        init(code, text);
    }
}
