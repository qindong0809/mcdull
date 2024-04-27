package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 菜单类型枚举
 *
 * @author dqcer
 */
public enum MenuTypeEnum implements IEnum<Integer> {
    /**
     * 目录
     */
    CATALOG(1, "目录"),
    /**
     * 菜单
     */
    MENU(2, "菜单"),
    /**
     * 功能点
     */
    POINTS(3, "功能点");



    MenuTypeEnum(Integer value, String desc) {
        this.init(value, desc);
    }

}
