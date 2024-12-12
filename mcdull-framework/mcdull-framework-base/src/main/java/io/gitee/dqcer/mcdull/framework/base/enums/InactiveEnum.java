package io.gitee.dqcer.mcdull.framework.base.enums;

/**
 *
 * @author dqcer
 * @since 2024/01/29
 */

public enum InactiveEnum implements IEnum<Boolean>{

    TRUE(true, "已失活"),

    FALSE(false, "正常");

    InactiveEnum(Boolean code, String text) {
        init(code, text);
    }


}
