package io.gitee.dqcer.mcdull.system.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum LoginDeviceEnum implements IEnum<Integer> {

    PC(1, "电脑端"),

    ;


    LoginDeviceEnum(Integer code, String text) {
        init(code, text);
    }
}
