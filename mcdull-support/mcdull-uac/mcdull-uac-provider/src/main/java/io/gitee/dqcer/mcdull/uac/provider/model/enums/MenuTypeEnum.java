package io.gitee.dqcer.mcdull.uac.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 菜单枚举
 *
 * @author dqcer
 * @since 2022/07/26
 */
@SuppressWarnings("unused")
public enum MenuTypeEnum implements IEnum<Integer> {


    MENU(0, "菜单"),
    IFRAME(1, "iframe"),
    LINK(2, "外链"),
    BUTTON(3, "按钮")
    ;

    MenuTypeEnum(Integer code, String text) {
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
