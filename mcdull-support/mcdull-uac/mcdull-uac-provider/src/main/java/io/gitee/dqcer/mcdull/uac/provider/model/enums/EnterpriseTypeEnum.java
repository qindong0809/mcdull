package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum EnterpriseTypeEnum implements IEnum<Integer> {

    NORMAL(1, "有限企业"),

    /**
     * 外资企业
     */
    FOREIGN(2, "外资企业"),
    ;


    EnterpriseTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
