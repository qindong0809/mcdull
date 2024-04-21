package io.gitee.dqcer.mcdull.framework.base.enums;

/**
 * environment
 *
 * @author dqcer
 * @since 2024/01/29
 */
public enum EnvironmentEnum implements IEnum<String>{

    PROD("prod", "已失活"),

    ;

    EnvironmentEnum(String code, String text) {
        init(code, text);
    }
}
