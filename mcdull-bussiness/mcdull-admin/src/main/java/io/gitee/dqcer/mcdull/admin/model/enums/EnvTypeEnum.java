package io.gitee.dqcer.mcdull.admin.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 *
 * @author dqcer
 * @since 2022/07/26
 */
public enum EnvTypeEnum implements IEnum<Integer> {

    DEV(1, "开发"),
    TEST(2, "测试"),
    PROD(3, "生产"),

    ;

    EnvTypeEnum(Integer code, String text) {
        init(code, text);
    }

    public static EnvTypeEnum toEnum(Integer value) {
        switch (value) {
            case 1:
                return EnvTypeEnum.DEV;
            case 2:
                return EnvTypeEnum.TEST;
            case 3:
                return EnvTypeEnum.PROD;
            default:
                throw new IllegalArgumentException("invalid value , only ['1', '2', '3'] is allowed");
        }
    }
}
