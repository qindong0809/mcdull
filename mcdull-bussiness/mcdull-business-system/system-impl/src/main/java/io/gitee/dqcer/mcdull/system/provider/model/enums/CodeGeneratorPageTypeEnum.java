package io.gitee.dqcer.mcdull.system.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 代码生成器页类型枚举
 * 页面类型
 *
 * @author dqcer
 * @since 2024/06/03
 */
public enum CodeGeneratorPageTypeEnum implements IEnum<String> {

    /**
     * 弹窗
     */
    MODAL("modal", "弹窗"),

    /**
     * 抽屉
     */
    DRAWER("drawer", "抽屉"),

    /**
     * 新页面
     */
    PAGE("page", "新页面");

    private String value;

    private String desc;

    CodeGeneratorPageTypeEnum(String value, String desc) {
        init(value, desc);

    }
}
