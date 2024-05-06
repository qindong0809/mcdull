package io.gitee.dqcer.mcdull.uac.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 页面类型
 *
 */
public enum CodeGeneratorPageTypeEnum implements IEnum<String> {

    MODAL("modal", "弹窗"),
    DRAWER("drawer", "抽屉"),
    PAGE("page", "新页面");

    private String value;

    private String desc;

    CodeGeneratorPageTypeEnum(String value, String desc) {
        init(value, desc);

    }

    public static CodeGeneratorPageTypeEnum toEnum(String code) {
        switch (code) {
            case "modal":
                return CodeGeneratorPageTypeEnum.MODAL;
            case "drawer":
                return CodeGeneratorPageTypeEnum.DRAWER;
            case "page":
                return CodeGeneratorPageTypeEnum.PAGE;
            default:
                throw new IllegalArgumentException("invalid value , only [modal, drawer, page] is allowed");
        }
    }

}
