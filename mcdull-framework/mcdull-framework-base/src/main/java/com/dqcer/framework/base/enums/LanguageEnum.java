package com.dqcer.framework.base.enums;

/**
 * 语言枚举
 *
 * @author dqcer
 * @version 2022/07/26
 */
@SuppressWarnings("unused")
public enum LanguageEnum implements IEnum<String> {
    /**
     * 中文
     */
    ZH_CN("zh-CN", "中文"),

    /**
     * 英文
     */
    EN_US("en-US", "英文")
    ;

    LanguageEnum(String code, String text) {
        init(code, text);
    }

    public static LanguageEnum toEnum(String value) {
        switch (value) {
            case "zh-CN":
                return LanguageEnum.ZH_CN;
            case "en-US":
                return LanguageEnum.EN_US;
            default:
                throw new IllegalArgumentException("invalid value , only [zh-CN, en-US] is allowed");
        }
    }
}
