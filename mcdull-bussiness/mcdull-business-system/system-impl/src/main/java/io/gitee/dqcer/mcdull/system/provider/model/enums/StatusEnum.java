package io.gitee.dqcer.mcdull.system.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 状态枚举
 *
 * @author dqcer
 * @since 2024/03/01
 */
public enum StatusEnum implements IEnum<String> {

    /**
     * 启用
     */
    ENABLE("0", "启动"),

    /**
     * 禁用
     */
    DISABLE("1", "停用");


    StatusEnum(String code, String text) {
        init(code, text);
    }
}
