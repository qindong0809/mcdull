package io.gitee.dqcer.mcdull.admin.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 菜单枚举
 *
 * @author dqcer
 * @since 2022/07/26
 */
@SuppressWarnings("unused")
public enum MenuTypeEnum implements IEnum<String> {

    /**
     * 目录
     */
    DIRECTORY("M", "目录"),

    /**
     * 菜单
     */
    MENU("C", "菜单"),

    /**
     * 按钮
     */
    BUTTON("C", "菜单")
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
