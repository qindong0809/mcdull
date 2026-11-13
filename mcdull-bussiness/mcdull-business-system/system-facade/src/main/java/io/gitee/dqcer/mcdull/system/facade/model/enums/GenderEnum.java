package io.gitee.dqcer.mcdull.system.facade.model.enums;


import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 性别枚举（共享给业务模块）
 */
public enum GenderEnum implements IEnum<Integer> {

    /**
     * 0 未知
     */
    UNKNOWN(0, "未知"),

    /**
     * 男 1 奇数为阳
     */
    MAN(1, "男"),

    /**
     * 女 2 偶数为阴
     */
    WOMAN(2, "女");
    ;


    GenderEnum(Integer code, String text) {
        init(code, text);
    }
}
