package io.gitee.dqcer.admin.config;

import io.gitee.dqcer.framework.base.enums.IEnum;

public enum OperationTypeEnum implements IEnum<String> {

    INSERT("insert", "新增"),
    UPDATE("update", "修改"),
    DELETE("delete", "删除"),

    ;

    OperationTypeEnum(String code, String text) {
        init(code, text);
    }
}
