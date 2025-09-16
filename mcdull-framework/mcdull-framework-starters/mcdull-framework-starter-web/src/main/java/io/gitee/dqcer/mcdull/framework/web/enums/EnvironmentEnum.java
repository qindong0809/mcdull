package io.gitee.dqcer.mcdull.framework.web.enums;

/**
 * environment
 *
 * @author dqcer
 * @since 2024/01/29
 */
public enum EnvironmentEnum implements IEnum<String> {

    PROD("prod", "生产环境"),
    TEST("test", "测试环境"),
    DEV("dev", "开发环境");

    ;

    EnvironmentEnum(String code, String text) {
        init(code, text);
    }
}
