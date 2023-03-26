package io.gitee.dqcer.mcdull.admin.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 内置码表类型枚举
 *
 * @author dqcer
 * @since 2022/07/26
 */
@SuppressWarnings("unused")
public enum DictTypeEnum implements IEnum<String> {

    /**
     * 状态
     */
    STATUS("status", "启停"),

    /**
     * delete flag
     */
    DELETE_FLAG("del_flag", "删除"),

    ;

    DictTypeEnum(String code, String text) {
        init(code, text);
    }

}
