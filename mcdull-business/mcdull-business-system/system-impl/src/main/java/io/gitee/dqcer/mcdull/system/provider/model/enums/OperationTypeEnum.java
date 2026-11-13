package io.gitee.dqcer.mcdull.system.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 操作类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum OperationTypeEnum implements IEnum<Integer> {

    ADD(1, "新增"),
    UPDATE(2, "更新"),
    ENABLE(3, "启用"),
    DISABLE(4, "停用"),
    DELETE(5, "删除"),

    ;


    OperationTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
