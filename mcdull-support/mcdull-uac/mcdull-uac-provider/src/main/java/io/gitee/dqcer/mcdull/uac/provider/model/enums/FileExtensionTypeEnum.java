package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum FileExtensionTypeEnum implements IEnum<String> {

    PDF(".pdf", "pdf"),

    EXCEL_X(".xlsx", "xlsx"),
    ;


    FileExtensionTypeEnum(String code, String text) {
        init(code, text);
    }

}
