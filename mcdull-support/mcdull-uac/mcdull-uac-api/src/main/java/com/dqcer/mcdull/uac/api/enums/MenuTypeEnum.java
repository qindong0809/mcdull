package com.dqcer.mcdull.uac.api.enums;

import com.dqcer.framework.base.enums.IEnum;

/**
 * 菜单枚举
 *
 * @author dqcer
 * @version 2022/07/26
 */
@SuppressWarnings("unused")
public enum MenuTypeEnum implements IEnum<String> {
    /**
     * 菜单
     */
    MENU("menu", "菜单"),

    /**
     * 按钮
     */
    BUTTON("button", "按钮")
    ;

    MenuTypeEnum(String code, String text) {
        init(code, text);
    }

    public static MenuTypeEnum toEnum(String code) {
        switch (code) {
            case "menu":
                return MenuTypeEnum.MENU;
            case "button":
                return MenuTypeEnum.BUTTON;
            default:
                throw new IllegalArgumentException("invalid value , only [menu, button] is allowed");
        }
    }
}
