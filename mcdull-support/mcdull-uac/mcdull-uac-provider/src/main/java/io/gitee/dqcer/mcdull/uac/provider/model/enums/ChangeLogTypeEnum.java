package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum ChangeLogTypeEnum implements IEnum<Integer> {

    /**
     * 重大更新
     */
    MAJOR_UPDATE(1, "重大更新"),

    /**
     * 功能更新
     */
    FUNCTION_UPDATE(2, "功能更新"),

    /**
     * Bug修复
     */
    BUG_FIX(3, "Bug修复"),
    ;


    ChangeLogTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
