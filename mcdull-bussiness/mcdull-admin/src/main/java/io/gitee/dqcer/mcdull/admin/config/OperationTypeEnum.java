package io.gitee.dqcer.mcdull.admin.config;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 操作类型的枚举
 *
 * @author dqcer
 * @since 2023/01/18 22:01:95
 */
public enum OperationTypeEnum implements IEnum<String> {

    /**
     * 插入
     */
    INSERT("insert", "新增"),
    /**
     * 更新
     */
    UPDATE("update", "修改"),
    /**
     * 删除
     */
    DELETE("delete", "删除"),

    STATUS("status", "状态"),

    ;

    OperationTypeEnum(String code, String text) {
        init(code, text);
    }
}
