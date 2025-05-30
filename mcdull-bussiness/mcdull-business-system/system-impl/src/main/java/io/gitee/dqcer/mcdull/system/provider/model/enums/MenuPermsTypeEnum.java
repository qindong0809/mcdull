package io.gitee.dqcer.mcdull.system.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum MenuPermsTypeEnum implements IEnum<Integer> {

    SA_TOKEN(1, "Sa-Token模式"),
    ;


    MenuPermsTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
