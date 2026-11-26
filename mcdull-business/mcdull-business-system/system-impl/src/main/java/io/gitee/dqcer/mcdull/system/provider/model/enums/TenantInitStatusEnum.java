package io.gitee.dqcer.mcdull.system.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

public enum TenantInitStatusEnum implements IEnum<Integer> {
    NOT_INIT(0, "未初始化"),
    RUNNING(1, "初始化中"),
    SUCCESS(2, "成功"),
    FAILED(3, "失败");

    TenantInitStatusEnum(Integer value, String desc) {
        this.init(value, desc);
    }
}
